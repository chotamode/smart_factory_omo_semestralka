package Production.ProductionEntity.Device;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.EventPublisher.RepairEventPublisher;
import EventManagement.Events.EventType;
import EventManagement.Events.RepairEvent;
import Exceptions.ConditionException;
import Exceptions.OilException;
import Operation.WorkType.WorkType;
import Product.Product;
import Production.Configuration;
import Production.ProductionEntity.Device.Resourse.Condition;
import Production.ProductionEntity.Device.Resourse.ConsumptionData;
import Production.ProductionEntity.Device.Resourse.DeviceResourceAbstract;
import Production.ProductionEntity.Device.Resourse.Oil;
import Production.ProductionEntity.ProductionEntity;
import Util.Constants;
import Util.TimeAndReportManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

@Getter
public abstract class Device extends ProductionEntity implements RepairEventPublisher {

    private static int lastId = 0;
    private final int id = lastId++;
    private final Condition condition = new Condition(100, 100);
    private final Oil oil = new Oil(100, 100);
    private final ConsumptionData consumptionData = new ConsumptionData(this, TimeAndReportManager.getInstance().getCurrentTime());
    Logger logger = LogManager.getLogger(Device.class);
    private boolean needsMaintenance = false;
    private Long consumedElectricity;
    private Long electricityConsumption = Constants.DEVICE_STARTING_ELECTRICITY_CONSUMPTION;
    private RepairEventChannel repairEventChannel;

    public Device(@JsonProperty("workType") WorkType workType) {
        super(workType);
        consumedElectricity = 0L;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName() + this.getId();
    }

    @Override
    public void workOnProduct(Product product) throws Exception {
        spendResource(condition, ConditionException.class, EventType.NEEDS_REPAIR);
        spendResource(oil, OilException.class, EventType.NEEDS_OIL_REFILL);

        consumedElectricity += electricityConsumption;

        consumptionData.addConsumedOil(oil.getConsumption());
        consumptionData.addConsumedElectricity(electricityConsumption);
        consumptionData.setTimeStamp(TimeAndReportManager.getInstance().getCurrentTime());

        super.workOnProduct(product);
    }

    private <T extends Exception> void spendResource(DeviceResourceAbstract resource, Class<T> exceptionClass, EventType eventType) throws T {
        try {
            resource.spend();
        } catch (Exception e) {
            if (needsMaintenance) {
                throw new RuntimeException("Device " + this.getClass().getSimpleName() + " needs maintenance");
            }
            if (exceptionClass.isInstance(e)) {
                publishEvent(new RepairEvent(e.getMessage(), this, eventType, TimeAndReportManager.getInstance().getCurrentTime()));
                needsMaintenance = true;
                throw exceptionClass.cast(e);
            }
        }
    }

    @Override
    public void publishEvent(RepairEvent event) {
        event.setPriority(this.getLinePriority());
        RepairEventPublisher.super.publishEvent(event);
    }

    @Override
    public void subscribeAsPublisher(RepairEventChannel eventChannel) {
        RepairEventPublisher.super.subscribeAsPublisher(eventChannel);
        repairEventChannel = eventChannel;
    }

    @Override
    public Configuration getConfigurations() {
        List<Map<String, String>> configurations = super.getConfigurations().getConfigurationsList();

        configurations.add(Map.of("Electricity consumption", String.valueOf(electricityConsumption)));
        configurations.add(Map.of("Oil consumption", String.valueOf(oil.getConsumption())));
        configurations.add(Map.of("Condition consumption", String.valueOf(condition.getConsumption())));
        configurations.add(Map.of("Max oil", String.valueOf(oil.getMax())));
        configurations.add(Map.of("Max condition", String.valueOf(condition.getMax())));

        return new Configuration(configurations, this);
    }

    public ConsumptionData getConsumptionData() {
        return new ConsumptionData(consumptionData);
    }

    public void repair() {
        needsMaintenance = false;
        electricityConsumption++;
    }

    public DeviceResourceAbstract[] getDeviceResources() {
        return new DeviceResourceAbstract[]{oil, condition};
    }

}
