package Production.ProductionLine;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.EventPublisher.ProductionEventPublisher;
import EventManagement.Events.EventType;
import EventManagement.Events.ProductionEvent;
import Management.Visitable;
import Management.Visitor;
import Operation.WorkType.WorkType;
import Product.ProductSeries;
import Production.Configurable;
import Production.Configuration;
import Production.ProductionEntity.Device.Device;
import Production.ProductionEntity.Device.Resourse.ConsumptionData;
import Production.ProductionEntity.OperationalCapable;
import Util.TimeAndReportManager;
import Util.TimeObserver;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Production line used to create products. Production line is different for each product.
 * It consists of devices and workers, each of which have link to next device/worker in the line.
 */
@Getter
@Setter
public class ProductionLine implements TimeObserver, Visitable, Configurable, ProductionEventPublisher {

    private static int lastId = 0;
    private final int id = lastId++;

    private OperationalCapable firstWorker;
    private OperationalCapable lastWorker;

    /**
     * Priority of the production line. Higher priority means that malfunction of the line will be fixed sooner.
     */
    private int priority = 1;

    private ProductSeries productSeries;

    private List<WorkType> workerSequence = new ArrayList<>();
    private ProductionLineState state = ProductionLineState.FREE;

//    @Setter(AccessLevel.NONE)
    private int currentProductIndex = 0;

    private ProductionEventChannel productionEventChannel = new ProductionEventChannel();

    private Logger logger = LogManager.getLogger(ProductionLine.class);

    public ProductionLine() {
        subscribeToTimeAndReportManager();
    }

    public ProductionLine(int priority) {
        subscribeToTimeAndReportManager();
        this.priority = priority;
    }


    /**
     * Adds worker to the production line
     *
     * @param worker Object of class implementing ProductionEntity interface
     */
    public void addWorker(OperationalCapable worker) {
        worker.subscribeToProductionEventChannel(productionEventChannel);
        worker.setLinePriority(priority);
        if (firstWorker == null) {
            firstWorker = worker;
        } else {
            lastWorker.setNextWorker(worker);
        }
        lastWorker = worker;
    }

    /**
     * Assemble production line for given product series.
     * Finding the right first worker and then adding all the workers to the line by setting nextWorker.
     *
     * @param productSeries Product series to assemble production line for
     */
    public void assembleProductionLine(ProductSeries productSeries) {
        this.firstWorker = null;
        this.currentProductIndex = 0;
        this.productSeries = productSeries;
        this.workerSequence = productSeries.getWorkerSequence();
        this.productionEventChannel.unsubscribeAll();

        this.state = ProductionLineState.AWAITING_START;
    }

    /**
     * Every worker performs operation on product and passes it to the next worker if it's finished
     * by creating new event and putting it to the ProductionEventChannel.
     * If there is no next worker, product is finished.
     */
    public void work() {
        if (currentProductIndex >= productSeries.getProducts().size()) {
            state = ProductionLineState.FREE;
            logger.info("Production line finished working");
            return;
        }
        if (firstWorker == null) {
            throw new RuntimeException("No workers in production line for product "
                    + productSeries.getProductName() +
                    " with index " +
                    currentProductIndex +
                    " at time " +
                    TimeAndReportManager.getInstance().getCurrentTime());
        }
        productionEventChannel.publishEvent(new ProductionEvent(
                "Product is ready for next operation",
                EventType.PRODUCT_READY_FOR_NEXT_OPERATION,
                productSeries.getProducts().get(currentProductIndex),
                firstWorker,
                this,
                TimeAndReportManager.getInstance().getCurrentTime()
        ));

        currentProductIndex++;
    }

    /**
     * Starts working on product series.
     * Checks if the production line is ready to start working on product series.
     * If it is, sets state to WORKING and sets all the workers to working.
     */
    public void workUntilFinished() throws Exception {

        if (!isLineReady()) {
            throw new RuntimeException("Production line is not ready to start working");
        }

        state = ProductionLineState.WORKING;
        logger.info("Production line started working");
    }

    @Override
    public boolean onTimeUpdate(Long time) {
        if (state == ProductionLineState.WORKING) {
            if (productSeries.isFinished()) {
                logger.info(TimeAndReportManager.getInstance().getTimeInYMDH() + " Production line finished working");
                state = ProductionLineState.FREE;
                for (OperationalCapable worker : getWorkers()) {
                    worker.setWorking(false);
                }
                return false;
            }

            if (currentProductIndex < productSeries.getProducts().size()) {
                logger.info("Factoryline" + id + " is working on product " + productSeries.getProducts().get(currentProductIndex).getName() + productSeries.getProducts().get(currentProductIndex).getSeriesIndex());

                work();
                return true;
            }
        }
        return false;
    }

    public List<OperationalCapable> getWorkers() {
        List<OperationalCapable> workers = new ArrayList<>();
        OperationalCapable worker = firstWorker;
        while (worker != null) {
            workers.add(worker);
            worker = worker.getNextWorker();
        }
        return workers;
    }

    /**
     * Checks if the production line is ready to start working on product series.
     * Checks if there is enough workers in the line and if they are capable of working on the product series.
     *
     */
    private boolean isLineReady() throws Exception {
        if (productSeries == null) {
            throw new Exception("Production line is not assembled");
        }
        if (workerSequence.size() < productSeries.getWorkerSequence().size()) {
            throw new Exception("Not enough workers in production line for product " + productSeries.getProductName());
        }
        for (int i = 0; i < productSeries.getWorkerSequence().size(); i++) {
            if (!workerSequence.get(i).equals(productSeries.getWorkerSequence().get(i))) {
                throw new Exception("Production line is not capable of working on product " + productSeries.getProductName());
            }
        }
        return true;
    }

    public List<OperationalCapable> getOperationalCapables() {
        List<OperationalCapable> operationalCapables = new ArrayList<>();
        OperationalCapable operationalCapable = firstWorker;
        while (operationalCapable != null) {
            operationalCapables.add(operationalCapable);
            operationalCapable = operationalCapable.getNextWorker();
        }
        return operationalCapables;
    }

    public void setLinePriority(int linePriority) {
        this.priority = linePriority;
        for (OperationalCapable worker : getOperationalCapables()) {
            worker.setLinePriority(linePriority);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Configuration getConfigurations() {
        List<Map<String, String>> configurations = new ArrayList<>();

        configurations.add(Map.of("Priority", String.valueOf(priority)));
        configurations.add(Map.of("State", state != null ? state.toString() : "Not set"));
        configurations.add(Map.of("Current product index", String.valueOf(currentProductIndex)));
        configurations.add(Map.of("Product series", productSeries != null ? productSeries.toString() : "Not set"));
        configurations.add(Map.of("Worker sequence", workerSequence != null ? workerSequence.toString() : "Not set"));
        configurations.add(Map.of("First worker", firstWorker != null ? firstWorker.toString() : "Not set"));
        configurations.add(Map.of("Last worker", lastWorker != null ? lastWorker.toString() : "Not set"));

        return new Configuration(configurations, this);
    }

    @Override
    public String getName() {
        return "Production line " + id;
    }

    /**
     * Method to get ConsumptionData from all devices in the production line to use it in the report.
     *
     * @return Set of ConsumptionData from all devices in the production line
     */
    public Set<ConsumptionData> getConsumptionData() {

        Set<ConsumptionData> consumptionDataSet = new HashSet<>();

        for (OperationalCapable worker : getOperationalCapables()) {
            if (worker instanceof Device) {
                ConsumptionData consumptionData = ((Device) worker).getConsumptionData();
                consumptionData.setProductionLine(this);
                consumptionDataSet.add(consumptionData);
            }
        }

        return consumptionDataSet;
    }
}
