package EventManagement.Channels;

import EventManagement.Events.EventType;
import EventManagement.Events.RepairEvent;
import Operation.WorkType.MachineWorkType;
import Production.ProductionEntity.Device.Machine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepairEventChannelTest {

    final RepairEventChannel repairEventChannel = new RepairEventChannel();

    /**
     * Test if event publishing so earlier is the elements with the highest priority and oldest timestamp
     */
    @Test
    void publishEvent1() {
        Machine machine = new Machine(MachineWorkType.MACHINE_MOLDING);
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 1L, 1));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 2L, 3));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 3L, 1));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 4L, 2));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 5L, 2));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 6L, 1));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 7L, 2));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 8L, 1));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 9L, 4));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 10L, 1));
        for(int i = 0; i < repairEventChannel.getEvents().size() - 1; i++) {
            RepairEvent event = repairEventChannel.getEvents().get(i);
            RepairEvent nextEvent = repairEventChannel.getEvents().get(i+1);
            assertTrue(
                    event.getPriority() > nextEvent.getPriority() ||
                            (event.getPriority() == nextEvent.getPriority() && event.getTimeStamp() <= nextEvent.getTimeStamp()));
        }
    }

    /**
     * Test if event publishing so earlier is the elements with the highest priority and oldest timestamp
     */
    @Test
    void publishEvent2(){
        Machine machine = new Machine(MachineWorkType.MACHINE_MOLDING);
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 2L, 1));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 4L, 3));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 3L, 1));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 5L, 2));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 9L, 2));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 6L, 1));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 9L, 2));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 10L, 1));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 9L, 4));
        repairEventChannel.publishEvent(new RepairEvent("test", machine, EventType.NEEDS_REPAIR, 10L, 1));
        for(int i = 0; i < repairEventChannel.getEvents().size() - 1; i++) {
            RepairEvent event = repairEventChannel.getEvents().get(i);
            RepairEvent nextEvent = repairEventChannel.getEvents().get(i+1);
            assertTrue(
                    event.getPriority() > nextEvent.getPriority() ||
                            (event.getPriority() == nextEvent.getPriority() && event.getTimeStamp() <= nextEvent.getTimeStamp()));
        }
    }
}