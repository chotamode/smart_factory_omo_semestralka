package Operation;

import ProductionEntity.Device.Cobot;
import ProductionEntity.Device.Machine;
import ProductionEntity.Human.Worker;
import Operation.WorkType.CobotWorkType;
import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import Product.Product;
import Product.ProductBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductionEntityTest {

    ProductBuilder productBuilder = new ProductBuilder();
    Product product = productBuilder.buildProductSmartphone();
    Worker worker = new Worker(HumanWorkType.HUMAN_MOLDING);
    Cobot cobot = new Cobot(CobotWorkType.COBOT_PRESSING);
    Machine machine = new Machine(MachineWorkType.MACHINE_CUTTING);

    @BeforeEach
    void setUp() {
        worker.setNextWorker(cobot);
        cobot.setNextWorker(machine);
    }

    @Test
    void workOnProduct() throws Exception {
        worker.workOnProduct(product);
        assertTrue(product.getCurrentOperation().isFinished());
        Operation operation = product.getFirstOperation();
        while(operation != null) {
            assertTrue(operation.isFinished());
            operation = operation.getNextOperation();
        }
    }
}