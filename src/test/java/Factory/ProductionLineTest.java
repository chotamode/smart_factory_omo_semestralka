package Factory;

import EventManagement.Channels.ProductionEventChannel;
import Operation.Operation;
import Operation.WorkType.CobotWorkType;
import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import Operation.WorkType.WorkType;
import Product.ProductBuilder;
import Product.ProductSeries;
import Product.Product;
import ProductionEntity.Device.Cobot;
import ProductionEntity.Device.Machine;
import ProductionEntity.Human.Worker;
import Util.TimeAndReportManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductionLineTest {

    ProductionLine productionLine = new ProductionLine();
    ProductBuilder productBuilder = new ProductBuilder();

    ProductSeries productSeries1 = new ProductSeries("Smartphone");
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
                productionLine.addWorker(new Worker((HumanWorkType) workType));
            } else if(workType instanceof MachineWorkType) {
                productionLine.addWorker(new Machine((MachineWorkType) workType));
            } else if(workType instanceof CobotWorkType) {
                productionLine.addWorker(new Cobot((CobotWorkType) workType));
            }
        }

    }

    @Test
    void work() {
        productionLine.work();
        assertEquals(1, productionLine.getCurrentProductIndex());
    }

    @Test
    public void workUntilFinished() throws InterruptedException {

        TimeAndReportManager.getInstance().start();

        productionLine.workUntilFinished();

        while(!productionLine.getProductSeries().isFinished()) {
            Thread.sleep(100);
        }

        assertEquals(100, productionLine.getCurrentProductIndex());
        assertTrue(productionLine.getProductSeries().isFinished());
        assertEquals(100, productionLine.getProductSeries().getProducts().size());
        for(Product p : productionLine.getProductSeries().getProducts()) {
            assertTrue(p.isFinished());
            for(Operation o : p.getOperations()) {
                assertTrue(o.isFinished());
            }
        }
    }

    @AfterEach
    public void tearDown() {
        TimeAndReportManager.getInstance().stop();
    }

}