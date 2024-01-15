package Reporting.ReportCreator;

import EventManagement.Channels.RepairEventChannel;
import Reporting.Report.OutagesReport;
import Reporting.Report.Report;
import Util.TimeAndReportManager;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Creates {@link OutagesReport}. OutagesReport contains information about outages.
 * @see OutagesReport
 */
public class OutagesReportCreator extends ReportCreator {
    @Override
    protected Report createReport(Long timeFrom, Long timeTo) throws FileNotFoundException {
        List<RepairEventChannel> repairEventChannels = TimeAndReportManager.getInstance().getRepairEventChannels();
        return new OutagesReport(timeFrom, timeTo, repairEventChannels);
    }
}
