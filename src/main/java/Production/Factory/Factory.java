package Production.Factory;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.Events.EventType;
import EventManagement.Events.RepairEvent;
import Management.Director;
import Management.Inspector;
import Management.Visitable;
import Management.Visitor;
import Operation.WorkType.WorkType;
import Product.Material.MaterialType;
import Product.Material.StockMaterial;
import Product.ProductSeries;
import Production.Configurable;
import Production.Configuration;
import Production.ProductionEntity.Device.Device;
import Production.ProductionEntity.Device.Resourse.ConsumptionData;
import Production.ProductionEntity.Human.Repairman;
import Production.ProductionEntity.OperationalCapable;
import Production.ProductionLine.ProductionLine;
import Production.ProductionLine.ProductionLineState;
import Util.TimeAndReportManager;
import Util.TimeObserver;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
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
public abstract class Factory implements Visitable, Configurable, TimeObserver {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.NONE)
    protected List<ProductSeries> orderedProductSeries = new ArrayList<>();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.NONE)
    protected List<ProductSeries> producedProductSeries = new ArrayList<>();

    private Logger logger = LogManager.getLogger(Factory.class);

    private List<StockMaterial> stockMaterials = new ArrayList<>();
    private List<ProductionLine> productionLines = new ArrayList<>();
    private List<Repairman> repairmen = new ArrayList<>();
    private List<OperationalCapable> operationalCapables = new ArrayList<>();
    private RepairEventChannel repairEventChannel = new RepairEventChannel();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Director director = new Director(this);

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Inspector inspector = new Inspector(this);

    public Factory() {
        subscribeToTimeAndReportManager();
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Configuration getConfigurations() {
        List<Map<String, String>> configurations = new ArrayList<>();
        Map<Configurable, Configuration> configurables = new LinkedHashMap<>();
        Configuration configuration;

        configurations.add(Map.of("Number of repairmen", String.valueOf(repairmen.size())));
        configurations.add(Map.of("Number of production lines", String.valueOf(productionLines.size())));
        configurations.add(Map.of("Number of operational capables", String.valueOf(operationalCapables.size())));
        configurations.add(Map.of("Number of stock materials", String.valueOf(stockMaterials.size())));
        configurations.add(Map.of("Number of ordered product series", String.valueOf(orderedProductSeries.size())));
        configurations.add(Map.of("Number of produced product series", String.valueOf(producedProductSeries.size())));

        for (ProductionLine productionLine : productionLines) {
            configurables.put(productionLine, productionLine.getConfigurations());
        }

        for (OperationalCapable operationalCapable : operationalCapables) {
            configurables.put(operationalCapable, operationalCapable.getConfigurations());
        }

        configuration = new Configuration(configurations, this);
        configuration.getConfigurableConfigurationMap().putAll(configurables);

        return configuration;
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
            logger.info("Reassembling production line assembled for " + (productionLine.getProductSeries() != null ? productionLine.getProductSeries().getProductName() : "nothing") + " for product " + productSeries.getProductName());
            productionLine.assembleProductionLine(productSeries);
        }

        return productionLine;
    }

    /**
     * Deliver order to client. Gets products from warehouse and removes them from warehouse.
     * If there is not enough products in warehouse, throws exception.
     * If there is enough products in warehouse, removes them from warehouse.
     *
     */

    public ProductSeries deliverOrder(String productName, int amount) throws Exception {
        ProductSeries productSeries = null;
        for (ProductSeries producedProductSeries : producedProductSeries) {
            if (producedProductSeries.getProductName().equals(productName)) {
                if (producedProductSeries.getAmountInSeries() == amount) {
                    productSeries = producedProductSeries;
                    break;
                } else {
                    throw new Exception("Not enough products in warehouse");
                }
            }
        }
        if (productSeries == null) {
            throw new Exception("No " + productName + " in amount of " + amount + " in warehouse");
        } else {
            orderedProductSeries.remove(productSeries);
            return productSeries;
        }
    }

    public List<ProductSeries> deliverAllOrders() {
        List<ProductSeries> productSeriesList = new ArrayList<>(producedProductSeries);
        producedProductSeries.clear();
        return productSeriesList;
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
        transferReadyProductsToWarehouse();
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

    public void startProduction() throws Exception {

        for (ProductSeries orderedProductSeries : orderedProductSeries) {
            findStockMaterialsForProduct(orderedProductSeries);
            orderedProductSeries.addMaterials();
        }

        addDevicesAndRepairmenToChannel();

        TimeAndReportManager.getInstance().start();

        for (ProductSeries orderedProductSeries : orderedProductSeries) {
            ProductionLine productionLine = findProductionLine(orderedProductSeries);
            addWorkersToProductionLine(productionLine);
        }

        for (ProductionLine productionLine : productionLines) {
            productionLine.workUntilFinished();
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

    private void stopAllProductionLines() {
        for (ProductionLine productionLine : productionLines) {
            productionLine.setState(ProductionLineState.STOPPED);
        }
    }

    private StockMaterial orderMaterial(MaterialType materialType, int amount) {
        StockMaterial stockMaterial = null;
        if (stockMaterials.stream().anyMatch(material -> material.getType().equals(materialType))) {
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

    private void findStockMaterialsForProduct(ProductSeries productSeries) {
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
        for (Entry<MaterialType, Integer> entry : productSeries.getNotSetStockMaterials().entrySet()) {
            if (stockMaterialsToAdd.stream().noneMatch(material -> material.getType().equals(entry.getKey()))) {
                MaterialType materialType = entry.getKey();
                int amount = entry.getValue();
                StockMaterial stockMaterial = orderMaterial(materialType, amount);
                stockMaterialsToAdd.add(stockMaterial);
            }
        }
        productSeries.setStockMaterials(stockMaterialsToAdd);
    }

    @Override
    public boolean onTimeUpdate(Long time) {
        TimeAndReportManager.getInstance().addConfiguration(getConfigurations());
        TimeAndReportManager.getInstance().addConsumptionData(getConsumptionData());
        if (time % 100 == 0) {
            director.executeInspection();
            inspector.executeInspection();
        }
        return false;
    }

    private Set<ConsumptionData> getConsumptionData() {
        Set<ConsumptionData> consumptionDataSet = new HashSet<>();
        for (ProductionLine productionLine : productionLines) {
            Set<ConsumptionData> consumptionData = productionLine.getConsumptionData();
            for (ConsumptionData CD : consumptionData) {
                CD.setFactory(this);
            }
            consumptionDataSet.addAll(consumptionData);
        }
        return consumptionDataSet;
    }

    /**
     * Get state of Devices at particular time.
     */
    public void getDevicesState(Long time) {
        for (OperationalCapable operationalCapable : operationalCapables) {
            if (operationalCapable instanceof Device) {
                logger.info("Device " + operationalCapable.getName() + " state: " + getDeviceState((Device) operationalCapable, time) + " at time " + time);
            }
        }
    }

    private String getDeviceState(Device device, Long time) {
        String state = "Functional";
        List<RepairEvent> repairEvents = new ArrayList<>();
        repairEvents.addAll(repairEventChannel.getEvents());
        repairEvents.addAll(repairEventChannel.getReactedEvents());
        repairEvents = repairEvents.stream()
                .filter(repairEvent -> repairEvent.getDevice().equals(device))
                .sorted(Comparator.comparing(RepairEvent::getTimeStamp))
                .toList();

        for (RepairEvent repairEvent : repairEvents) {
            if (repairEvent.getTimeStamp() <= time) {
                if (repairEvent.getType().equals(EventType.OIL_REFILL_DONE) && state.equals("Needs oil refill")) {
                    state = "Functional";
                } else if (repairEvent.getType().equals(EventType.REPAIR_DONE) && state.equals("Needs repair")) {
                    state = "Functional";
                }

                if (repairEvent.getType().equals(EventType.NEEDS_REPAIR)) {
                    state = "Needs repair";
                } else if (repairEvent.getType().equals(EventType.NEEDS_OIL_REFILL)) {
                    state = "Needs oil refill";
                }
            }
        }
        return state;
    }

    public void transferReadyProductsToWarehouse() {
        for (ProductSeries productSeries : orderedProductSeries) {
            if (productSeries.isFinished()) {
                producedProductSeries.add(productSeries);
            }
        }
        orderedProductSeries.removeAll(producedProductSeries);
    }

    public void addRepairman(Repairman repairman) {
        repairmen.add(repairman);
    }

    public void addWorker(OperationalCapable o) {
        operationalCapables.add(o);
    }

    public void executeInspectorInspection() {
        inspector.executeInspection();
    }

    public void executeDirectorInspection() {
        director.executeInspection();
    }

}
