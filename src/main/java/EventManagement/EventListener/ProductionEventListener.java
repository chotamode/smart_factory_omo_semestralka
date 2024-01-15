package EventManagement.EventListener;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.Events.ProductionEvent;

public interface ProductionEventListener extends EventListener {
    void react(ProductionEvent event) throws Exception;

    /**
     * Subscribe to an event channel as a listener.
     *
     */
    default void subscribeAsListener(ProductionEventChannel eventChannel) {
        eventChannel.subscribeAsListener(this);
    }

}
