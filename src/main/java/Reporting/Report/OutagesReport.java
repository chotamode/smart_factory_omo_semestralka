package Reporting.Report;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.Events.EventType;
import EventManagement.Events.RepairEvent;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Creates OutagesReport. OutagesReport contains information about outages.
 * It contains information about:
 * <ul>
 *     <li>Longest outage</li>
 *     <li>Shortest outage</li>
 *     <li>Average outage</li>
 *     <li>Average waiting time</li>
 *     <li>Types of outage sources sorted by outage length</li>
 * </ul>
 *
 */
public class OutagesReport extends Report {

    final List<RepairEvent> repairEvents = new ArrayList<>();
    final List<RepairEvent> outageRepairEvents = new ArrayList<>();
    List<RepairEventChannel> repairEventChannels;

    public OutagesReport(Long timeFrom, Long timeTo, List<RepairEventChannel> repairEventChannels) throws FileNotFoundException {
        super(timeFrom, timeTo);
        this.repairEventChannels = repairEventChannels;
        for (RepairEventChannel repairEventChannel : repairEventChannels) {
            repairEvents.addAll(repairEventChannel.getEvents());
            repairEvents.addAll(repairEventChannel.getReactedEvents());
        }

        outageRepairEvents.addAll(
                repairEvents.stream()
                        .filter(repairEvent -> repairEvent.getType().equals(EventType.NEEDS_REPAIR)
                                || repairEvent.getType().equals(EventType.NEEDS_OIL_REFILL))
                        .toList()
        );
    }

    private void printRepairEvent(RepairEvent repairEvent) {
        printWriter.println("");
        printWriter.println("Repair event for device: " + repairEvent.getDevice().getName());
        printWriter.println("Type: " + repairEvent.getType());
        printWriter.println("Outage start time: " + repairEvent.getTimeStamp());
        printWriter.println("Repaired by: " + repairEvent.getRepairman().getName());
        printWriter.println("Repair start time: " + repairEvent.getRepairStartTime());
        printWriter.println("Repair end time: " + repairEvent.getRepairEndTime());
        printWriter.println("Repair waiting time: " + repairEvent.getWaitTime());
    }

    @Override
    public void printReport() {

        Optional<RepairEvent> longestOutage = getLongestOutage();
        Optional<RepairEvent> shortestOutage = getShortestOutage();
        Optional<Long> averageOutageTime = getAverageOutageTime();
        Optional<Long> averageWaitingTime = getAverageWaitingTime();

        printWriter.println("");
        if (longestOutage.isPresent()) {
            printWriter.print("Longest outage: ");
            printRepairEvent(longestOutage.get());
        } else {
            printWriter.println("Error getting longest outage");
        }
        if (shortestOutage.isPresent()) {
            printWriter.println("");
            printWriter.print("Shortest outage: ");
            printRepairEvent(shortestOutage.get());
        } else {
            printWriter.println("Error getting shortest outage");
        }
        printWriter.println("");
        printWriter.println("Average outage: " + (averageOutageTime.isPresent() ? averageOutageTime.get() : "Error getting average outage"));
        printWriter.println("Average waiting time: " + (averageWaitingTime.isPresent() ? averageWaitingTime.get() : "Error getting average waiting time"));
        printWriter.println("");
        printWriter.println("Types of outage sources sorted by outage length:");
        for (RepairEvent repairEvent : getOutageSourcesSortedByOutageLength()) {
            printRepairEvent(repairEvent);
        }
    }

    private List<RepairEvent> getOutageSourcesSortedByOutageLength() {
        return outageRepairEvents.stream()
                .sorted(Comparator.comparingLong(RepairEvent::getOutageTime).reversed())
                .collect(Collectors.toList());
    }

    private Optional<Long> getAverageWaitingTime() {
        return outageRepairEvents.stream()
                .map(RepairEvent::getWaitTime)
                .reduce(Long::sum)
                .map(sum -> sum / outageRepairEvents.size());
    }

    private Optional<Long> getAverageOutageTime() {
        return outageRepairEvents.stream()
                .map(RepairEvent::getOutageTime)
                .reduce(Long::sum)
                .map(sum -> sum / repairEvents.size());
    }

    private Optional<RepairEvent> getShortestOutage() {
        return outageRepairEvents.stream()
                .min(Comparator.comparingLong(RepairEvent::getOutageTime));
    }

    private Optional<RepairEvent> getLongestOutage() {
        return outageRepairEvents.stream()
                .max(Comparator.comparingLong(RepairEvent::getOutageTime));
    }
}

