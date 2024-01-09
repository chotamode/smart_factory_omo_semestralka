package EventManagement.Channels;

import EventManagement.EventListener.ProductionEventListener;
import EventManagement.EventListener.RepairEventListener;
import EventManagement.EventPublisher.ProductionEventPublisher;
import EventManagement.EventPublisher.RepairEventPublisher;
import EventManagement.Events.RepairEvent;
import Util.TimeObserver;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Getter
public class RepairEventChannel implements TimeObserver {

    private List<RepairEvent> events = new CopyOnWriteArrayList<>();
    private List<RepairEvent> reactedEvents = new CopyOnWriteArrayList<>();
    private List<RepairEventListener> listeners = new CopyOnWriteArrayList<>();
    private List<RepairEventPublisher> publishers = new CopyOnWriteArrayList<>();

    Logger logger = LogManager.getLogger(RepairEventChannel.class);

    public RepairEventChannel() {
        subscribeToTimeAndReportManager();
    }

    public void publishEvent(RepairEvent event) {
        events.add(event);
        logger.info("Event " + event.getType() + " for " + event.getDevice().getClass().getSimpleName() + " published at time: " + event.getTimeStamp());
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

        for(RepairEventListener listener : listeners){
            for(RepairEvent event : events){
                if(event.getRepairman() == null
                        && event.getTimeStamp() < time
                ){
                    listener.react(event);
                    reactedEvents.add(event);
                    actionPerformed = true;
                }
                if(event.getRepairman() == listener
                ){
                    listener.react(event);
                    reactedEvents.add(event);
                    actionPerformed = true;
                }
            }
        }

        events.removeAll(reactedEvents);
        return actionPerformed;
    }
}
