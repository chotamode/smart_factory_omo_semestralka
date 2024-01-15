package Reporting.Report;

import EventManagement.Channels.EventChannel;
import EventManagement.EventListener.EventListener;
import EventManagement.EventPublisher.EventPublisher;
import EventManagement.Events.Event;
import EventManagement.Events.EventType;
import Util.Constants;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Creates EventReport. EventReport contains information about events.
 * It contains all events for given time period sorted by
 * <ul>
 *     <li>Events by type</li>
 *     <li>Events by source</li>
 *     <li>Events by handler</li>
 * </ul>
 *
 */
public class EventReport extends Report {

    private final List<EventChannel> eventChannels;
    private EventGroup eventGroup = EventGroup.BY_TYPE;

    public EventReport(Long timeFrom, Long timeTo, List<EventChannel> eventChannels, EventGroup eventGroup) throws FileNotFoundException {
        super(timeFrom, timeTo);
        this.eventChannels = eventChannels;
        this.eventGroup = eventGroup;
        printWriter = new PrintWriter(Constants.REPORTS_DIRECTORY_NAME +
                "/" + getClass().getSimpleName() + '_' + eventGroup + '_' +
                getTimeFrom() + '_' +
                getTimeTo() + ".txt");
    }

    public EventReport(Long timeFrom, Long timeTo, List<EventChannel> eventChannels) throws FileNotFoundException {
        super(timeFrom, timeTo);
        this.eventChannels = eventChannels;
        this.eventGroup = EventGroup.NONE;
    }

    @Override
    public void printToFile() {
        printWriter.println("Report for time period: " + getTimeFrom() + " - " + getTimeTo());
        printWriter.println("Report type: " + eventGroup);
        printReport();
        printWriter.close();
    }

    @Override
    public void printReport() {

        List<Event> events = new ArrayList<>();

        for (EventChannel eventChannel : eventChannels) {
            events.addAll(eventChannel.getEventsForReporting());
        }

        switch (eventGroup) {
            case BY_HANDLER:
                printEventsByHandler(events);
                break;
            case BY_TYPE:
                printEventsByType(events);
                break;
            case BY_SOURCE:
                printEventsBySource(events);
                break;
            case NONE:
                for (Event event : events) {
                    printEvent(event);
                }
                break;
        }

    }

    private void printEvent(Event event) {
        printWriter.println("");
        printWriter.println("Event at time: " + event.getTimeStamp());
        printWriter.println("Event type: " + event.getType());
        printWriter.println("Event source: " + event.getSource().getName());
        if (event.getTarget() == null) {
            printWriter.println("Event handler: " + "No handler");
        } else {
            printWriter.println("Event handler: " + event.getTarget().getName());
        }
    }

    private void printEventsBySource(List<Event> events) {

        Map<EventPublisher, List<Event>> eventsBySource = events.stream().collect(Collectors.groupingBy(Event::getSource));

        for (EventPublisher eventPublisher : eventsBySource.keySet()) {
            printWriter.println("");
            printWriter.println("Events for source: " + eventPublisher.getName());
            for (Event event : eventsBySource.get(eventPublisher)) {
                printEvent(event);
            }
        }
    }

    private void printEventsByType(List<Event> events) {

        Map<EventType, List<Event>> eventsByType = events.stream().collect(Collectors.groupingBy(Event::getType));

        for (EventType eventType : eventsByType.keySet()) {
            printWriter.println("");
            printWriter.println("Events for type: " + eventType);
            for (Event event : eventsByType.get(eventType)) {
                printEvent(event);
            }
        }

    }

    private void printEventsByHandler(List<Event> events) {

        Map<EventListener, List<Event>> eventsByHandler = new HashMap<>();

        for (Event event : events) {
            EventListener target = event.getTarget();
            if (!eventsByHandler.containsKey(target)) {
                eventsByHandler.put(target, new ArrayList<>());
            }
            eventsByHandler.get(target).add(event);
        }

        for (EventListener eventListener : eventsByHandler.keySet()) {
            printWriter.println("");
            if (eventListener == null) {
                printWriter.println("Events for handler: " + "No handler");
            } else {
                printWriter.println("Events for handler: " + eventListener.getName());
            }
            for (Event event : eventsByHandler.get(eventListener)) {
                printEvent(event);
            }
        }
    }
}
