package EventManagement;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.Events.EventType;
import EventManagement.Events.ProductionEvent;
import ProductionEntity.Device.Cobot;
import ProductionEntity.Device.Machine;
import ProductionEntity.Human.Worker;

import Operation.WorkType.CobotWorkType;
import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import Product.Product;
import Util.TimeAndReportManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventPublisherListenerTest {

    Worker worker = new Worker(HumanWorkType.HUMAN_MOLDING);
    Cobot cobot = new Cobot(CobotWorkType.COBOT_MOLDING);
    Machine machine = new Machine(MachineWorkType.MACHINE_MOLDING);

    EventManagement.Channels.ProductionEventChannel ProductionEventChannel = new ProductionEventChannel();

    @BeforeEach
    public void setUp() {
        worker.subscribeToProductionEventChannel(ProductionEventChannel);
        cobot.subscribeToProductionEventChannel(ProductionEventChannel);
        machine.subscribeToProductionEventChannel(ProductionEventChannel);
    }

    @Test
    void subscribeToProductionEventChannel() {
        assertEquals(ProductionEventChannel, worker.getProductionEventChannel());
        assertEquals(ProductionEventChannel, cobot.getProductionEventChannel());
        assertEquals(ProductionEventChannel, machine.getProductionEventChannel());

        assertEquals(ProductionEventChannel.getListeners().size(), 3);
        assertEquals(ProductionEventChannel.getPublishers().size(), 3);

        assertTrue(ProductionEventChannel.getListeners().contains(worker));
        assertTrue(ProductionEventChannel.getListeners().contains(cobot));
        assertTrue(ProductionEventChannel.getListeners().contains(machine));
    }


    @Test
    void publishEvent() {
        ProductionEvent productionEvent = new ProductionEvent("Test",
                EventType.OPERATION_DONE,
                new Product(),
                worker,
                this,
                TimeAndReportManager.getInstance().getCurrentTime());
        worker.publishEvent(productionEvent);
        assertEquals(1, ProductionEventChannel.getEvents().size());
        assertEquals(productionEvent, ProductionEventChannel.getEvents().get(0));

        cobot.publishEvent(productionEvent);
        assertEquals(2, ProductionEventChannel.getEvents().size());
        assertEquals(productionEvent, ProductionEventChannel.getEvents().get(1));

        machine.publishEvent(productionEvent);
        assertEquals(3, ProductionEventChannel.getEvents().size());
        assertEquals(productionEvent, ProductionEventChannel.getEvents().get(2));
    }

}