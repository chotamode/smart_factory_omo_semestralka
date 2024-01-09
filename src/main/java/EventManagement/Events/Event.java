package EventManagement.Events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public abstract class Event {
    private String name;
    private EventType type;
    private Long timeStamp;


    public Event(String name, EventType type, Long timeStamp) {
        this.name = name;
        this.type = type;
        this.timeStamp = timeStamp;
    }
}
