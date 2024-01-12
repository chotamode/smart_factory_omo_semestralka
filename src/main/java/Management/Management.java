package Management;

import Factory.Factory;
import Product.Product;
import ProductionEntity.Device.Cobot;
import ProductionEntity.Device.Machine;
import ProductionEntity.Device.Resourse.DeviceResourceAbstract;
import ProductionEntity.Human.Repairman;
import ProductionEntity.Human.Worker;
import Factory.ProductionLine;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Management implements Visitor{

    private Logger logger = LogManager.getLogger(this.getClass());

    private List<Visitable> visitables = new ArrayList<Visitable>();
    private Factory factory;

    public Management(Factory factory) {
        this.factory = factory;
    }

    @Override
    public void executeInspection() {
        for(Visitable visitable : visitables) {
            visitable.accept(this);
        }
    }

    /**
     * Logs factory details
     * @param factory
     */
    @Override
    public void visit(Factory factory) {
        logger.info("Factory has " + factory.getStockMaterials().size() + " materials in stock");
        logger.info("Factory has " + factory.getProductionLines().size() + " production lines");
        logger.info("Factory has " + factory.getRepairmen().size() + " repairmen");
        logger.info("Factory has " + factory.getOperationalCapables().size() + " operational capables");
        logger.info("Factory has " + factory.getOrderedProductSeries().size() + " ordered product series");
        logger.info("Factory has " + factory.getProducedProductSeries().size() + " produced product series");
    }

    @Override
    public void visit(Machine machine) {
        logger.info("Machine " + machine.getClass().getSimpleName() + machine.getId() + " has oil level " + machine.getOil().getCurrent() + " and condition level " + machine.getCondition().getCurrent());
        logger.info("Machine " + machine.getClass().getSimpleName() + machine.getId() + " has consumed electricity " + machine.getConsumedElectricity());
        logger.info("Machine " + machine.getClass().getSimpleName() + machine.getId() + " has total maintenance cost " + machine.getTotalMaintenanceCost() + " and total paid maintenance cost " + machine.getTotalPaidMaintenanceCost());
        for(DeviceResourceAbstract deviceResourceAbstract : machine.getDeviceResources()) {
            logger.info("Machine " + machine.getClass().getSimpleName() + machine.getId() + " has " + deviceResourceAbstract.getClass().getSimpleName() + " level " + deviceResourceAbstract.getCurrent());
        }
    }

    @Override
    public void visit(Cobot cobot) {
        logger.info("Cobot " + cobot.getClass().getSimpleName() + cobot.getId() + " has oil level " + cobot.getOil().getCurrent() + " and condition level " + cobot.getCondition().getCurrent());
        logger.info("Cobot " + cobot.getClass().getSimpleName() + cobot.getId() + " has consumed electricity " + cobot.getConsumedElectricity());
        logger.info("Cobot " + cobot.getClass().getSimpleName() + cobot.getId() + " has total maintenance cost " + cobot.getTotalMaintenanceCost() + " and total paid maintenance cost " + cobot.getTotalPaidMaintenanceCost());
        for(DeviceResourceAbstract deviceResourceAbstract : cobot.getDeviceResources()) {
            logger.info("Cobot " + cobot.getClass().getSimpleName() + cobot.getId() + " has " + deviceResourceAbstract.getClass().getSimpleName() + " level " + deviceResourceAbstract.getCurrent());
        }
    }

    @Override
    public void visit(Worker worker) {
        logger.info("Worker " + worker.getClass().getSimpleName() + worker.getId() + " has total maintenance cost " + worker.getTotalMaintenanceCost() + " and total paid maintenance cost " + worker.getTotalPaidMaintenanceCost());
    }

    @Override
    public void visit(Repairman repairman) {
        logger.info("Repairman " + repairman.getClass().getSimpleName() + repairman.getId() + " done total maintenances " + repairman.getMaintenancesDone());
    }

    @Override
    public void visit(Product product) {
        logger.info("Product " + product.getName() + product.getSeriesIndex() + " has " + product.getOperations().size() + " operations");
        logger.info("Product " + product.getName() + product.getSeriesIndex() + " has " + product.getTotalCost() + " total cost");
    }

    @Override
    public void visit(ProductionLine productionLine) {
        logger.info("Production line " + productionLine.getId() + " has " + productionLine.getOperationalCapables().size() + " operational capables");
        logger.info("Production line " + productionLine.getId() + " has " + productionLine.getWorkers().size() + " workers");
        logger.info("Production line " + productionLine.getId() + " has " + productionLine.getState().getClass().getSimpleName() + " state");
        logger.info("Production line " + productionLine.getId() + " has " + productionLine.getPriority() + " priority");
        logger.info("Production line " + productionLine.getId() + " has " + productionLine.getWorkerSequence().size() + " worker sequence");
    }
}
