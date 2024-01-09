package Factory;

import EventManagement.Channels.ProductionEventChannel;
import ProductionEntity.Human.Repairman;
import Operation.OperationalCapable;
import Operation.WorkType.WorkType;
import Product.StockMaterial;
import Product.Product;
import Product.ProductSeries;
import Util.TimeAndReportManager;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
public abstract class Factory {
    private List<StockMaterial> stockMaterials = new ArrayList<StockMaterial>();
    private List<ProductionLine> productionLines = new ArrayList<ProductionLine>();

    private List<Repairman> repairmen = new ArrayList<Repairman>();

    private List<OperationalCapable> operationalCapables = new ArrayList<OperationalCapable>();

    protected List<ProductSeries> orderedProductSeries = new ArrayList<ProductSeries>();
    protected List<ProductSeries> producedProductSeries = new ArrayList<ProductSeries>();

    private ProductionEventChannel productionEventChannel = new ProductionEventChannel();

    private boolean working = false;

    /**
     * Accept order to produce product in given amount. And find the right and free production line for this product.
     * If there is no production line for this product, find free Factory.ProductionLine and reassemble it for this product.
     *
     * @param productName Name of product to produce different for each factory
     * @param amount Amount of product to produce
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
                .filter(line -> !line.isWorking())
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
     * @param amount Amount of product to deliver
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
    private OperationalCapable findWorker(WorkType workType) {
        //TODO: worker on another production line should not be able to work on this production line.
        OperationalCapable worker = null;
        worker = operationalCapables.stream()
                .filter(operationalCapable -> operationalCapable.getWorkType() == workType)
                .findFirst()
                .orElse(null);
        if (worker == null) {
            throw new RuntimeException("No worker for this work type");
        } else {
            return worker;
        }
    }

    private void addWorkersToProductionLine(ProductionLine productionLine) {
        for (WorkType workType : productionLine.getWorkerSequence()) {
            OperationalCapable worker = findWorker(workType);
            productionLine.addWorker(worker);
        }
    }

    public boolean isWorking() {
        for(ProductionLine productionLine : productionLines) {
            if(productionLine.isWorking()) {
                return true;
            }
        }
        return false;
    }

    public boolean canAcceptOrder(){
        if(orderedProductSeries.size() < productionLines.size() || haveFreeProductionLine()){
            return true;
        }else{
            throw new RuntimeException("No free production line.");
        }
    }

    public boolean haveFreeProductionLine(){
        for(ProductionLine productionLine : productionLines) {
            if (!productionLine.isWorking()) {
                return true;
            }
        }
        return false;
    }

    public void startProduction() {
        TimeAndReportManager.getInstance().start();

        for(ProductSeries orderedProductSeries : orderedProductSeries){
            ProductionLine productionLine = findProductionLine(orderedProductSeries);

            assert productionLine != null;
            addWorkersToProductionLine(productionLine);
        }

        for(ProductionLine productionLine : productionLines){
            productionLine.workUntilFinished();
        }
    }

    private ProductionLine findLineForProduct(ProductSeries productSeries){
        for(ProductionLine productionLine : productionLines){
            if(productionLine.getProductSeries() != null && productionLine.getProductSeries().equals(productSeries)){
                return productionLine;
            }
        }
        return null;
    }
}
