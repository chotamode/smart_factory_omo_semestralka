package Reporting.Report;

import Util.Constants;
import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Abstract class for all reports. Contains time period for which report is created. And write all information to file.
 */
@Getter
public abstract class Report {

    private final Long timeFrom;
    private final Long timeTo;

    PrintWriter printWriter;

    public Report(Long timeFrom, Long timeTo) throws FileNotFoundException {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        printWriter = new PrintWriter(Constants.REPORTS_DIRECTORY_NAME +
                "/" + getClass().getSimpleName() + '_' +
                getTimeFrom() + '_' +
                getTimeTo() + ".txt");
    }

    public void printToFile() {
        printWriter.println("Report for time period: " + getTimeFrom() + " - " + getTimeTo());
        printReport();
        printWriter.close();
    }

    public abstract void printReport();

}
