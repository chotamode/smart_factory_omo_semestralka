package Operation;

import EventManagement.Channels.ProductionEventChannel;
import Production.ProductionEntity.Device.Cobot;
import Production.ProductionEntity.Device.Machine;
import Production.ProductionEntity.Human.Worker;
import Operation.WorkType.CobotWorkType;
import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import Product.Product;
import Product.ProductBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductionEntityTest {

    final ProductBuilder productBuilder = new ProductBuilder();
    final Product product = productBuilder.buildProductSmartphone();
    final Worker worker = new Worker(HumanWorkType.HUMAN_MOLDING);
    final Cobot cobot = new Cobot(CobotWorkType.COBOT_PRESSING);
    final Machine machine = new Machine(MachineWorkType.MACHINE_CUTTING);
    final ProductionEventChannel productionEventChannel = new ProductionEventChannel();

    @BeforeEach
    void setUp() {
        worker.setNextWorker(cobot);
        cobot.setNextWorker(machine);
        worker.subscribeToProductionEventChannel(productionEventChannel);
        cobot.subscribeToProductionEventChannel(productionEventChannel);
    }

    @Test
    void workOnProduct() throws Exception {
        worker.workOnProduct(product);
        assertTrue(product.getFirstOperation().isFinished());
    }
}