package ProductionEntity.Device;

import Operation.Operation;
import Operation.OperationalCapable;
import Operation.WorkType.WorkType;
import Product.Product;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Cobot extends Device implements OperationalCapable {

    public Cobot(WorkType workType) {
        super(workType);
    }
}
