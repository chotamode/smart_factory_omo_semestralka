package EventManagement.Channels;

import EventManagement.EventListener.ProductionEventListener;
import EventManagement.EventListener.RepairEventListener;
import EventManagement.EventPublisher.ProductionEventPublisher;
import EventManagement.EventPublisher.RepairEventPublisher;
import EventManagement.Events.RepairEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RepairEventChannel{

    private List<RepairEvent> events = new ArrayList<>();
    private List<RepairEventListener> listeners = new ArrayList<>();
    private List<RepairEventPublisher> publishers = new ArrayList<>();

    public void publishEvent(RepairEvent event) {
        events.add(event);
        for (RepairEventListener listener : listeners) {
            listener.react(event);
        }
    }

    public void subscribeAsListener(RepairEventListener eventListener) {
        listeners.add(eventListener);
    }

    public void subscribeAsPublisher(RepairEventPublisher eventPublisher) {
        publishers.add(eventPublisher);
    }

}
