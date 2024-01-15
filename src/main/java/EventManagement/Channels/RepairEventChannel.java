package EventManagement.Channels;

import EventManagement.EventListener.RepairEventListener;
import EventManagement.EventPublisher.RepairEventPublisher;
import EventManagement.Events.Event;
import EventManagement.Events.RepairEvent;
import Exceptions.RepairmanBusyException;
import Util.TimeAndReportManager;
import Util.TimeObserver;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Getter
public class RepairEventChannel implements TimeObserver, EventChannel {

    private final List<RepairEvent> events = new CopyOnWriteArrayList<>();
    private final List<RepairEvent> reactedEvents = new CopyOnWriteArrayList<>();
    private final List<RepairEventListener> listeners = new CopyOnWriteArrayList<>();
    private final List<RepairEventPublisher> publishers = new CopyOnWriteArrayList<>();

    Logger logger = LogManager.getLogger(RepairEventChannel.class);

    public RepairEventChannel() {
        subscribeToTimeAndReportManager(this);
    }

    public void publishEvent(RepairEvent event) {
        int i = 0;
        for (; i < events.size(); i++) {
            RepairEvent existingEvent = events.get(i);
            if (existingEvent.getPriority() < event.getPriority() ||
                    (existingEvent.getPriority() == event.getPriority()
                            && existingEvent.getTimeStamp() > event.getTimeStamp())) {
                break;
            }
        }
        events.add(i, event);
        logger.info(TimeAndReportManager.getInstance().getTimeInYMDH() + " Event " + event.getType() + " for " + event.getDevice().getClass().getSimpleName() + event.getDevice().getId() + " published");
    }

    public void subscribeAsListener(RepairEventListener eventListener) {
        listeners.add(eventListener);
    }

    public void subscribeAsPublisher(RepairEventPublisher eventPublisher) {
        publishers.add(eventPublisher);
    }

    @Override
    public boolean onTimeUpdate(Long time) {
        boolean actionPerformed = false;

        for (RepairEventListener listener : listeners) {
            for (RepairEvent event : events) {
                try {
                    if (event.getRepairman() == listener || event.getRepairman() == null
                            && event.getTimeStamp() <= time
                    ) {
                        listener.react(event);
                        reactedEvents.add(event);
                        actionPerformed = true;
                    }
                } catch (Exception e) {
                    if (!(e instanceof RepairmanBusyException)) {
                        logger.error(e.getMessage());
                    } else {
                        actionPerformed = true;
                    }
                }
            }
        }

        events.removeAll(reactedEvents);
        return actionPerformed;
    }

    @Override
    public List<Event> getEventsForReporting() {
        List<Event> events = new CopyOnWriteArrayList<>(this.reactedEvents);
        events.addAll(this.events);
        return events;
    }

    @Override
    public void unsubscribeAll() {
        listeners.clear();
        publishers.clear();
    }

}
