package EventManagement;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.EventPublisher.EventPublisher;
import EventManagement.Events.EventType;
import EventManagement.Events.ProductionEvent;
import Production.ProductionEntity.Device.Cobot;
import Production.ProductionEntity.Device.Machine;
import Production.ProductionEntity.Human.Worker;

import Operation.WorkType.CobotWorkType;
import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import Product.Product;
import Util.TimeAndReportManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventPublisherListenerTest implements EventPublisher {

    final Worker worker = new Worker(HumanWorkType.HUMAN_MOLDING);
    final Cobot cobot = new Cobot(CobotWorkType.COBOT_MOLDING);
    final Machine machine = new Machine(MachineWorkType.MACHINE_MOLDING);

    final EventManagement.Channels.ProductionEventChannel productionEventChannel = new ProductionEventChannel();

    @BeforeEach
    public void setUp() {
        worker.subscribeToProductionEventChannel(productionEventChannel);
        cobot.subscribeToProductionEventChannel(productionEventChannel);
        machine.subscribeToProductionEventChannel(productionEventChannel);
        TimeAndReportManager.getInstance().start();
    }

    @Test
    void subscribeToProductionEventChannel() {
        assertEquals(productionEventChannel, worker.getProductionEventChannel());
        assertEquals(productionEventChannel, cobot.getProductionEventChannel());
        assertEquals(productionEventChannel, machine.getProductionEventChannel());

        assertEquals(productionEventChannel.getListeners().size(), 3);
        assertEquals(productionEventChannel.getPublishers().size(), 3);

        assertTrue(productionEventChannel.getListeners().contains(worker));
        assertTrue(productionEventChannel.getListeners().contains(cobot));
        assertTrue(productionEventChannel.getListeners().contains(machine));
    }


    @Test
    void publishEvent() {
        ProductionEvent productionEvent1 = new ProductionEvent("Test",
                EventType.OPERATION_DONE,
                new Product(),
                worker,
                this,
                TimeAndReportManager.getInstance().getCurrentTime());
        ProductionEvent productionEvent2 = new ProductionEvent("Test",
                EventType.OPERATION_DONE,
                new Product(),
                cobot,
                this,
                TimeAndReportManager.getInstance().getCurrentTime());
        ProductionEvent productionEvent3 = new ProductionEvent("Test",
                EventType.OPERATION_DONE,
                new Product(),
                machine,
                this,
                TimeAndReportManager.getInstance().getCurrentTime());
        worker.publishEvent(productionEvent1);
        assertEquals(1, productionEventChannel.getEvents().size());
        assertEquals(productionEvent1, productionEventChannel.getEvents().get(0));

        cobot.publishEvent(productionEvent2);
        assertEquals(2, productionEventChannel.getEvents().size());
        assertEquals(productionEvent2, productionEventChannel.getEvents().get(1));

        machine.publishEvent(productionEvent3);
        assertEquals(3, productionEventChannel.getEvents().size());
        assertEquals(productionEvent3, productionEventChannel.getEvents().get(2));
    }

    @Override
    public String getName() {
        return "Test";
    }
}