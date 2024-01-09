package EventManagement.EventListener;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.Events.RepairEvent;

public interface RepairEventListener {
    void react(RepairEvent event);

    /**
     * Subscribe to an event channel as a listener.
     * @param eventChannel
     */
    default void subscribeAsListener(RepairEventChannel eventChannel) {
        eventChannel.subscribeAsListener(this);
    }
}
