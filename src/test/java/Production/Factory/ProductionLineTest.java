package Production.Factory;

import EventManagement.Channels.ProductionEventChannel;
import Operation.WorkType.CobotWorkType;
import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import Operation.WorkType.WorkType;
import Product.ProductBuilder;
import Product.ProductSeries;
import Product.Product;
import Production.ProductionLine.ProductionLine;
import Production.ProductionEntity.Device.Cobot;
import Production.ProductionEntity.Device.Machine;
import Production.ProductionEntity.Human.Worker;
import Util.TimeAndReportManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductionLineTest {

    final ProductionLine productionLine = new ProductionLine();
    final ProductBuilder productBuilder = new ProductBuilder();

    final ProductSeries productSeries1 = new ProductSeries("Smartphone");
    ProductionEventChannel productionEventChannel = new ProductionEventChannel();

    @BeforeEach
    public void setUp() {

        Product product;
        for (int i = 0; i < 100; i++) {
            product = productBuilder.buildProductSmartphone();
            product.setSeriesIndex(i);
            productSeries1.addProduct(product);
        }

        productionLine.assembleProductionLine(productSeries1);

        for(WorkType workType : productionLine.getWorkerSequence()) {
            if(workType instanceof HumanWorkType) {
                productionLine.addWorker(new Worker(workType));
            } else if(workType instanceof MachineWorkType) {
                productionLine.addWorker(new Machine(workType));
            } else if(workType instanceof CobotWorkType) {
                productionLine.addWorker(new Cobot(workType));
            }
        }

        TimeAndReportManager.getInstance().start();

    }

    @Test
    void work() {
        productionLine.work();
        assertEquals(1, productionLine.getCurrentProductIndex());
    }

    @AfterEach
    public void tearDown() {
        TimeAndReportManager.getInstance().stop();
    }

}