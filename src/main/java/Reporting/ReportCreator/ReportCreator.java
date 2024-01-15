package Reporting.ReportCreator;

import Reporting.Report.Report;
import Util.Constants;
import Util.TimeAndReportManager;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Abstract class for report creators. Implementing Factory method design pattern.
 */
public abstract class ReportCreator {

    public ReportCreator() {
    }

    /**
     * Creates report for one tick.
     */
    public void createReportForTime(Long time) {
        createReportForTimePeriod(time, time);
    }

    /**
     * Creates report for time period.
     */
    public void createReportForTimePeriod(Long timeFrom, Long timeTo) {

        Report report = null;

        if (timeFrom < TimeAndReportManager.getInstance().getStartTime() ||
                timeTo < TimeAndReportManager.getInstance().getStartTime()) {
            throw new IllegalArgumentException("TimeTo cannot be less than start time.");
        } else if (timeTo > TimeAndReportManager.getInstance().getEndTime() ||
                timeFrom > TimeAndReportManager.getInstance().getEndTime()) {
            throw new IllegalArgumentException("TimeFrom cannot be greater than end time.");
        }

        try {
            report = createReport(timeFrom, timeTo);
        } catch (FileNotFoundException e) {
            createReportsDirectoryIfNotExists();
        }

        assert report != null;
        report.printToFile();
    }

    protected abstract Report createReport(Long timeFrom, Long timeTo) throws FileNotFoundException;

    /**
     * Creates directory for reports if it does not exist.
     */
    protected void createReportsDirectoryIfNotExists() {
        File reportsDirectory = new File(Constants.REPORTS_DIRECTORY_NAME);
        if (!reportsDirectory.exists()) {
            boolean created = reportsDirectory.mkdir();
            if (!created) {
                throw new RuntimeException("Directory for reports is missing and could not be created.");
            }
        }
    }

}
