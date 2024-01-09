package ProductionEntity.Device;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.EventPublisher.RepairEventPublisher;
import EventManagement.Events.RepairEvent;
import Exceptions.DeviceResource.ConditionException;
import Exceptions.DeviceResource.OilException;
import Operation.WorkType.WorkType;
import Product.Product;
import ProductionEntity.Device.Resourse.Condition;
import ProductionEntity.Device.Resourse.Oil;
import ProductionEntity.ProductionEntity;
import EventManagement.Events.EventType;
import Util.TimeAndReportManager;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
public abstract class Device extends ProductionEntity implements RepairEventPublisher {

    private final Condition condition = new Condition(100, 100);
    private final Oil oil = new Oil(100, 100);

    private RepairEventChannel repairEventChannel;

    public Device(WorkType workType) {
        super(workType);
    }

    Logger logger = LogManager.getLogger(Device.class);

    @Override
    public void workOnProduct(Product product) throws Exception {
        if(isFunctional()){
            try {
                oil.spend(1);
                condition.spend(1);
                logger.info("Device " + this.getClass().getSimpleName() + " has oil level " + oil.getCurrent() + " and condition level " + condition.getCurrent());
            } catch (OilException e) {
                publishEvent(new RepairEvent(e.getMessage(), this, EventType.NEEDS_OIL_REFILL, TimeAndReportManager.getInstance().getCurrentTime()));
                throw new OilException(e.getMessage());
            } catch (ConditionException e) {
                publishEvent(new RepairEvent(e.getMessage(), this, EventType.NEEDS_REPAIR, TimeAndReportManager.getInstance().getCurrentTime()));
                throw new ConditionException(e.getMessage());
            } catch (Exception e) {
                throw new Exception(e);}
            super.workOnProduct(product);
        }
    }

    public boolean isFunctional() {
        return oil.getCurrent() > 0 && condition.getCurrent() > 0;
    }

    @Override
    public void publishEvent(RepairEvent event) {
        RepairEventPublisher.super.publishEvent(event);
    }

    @Override
    public void subscribeAsPublisher(RepairEventChannel eventChannel) {
        RepairEventPublisher.super.subscribeAsPublisher(eventChannel);
        repairEventChannel = eventChannel;
    }
}
