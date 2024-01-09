package EventManagement.EventPublisher;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.Channels.RepairEventChannel;
import EventManagement.Events.ProductionEvent;
import EventManagement.Events.RepairEvent;

public interface RepairEventPublisher {
    default void publishEvent(RepairEvent event){
        getRepairEventChannel().publishEvent(event);
    }

    default void subscribeAsPublisher(RepairEventChannel eventChannel){
        eventChannel.subscribeAsPublisher(this);
    }

    RepairEventChannel getRepairEventChannel();
}
