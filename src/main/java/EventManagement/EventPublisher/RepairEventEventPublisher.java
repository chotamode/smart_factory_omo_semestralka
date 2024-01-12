package EventManagement.EventPublisher;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.Events.RepairEvent;

public interface RepairEventEventPublisher {
    default void publishEvent(RepairEvent event){
        getRepairEventChannel().publishEvent(event);
    }

    default void subscribeAsPublisher(RepairEventChannel eventChannel){
        eventChannel.subscribeAsPublisher(this);
    }

    RepairEventChannel getRepairEventChannel();
}
