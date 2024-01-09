package EventManagement.Events;

import lombok.Getter;

@Getter
public class RepairEvent extends Event{
    public RepairEvent(String name, EventType type, Long timeStamp) {
        super(name, type, timeStamp);
    }
}
