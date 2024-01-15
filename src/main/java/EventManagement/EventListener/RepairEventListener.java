package EventManagement.EventListener;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.Events.RepairEvent;
import Exceptions.RepairmanBusyException;

public interface RepairEventListener extends EventListener {
    void react(RepairEvent event) throws RepairmanBusyException;

    /**
     * Subscribe to an event channel as a listener.
     *
     */
    default void subscribeAsListener(RepairEventChannel eventChannel) {
        eventChannel.subscribeAsListener(this);
    }
}
