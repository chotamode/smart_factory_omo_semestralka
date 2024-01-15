package EventManagement.Events;

import EventManagement.EventListener.EventListener;
import EventManagement.EventPublisher.EventPublisher;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public abstract class Event {
    protected EventPublisher source;
    protected EventListener target;
    private String name;
    private EventType type;
    private Long timeStamp;

    public Event(String name, EventType type, Long timeStamp, EventPublisher source, EventListener target) {
        this.name = name;
        this.type = type;
        this.timeStamp = timeStamp;
        this.source = source;
        this.target = target;
    }
}
