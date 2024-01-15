package Reporting.ReportCreator;

import Production.ProductionEntity.Device.Resourse.ConsumptionData;
import Reporting.Report.ConsumptionReport;
import Reporting.Report.Report;
import Util.TimeAndReportManager;

import java.io.FileNotFoundException;
import java.util.Set;

/**
 * Creates {@link ConsumptionReport}. ConsumptionReport contains information about consumption.
 * @see ConsumptionReport
 */
public class ConsumptionReportCreator extends ReportCreator {
    @Override
    protected Report createReport(Long timeFrom, Long timeTo) throws FileNotFoundException {
        Set<ConsumptionData> consumptionData = TimeAndReportManager.getInstance().getConsumptionData();
        return new ConsumptionReport(timeFrom, timeTo, consumptionData);
    }
}
