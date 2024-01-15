package Reporting.ReportCreator;

import Production.Configuration;
import Reporting.Report.ConfigurationReport;
import Reporting.Report.Report;
import Util.TimeAndReportManager;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Creates {@link ConfigurationReport}. ConfigurationReport contains information about configurations.
 * @see ConfigurationReport
 */
public class ConfigurationReportCreator extends ReportCreator {

    @Override
    protected Report createReport(Long timeFrom, Long timeTo) throws FileNotFoundException {
        List<Configuration> configurations = TimeAndReportManager.getInstance().getConfigurations(timeFrom, timeTo);
        return new ConfigurationReport(timeFrom, timeTo, configurations);
    }
}
