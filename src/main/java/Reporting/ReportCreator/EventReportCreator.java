package Reporting.ReportCreator;

import EventManagement.Channels.EventChannel;
import Reporting.Report.EventGroup;
import Reporting.Report.EventReport;
import Reporting.Report.Report;
import Util.TimeAndReportManager;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Creates {@link EventReport}. EventReport contains information about events.
 * @see EventReport
 */
public class EventReportCreator extends ReportCreator {

    public void createReportForTime(Long time, EventGroup eventGroup) {
        createReportForTimePeriod(time, time, eventGroup);

    }

    /**
     * Creates report for time period sorted by event group BY_TYPE, BY_SOURCE, BY_HANDLER.
     */
    public void createReportForTimePeriod(Long timeFrom, Long timeTo, EventGroup eventGroup) {
        if (timeFrom < TimeAndReportManager.getInstance().getStartTime() ||
                timeTo < TimeAndReportManager.getInstance().getStartTime()) {
            throw new RuntimeException("Time cannot be less than start time.");
        } else if (timeTo > TimeAndReportManager.getInstance().getEndTime() ||
                timeFrom > TimeAndReportManager.getInstance().getEndTime()) {
            throw new RuntimeException("Time cannot be equal to end time.");
        }

        Report report = null;
        try {
            report = createReport(timeFrom, timeTo, eventGroup);
        } catch (FileNotFoundException e) {
            createReportsDirectoryIfNotExists();
        }

        assert report != null;
        report.printToFile();

    }

    @Override
    protected Report createReport(Long timeFrom, Long timeTo) throws FileNotFoundException {
        List<EventChannel> eventChannels = TimeAndReportManager.getInstance().getEventChannels();
        return new EventReport(timeFrom, timeTo, eventChannels, EventGroup.BY_TYPE);
    }

    protected Report createReport(Long timeFrom, Long timeTo, EventGroup eventGroup) throws FileNotFoundException {
        List<EventChannel> eventChannels = TimeAndReportManager.getInstance().getEventChannels();
        return new EventReport(timeFrom, timeTo, eventChannels, eventGroup);
    }
}
