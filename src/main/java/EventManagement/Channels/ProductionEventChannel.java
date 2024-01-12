package EventManagement.Channels;

import EventManagement.EventListener.ProductionEventListener;
import EventManagement.EventPublisher.ProductionEventEventPublisher;
import EventManagement.Events.ProductionEvent;
import Util.TimeAndReportManager;
import Util.TimeObserver;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class ProductionEventChannel implements TimeObserver {

    private List<ProductionEvent> events = new CopyOnWriteArrayList<>();
    private List<ProductionEvent> reactedEvents = new CopyOnWriteArrayList<>();
    private final List<ProductionEventListener> listeners = new CopyOnWriteArrayList<>();
    private List<ProductionEventEventPublisher> publishers = new CopyOnWriteArrayList<>();

    private Logger logger = LogManager.getLogger(ProductionEventChannel.class);

    public ProductionEventChannel() {
        subscribeToTimeAndReportManager();
    }

    public void publishEvent(ProductionEvent event) {
        if(events.contains(event)){
            return;
        }
        events.add(event);
        logger.info(TimeAndReportManager.getInstance().getTimeInYMDH() + " Event for " + event.getTarget().getClass().getSimpleName() + " and product " + event.getProduct().getName() + event.getProduct().getSeriesIndex() + " published");
    }

    public void subscribeAsListener(ProductionEventListener eventListener) {
        logger.info("Subscribed " + eventListener.getClass().getSimpleName() + " to production event channel as listener");
        listeners.add(eventListener);
    }

    public void subscribeAsPublisher(ProductionEventEventPublisher eventPublisher) {
        logger.info("Subscribed " + eventPublisher.getClass().getSimpleName() + " to production event channel as publisher");
        publishers.add(eventPublisher);
    }

    @Override
    public boolean onTimeUpdate(Long time) {
        boolean actionPerformed = false;

        for (ProductionEventListener listener : listeners) {
            for (ProductionEvent event : events) {
                if (event.getTarget() == listener
                        && event.getTimeStamp() < TimeAndReportManager.getInstance().getCurrentTime()
                ) {
                    try {
                        listener.react(event);
                        reactedEvents.add(event);
                        actionPerformed = true;
                        break;
                    } catch (Exception e) {
                        if(!(e instanceof RuntimeException)){
                            logger.error(e.getMessage());
                        }
                        break;
                    }
                }
            }

        }
        events.removeAll(reactedEvents);
//        }
        return actionPerformed;
    }

}
