package ProductionEntity.Device;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.EventPublisher.RepairEventPublisher;
import Operation.WorkType.WorkType;
import Product.Product;
import ProductionEntity.Device.Resourse.Condition;
import ProductionEntity.Device.Resourse.Electricity;
import ProductionEntity.Device.Resourse.Oil;
import ProductionEntity.ProductionEntity;
import lombok.Getter;

@Getter
public abstract class Device extends ProductionEntity implements RepairEventPublisher {

    private final Condition condition = new Condition(100, 100);
    private final Electricity electricity = new Electricity(100, 100);
    private final Oil oil = new Oil(100, 100);

    private RepairEventChannel repairEventChannel;

    public Device(WorkType workType) {
        super(workType);
    }

    @Override
    public void workOnProduct(Product product) {
        super.workOnProduct(product);
        condition.spend(1);
        electricity.spend(1);
        oil.spend(1);
    }
}
