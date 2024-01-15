package Production.ProductionEntity.Device.Resourse;

import Production.Factory.Factory;
import Production.ProductionEntity.Device.Device;
import Production.ProductionLine.ProductionLine;
import Util.TimeAndReportManager;
import lombok.Getter;
import lombok.Setter;

/**
 * ConsumptionData is used for creating consumption reports.
 * It is used for creating consumption reports and string consumption data.
 */
@Getter
@Setter
public class ConsumptionData {

    private Device consumer;
    private int consumedOil = 0;
    private Long consumedElectricity = 0L;

    private ProductionLine productionLine;
    private Factory factory;

    private Long timeStamp;

    public ConsumptionData(Device consumer, Long timeStamp) {
        this.consumer = consumer;
        this.timeStamp = timeStamp;
    }

    public ConsumptionData(ConsumptionData consumptionData) {
        this.consumer = consumptionData.consumer;
        this.consumedOil = consumptionData.consumedOil;
        this.consumedElectricity = consumptionData.consumedElectricity;
        this.timeStamp = TimeAndReportManager.getInstance().getCurrentTime();
        this.productionLine = consumptionData.productionLine;
        this.factory = consumptionData.factory;
    }

    public void addConsumedOil(int amount) {
        consumedOil += amount;
    }

    public void addConsumedElectricity(Long amount) {
        consumedElectricity += amount;
    }
}
