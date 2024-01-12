package ProductionEntity.Device;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.EventPublisher.RepairEventEventPublisher;
import EventManagement.Events.RepairEvent;
import Exceptions.ConditionException;
import Exceptions.OilException;
import Operation.WorkType.WorkType;
import Product.Product;
import ProductionEntity.Device.Resourse.Condition;
import ProductionEntity.Device.Resourse.DeviceResourceAbstract;
import ProductionEntity.Device.Resourse.Oil;
import ProductionEntity.ProductionEntity;
import EventManagement.Events.EventType;
import Util.TimeAndReportManager;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
public abstract class Device extends ProductionEntity implements RepairEventEventPublisher {

    private static int lastId = 0;
    private final int id = lastId++;

    private boolean needsMaintenance = false;

    private final Condition condition = new Condition(100, 100);
    private final Oil oil = new Oil(100, 100);

    private Long consumedElectricity = 0L;

    private RepairEventChannel repairEventChannel;

    public Device(WorkType workType) {
        super(workType);
    }

    Logger logger = LogManager.getLogger(Device.class);

    @Override
    public void workOnProduct(Product product) throws Exception {

        try {
            oil.spend();
            condition.spend();
//            logger.info("Device " + this.getClass().getSimpleName() + " has oil level " + oil.getCurrent() + " and condition level " + condition.getCurrent());
        } catch (OilException e) {
            if (needsMaintenance) {
                throw new RuntimeException("Device " + this.getClass().getSimpleName() + " needs maintenance");
            }
            publishEvent(new RepairEvent(e.getMessage(), this, EventType.NEEDS_OIL_REFILL, TimeAndReportManager.getInstance().getCurrentTime()));
            needsMaintenance = true;
            throw new OilException(e.getMessage());
        } catch (ConditionException e) {
            if (needsMaintenance) {
                throw new RuntimeException("Device " + this.getClass().getSimpleName() + " needs maintenance");
            }
            publishEvent(new RepairEvent(e.getMessage(), this, EventType.NEEDS_REPAIR, TimeAndReportManager.getInstance().getCurrentTime()));
            needsMaintenance = true;
            throw new ConditionException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e);
        }

        consumedElectricity++;
        super.workOnProduct(product);

    }

    @Override
    public void publishEvent(RepairEvent event) {
        event.setPriority(this.getLinePriority());
        RepairEventEventPublisher.super.publishEvent(event);
    }

    @Override
    public void subscribeAsPublisher(RepairEventChannel eventChannel) {
        RepairEventEventPublisher.super.subscribeAsPublisher(eventChannel);
        repairEventChannel = eventChannel;
    }

    public void repair() {
        needsMaintenance = false;
    }

    public DeviceResourceAbstract[] getDeviceResources() {
        return new DeviceResourceAbstract[]{oil, condition};
    }
}
