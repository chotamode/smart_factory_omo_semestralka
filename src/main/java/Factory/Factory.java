package Factory;

import EventManagement.Channels.RepairEventChannel;
import Exceptions.NotEnoughMaterialException;
import Management.Visitable;
import Management.Visitor;
import Product.Material.MaterialType;
import ProductionEntity.Device.Device;
import ProductionEntity.Human.Repairman;
import Operation.OperationalCapable;
import Operation.WorkType.WorkType;
import Product.Material.StockMaterial;
import Product.Product;
import Product.ProductSeries;
import Util.TimeAndReportManager;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Factory.Factory is a place where production takes place.
 * Factory.Factory has a stock of materials.
 * Factory.Factory has a production line.
 * Factory.Factory has a warehouse.
 * Factory.Factory can't create new Factory.ProductionLine, it can only reassemble it.
 * In this project we will have to types of factories: Toy Manufacturing Factory.Factory and Electronics Manufacturing Factory.Factory.
 * But it is possible to add more types of factories.
 */
@Getter
@Setter
public abstract class Factory implements Visitable {
    private List<StockMaterial> stockMaterials = new ArrayList<StockMaterial>();
    private List<ProductionLine> productionLines = new ArrayList<ProductionLine>();

    private List<Repairman> repairmen = new ArrayList<Repairman>();

    private List<OperationalCapable> operationalCapables = new ArrayList<OperationalCapable>();

    protected List<ProductSeries> orderedProductSeries = new ArrayList<ProductSeries>();
    protected List<ProductSeries> producedProductSeries = new ArrayList<ProductSeries>();

    private RepairEventChannel repairEventChannel = new RepairEventChannel();

    Logger logger = LogManager.getLogger(Factory.class);

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * Accept order to produce product in given amount. And find the right and free production line for this product.
     * If there is no production line for this product, find free Factory.ProductionLine and reassemble it for this product.
     *
     * @param productName Name of product to produce different for each factory
     * @param amount      Amount of product to produce
     */
    public abstract void acceptOrder(String productName, int amount);

    /**
     * Find production line assembled for given product and free to use.
     * If there is no production line for this product, find free Factory.ProductionLine and reassemble it for this product.
     *
     * @param productSeries Product series to find production line for
     * @return Factory.ProductionLine for given product
     */
    private ProductionLine findProductionLine(ProductSeries productSeries) {
        ProductionLine productionLine;
        productionLine = productionLines.stream()
                .filter(line -> line.getState() == ProductionLineState.FREE)
                .findFirst()
                .orElse(null);
        if (productionLine == null) {
            throw new RuntimeException("No free production line");
        } else {
            productionLine.assembleProductionLine(productSeries);
        }

        return productionLine;
    }

    /**
     * Deliver order to client. Gets products from warehouse and removes them from warehouse.
     *
     * @param productName Name of product to deliver
     * @param amount      Amount of product to deliver
     * @return List of delivered products
     */
    public Product deliverOrder(String productName, int amount) {
        //TODO: get products from warehouse and remove them from warehouse
        return null;
    }

    public void addProductionLine(ProductionLine productionLine) {
        productionLines.add(productionLine);
    }

    /**
     * Find Worker, Cobot or Machine for given work type.
     *
     * @param workType Work type to find worker for
     * @return Worker for given work type
     */
    private OperationalCapable findWorker(WorkType workType) throws Exception {
        //TODO: worker on another production line should not be able to work on this production line.
        OperationalCapable worker = null;
        worker = operationalCapables.stream()
                .filter(operationalCapable -> operationalCapable.getWorkType() == workType && !operationalCapable.isWorking())
                .findFirst()
                .orElse(null);
        if (worker == null) {
            throw new Exception("No free worker for work type " + workType);
        } else {
            return worker;
        }
    }

    private void addWorkersToProductionLine(ProductionLine productionLine) throws Exception {
        for (WorkType workType : productionLine.getWorkerSequence()) {
            OperationalCapable worker = findWorker(workType);
            productionLine.addWorker(worker);
            worker.setWorking(true);
        }
    }

    public boolean isWorking() {
        for (ProductionLine productionLine : productionLines) {
            if (productionLine.getState() == ProductionLineState.WORKING) {
                return true;
            }
        }
        return false;
    }

    public boolean canAcceptOrder() {
        if (orderedProductSeries.size() < productionLines.size() || haveFreeProductionLine()) {
            return true;
        } else {
            throw new RuntimeException("No free production line.");
        }
    }

    public boolean haveFreeProductionLine() {
        for (ProductionLine productionLine : productionLines) {
            if (productionLine.getState() == ProductionLineState.FREE) {
                return true;
            }
        }
        return false;
    }

    public void startProduction() {

        for (ProductSeries orderedProductSeries : orderedProductSeries) {
            try {
                findStockMaterialsForProduct(orderedProductSeries);
                orderedProductSeries.addMaterials();
            } catch (Exception e) {
                logger.error(e.getMessage());
                return;
            }
        }

        try {
            addDevicesAndRepairmenToChannel();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        TimeAndReportManager.getInstance().start();

        for (ProductSeries orderedProductSeries : orderedProductSeries) {
            ProductionLine productionLine = findProductionLine(orderedProductSeries);

            assert productionLine != null;
            try {
                addWorkersToProductionLine(productionLine);
            } catch (Exception e) {
                logger.error(e.getMessage());
                return;
            }
        }

        for (ProductionLine productionLine : productionLines) {
            try {
                productionLine.workUntilFinished();
            } catch (Exception e) {
                stopAllProductionLines();
                logger.error(e.getMessage());
                return;
            }
        }
    }

    private void addDevicesAndRepairmenToChannel() {
        for (OperationalCapable operationalCapable : operationalCapables) {
            if (operationalCapable instanceof Device) {
                ((Device) operationalCapable).subscribeAsPublisher(repairEventChannel);
            }
        }
        for (Repairman repairman : repairmen) {
            repairman.subscribeAsListener(repairEventChannel);
            repairman.subscribeAsPublisher(repairEventChannel);
        }
    }

    private ProductionLine findLineForProduct(ProductSeries productSeries) {
        for (ProductionLine productionLine : productionLines) {
            if (productionLine.getProductSeries() != null && productionLine.getProductSeries().equals(productSeries)) {
                return productionLine;
            }
        }
        return null;
    }

    private void stopAllProductionLines() {
        for (ProductionLine productionLine : productionLines) {
            productionLine.setState(ProductionLineState.STOPPED);
        }
    }

    private StockMaterial orderMaterial(MaterialType materialType, int amount) {
        StockMaterial stockMaterial = null;
        if(stockMaterials.stream().anyMatch(material -> material.getType().equals(materialType))) {
            for (StockMaterial sm : stockMaterials) {
                if (sm.getType().equals(materialType)) {
                    sm.add(amount);
                    logger.info("Ordered " + amount + " " + materialType.getName() + " for " + sm.getPrice() * amount + " USD");
                    return sm;
                }
            }
        } else {
            stockMaterial = new StockMaterial(amount, materialType);
            stockMaterials.add(stockMaterial);
        }

        assert stockMaterial != null;
        logger.info("Ordered " + amount + " " + materialType.getName() + " for " + stockMaterial.getPrice() * amount + " USD");
        return stockMaterial;
    }

    private void addMaterialsToSeries(ProductSeries productSeries) {
        for (Entry<MaterialType, Integer> entry : productSeries.getTotalMaterialNeeded().entrySet()) {
            MaterialType materialType = entry.getKey();
            int amount = entry.getValue();
            orderMaterial(materialType, amount);
        }
    }

    private void getMaterialsFromStock(ProductSeries productSeries) throws Exception {
        for (Entry<MaterialType, Integer> entry : productSeries.getTotalMaterialNeeded().entrySet()) {
            MaterialType materialType = entry.getKey();
            int amount = entry.getValue();
            for (StockMaterial stockMaterial : stockMaterials) {
                if (stockMaterial.getType() == materialType) {
                    stockMaterial.use(amount);
                }
            }
        }
    }

    private void findStockMaterialsForProduct(ProductSeries productSeries) throws Exception {
        List<StockMaterial> stockMaterialsToAdd = new ArrayList<>();

        for (Entry<MaterialType, Integer> entry : productSeries.getTotalMaterialNeeded().entrySet()) {
            MaterialType materialType = entry.getKey();
            int amount = entry.getValue();
            for (StockMaterial stockMaterial : stockMaterials) {
                if (stockMaterial.getType() == materialType) {
                    if (stockMaterial.getQuantity() < amount) {
                        stockMaterial = orderMaterial(materialType, amount);
                    }
                }
                stockMaterialsToAdd.add(stockMaterial);
            }
        }
        for(Entry<MaterialType, Integer> entry : productSeries.getNotSetStockMaterials().entrySet()) {
            if(stockMaterialsToAdd.stream().noneMatch(material -> material.getType().equals(entry.getKey()))){
                MaterialType materialType = entry.getKey();
                int amount = entry.getValue();
                StockMaterial stockMaterial = orderMaterial(materialType, amount);
                stockMaterialsToAdd.add(stockMaterial);
            }
        }
        productSeries.setStockMaterials(stockMaterialsToAdd);
    }
}
