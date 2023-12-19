package EventManagement;

import java.util.ArrayList;
import java.util.List;

public class EventChannel {
    private List<Event> events = new ArrayList<>();
    private List<EventListener> listeners = new ArrayList<>();

    public void publishEvent (Event event) {
        events.add(event);
        for (EventListener listener : listeners) {
            listener.react(event);
        }
    }
}
