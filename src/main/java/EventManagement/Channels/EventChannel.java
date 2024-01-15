package EventManagement.Channels;

import EventManagement.Events.Event;

import java.util.List;

public interface EventChannel {

    List<Event> getEventsForReporting();

    void unsubscribeAll();

}
