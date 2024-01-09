package Factory;

import Operation.OperationalCapable;
import Operation.WorkType.CobotWorkType;
import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import ProductionEntity.Device.Cobot;
import ProductionEntity.Device.Machine;
import ProductionEntity.Human.Worker;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElectronicsFactoryTest {

    @Test
    void acceptOrder() {
        ElectronicsFactory electronicsFactory = new ElectronicsFactory();
        electronicsFactory.addProductionLine(new ProductionLine());
        electronicsFactory.acceptOrder("Smartphone", 100);
        assertEquals(1, electronicsFactory.getOrderedProductSeries().size());
        assertEquals(100, electronicsFactory.getOrderedProductSeries().get(0).getProducts().size());
        assertEquals("Smartphone", electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getName());
        assertEquals(3, electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getOperations().size());
        assertEquals(5, electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getProductMaterials().size());
        assertEquals(3, electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getWorkerSequence().size());
    }

    @Test
    void acceptThreeOrders(){
        ElectronicsFactory electronicsFactory = new ElectronicsFactory();

        electronicsFactory.addProductionLine(new ProductionLine());
        electronicsFactory.addProductionLine(new ProductionLine());
        electronicsFactory.addProductionLine(new ProductionLine());

        electronicsFactory.setOperationalCapables(List.of(
                new Machine(MachineWorkType.MACHINE_PRESSING),
                new Machine(MachineWorkType.MACHINE_MOLDING),
                new Machine(MachineWorkType.MACHINE_CUTTING),
                new Machine(MachineWorkType.MACHINE_CUTTING),
                new Worker(HumanWorkType.HUMAN_MOLDING),
                new Worker(HumanWorkType.HUMAN_PRESSING),
                new Worker(HumanWorkType.HUMAN_CUTTING),
                new Worker(HumanWorkType.HUMAN_CUTTING),
                new Cobot(CobotWorkType.COBOT_MOLDING),
                new Cobot(CobotWorkType.COBOT_MOLDING),
                new Cobot(CobotWorkType.COBOT_PRESSING),
                new Cobot(CobotWorkType.COBOT_CUTTING)
        ));

        electronicsFactory.acceptOrder("Smartphone", 100);
        electronicsFactory.acceptOrder("Smartwatch", 100);
        electronicsFactory.acceptOrder("Laptop", 100);

        assertEquals(3, electronicsFactory.getOrderedProductSeries().size());

        assertEquals(100, electronicsFactory.getOrderedProductSeries().get(0).getProducts().size());
        assertEquals(100, electronicsFactory.getOrderedProductSeries().get(1).getProducts().size());
        assertEquals(100, electronicsFactory.getOrderedProductSeries().get(2).getProducts().size());

        assertEquals("Smartphone", electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getName());
        assertEquals("Smartwatch", electronicsFactory.getOrderedProductSeries().get(1).getProducts().get(0).getName());
        assertEquals("Laptop", electronicsFactory.getOrderedProductSeries().get(2).getProducts().get(0).getName());

        assertEquals(3, electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getOperations().size());
        assertEquals(5, electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getProductMaterials().size());
        assertEquals(3, electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getWorkerSequence().size());

        assertEquals(3, electronicsFactory.getOrderedProductSeries().get(1).getProducts().get(0).getOperations().size());
        assertEquals(3, electronicsFactory.getOrderedProductSeries().get(1).getProducts().get(0).getProductMaterials().size());
        assertEquals(3, electronicsFactory.getOrderedProductSeries().get(1).getProducts().get(0).getWorkerSequence().size());

        assertEquals(3, electronicsFactory.getOrderedProductSeries().get(2).getProducts().get(0).getOperations().size());
        assertEquals(7, electronicsFactory.getOrderedProductSeries().get(2).getProducts().get(0).getProductMaterials().size());
        assertEquals(3, electronicsFactory.getOrderedProductSeries().get(2).getProducts().get(0).getWorkerSequence().size());

        electronicsFactory.startProduction();

        for(ProductionLine productionLine : electronicsFactory.getProductionLines()) {
            for (OperationalCapable operationalCapable : productionLine.getOperationalCapables()) {
                for (ProductionLine productionLine2 : electronicsFactory.getProductionLines()) {
                    if (productionLine != productionLine2) {
                        for (OperationalCapable operationalCapable2 : productionLine2.getOperationalCapables()) {
                            assertNotEquals(operationalCapable, operationalCapable2);
                        }
                    }
                }
            }
        }
    }



}