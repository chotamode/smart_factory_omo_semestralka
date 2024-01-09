package EventManagement.Channels;

import EventManagement.EventListener.ProductionEventListener;
import EventManagement.EventPublisher.ProductionEventPublisher;
import EventManagement.Events.ProductionEvent;
import Util.TimeAndReportManager;
import Util.TimeObserver;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class ProductionEventChannel implements TimeObserver {

    private List<ProductionEvent> events = new CopyOnWriteArrayList<>();
    private List<ProductionEvent> reactedEvents = new CopyOnWriteArrayList<>();
    private final List<ProductionEventListener> listeners = new CopyOnWriteArrayList<>();
    private List<ProductionEventPublisher> publishers = new CopyOnWriteArrayList<>();

    private Logger logger = LogManager.getLogger(ProductionEventChannel.class);

    public ProductionEventChannel() {
        subscribeToTimeAndReportManager();
    }

    public synchronized void publishEvent(ProductionEvent event) {
        events.add(event);
        logger.info("Event for " + event.getTarget().getClass().getSimpleName() + " and product " + event.getProduct().getName() + event.getProduct().getSeriesIndex() + " published at time: " + TimeAndReportManager.getInstance().getCurrentTime() );
    }

    public void subscribeAsListener(ProductionEventListener eventListener) {
        logger.info("Subscribed " + eventListener.getClass().getSimpleName() + " to production event channel");
        listeners.add(eventListener);
    }

    public void subscribeAsPublisher(ProductionEventPublisher eventPublisher) {
        logger.info("Subscribed " + eventPublisher.getClass().getSimpleName() + " to production event channel");
        publishers.add(eventPublisher);
    }

    @Override
    public boolean onTimeUpdate(Long time) {
        boolean actionPerformed = false;

//        logger.info("Production event channel is checking for events at time: " + time);
//        synchronized (events) {
            for (ProductionEventListener listener : listeners) {
                for (ProductionEvent event : events) {
                    if (event.getTarget() == listener
                            && event.getTimeStamp() < TimeAndReportManager.getInstance().getCurrentTime()
                    ) {

//                        logger.info(event.getTarget().getClass().getSimpleName() +
//                                " is reacted to event for Product series index " +
//                                event.getProduct().getSeriesIndex() +
//                                " at time: "
//                                + TimeAndReportManager.getInstance().getCurrentTime());

                        listener.react(event);
                        reactedEvents.add(event);
                        actionPerformed = true;
                    }
                }

            }
            events.removeAll(reactedEvents);
//        }
        return actionPerformed;
    }
}
