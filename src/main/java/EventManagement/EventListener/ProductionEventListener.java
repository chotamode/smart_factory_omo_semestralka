package EventManagement.EventListener;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.Events.Event;
import EventManagement.Events.ProductionEvent;

public interface ProductionEventListener{
    void react(ProductionEvent event) throws Exception;

    /**
     * Subscribe to an event channel as a listener.
     * @param eventChannel
     */
    default void subscribeAsListener(ProductionEventChannel eventChannel){
        eventChannel.subscribeAsListener(this);
    }

}
