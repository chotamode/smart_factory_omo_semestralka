package Human;

import EventManagement.Event;
import EventManagement.EventChannel;
import EventManagement.EventListener;
import EventManagement.EventPublisher;

import java.util.ArrayList;
import java.util.List;

public abstract class Human implements EventListener, EventPublisher {

    private final List<EventChannel> eventChannels = new ArrayList<>();
    @Override
    public abstract void react(Event event);

    @Override
    public abstract void publishEvent(Event event);

}
