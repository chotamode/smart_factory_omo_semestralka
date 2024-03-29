package EventManagement.EventPublisher;

import EventManagement.Channels.ProductionEventChannel;
import EventManagement.Events.ProductionEvent;

public interface ProductionEventPublisher extends EventPublisher {
    default void publishEvent(ProductionEvent event) {
        getProductionEventChannel().publishEvent(event);
    }

    default void subscribeAsPublisher(ProductionEventChannel eventChannel) {
        eventChannel.subscribeAsPublisher(this);
    }

    ProductionEventChannel getProductionEventChannel();
}
