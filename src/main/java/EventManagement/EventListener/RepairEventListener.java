package EventManagement.EventListener;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.Events.RepairEvent;
import Exceptions.RepairmanBusyException;

public interface RepairEventListener {
    void react(RepairEvent event) throws RepairmanBusyException;

    /**
     * Subscribe to an event channel as a listener.
     * @param eventChannel
     */
    default void subscribeAsListener(RepairEventChannel eventChannel) {
        eventChannel.subscribeAsListener(this);
    }
}
