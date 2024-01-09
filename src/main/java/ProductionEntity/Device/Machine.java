package ProductionEntity.Device;

import Operation.Operation;
import Operation.OperationalCapable;
import Operation.WorkType.WorkType;
import Product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Machine extends Device implements OperationalCapable {

    public Machine(WorkType workType) {
        super(workType);
    }
}
