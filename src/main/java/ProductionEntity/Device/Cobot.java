package ProductionEntity.Device;

import Management.Visitable;
import Management.Visitor;
import Operation.OperationalCapable;
import Operation.WorkType.WorkType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Cobot extends Device implements OperationalCapable, Visitable {

    private int hourlyMaintenanceCost = 2;

    public Cobot(WorkType workType) {
        super(workType);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
