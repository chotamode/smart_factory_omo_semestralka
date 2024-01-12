package ProductionEntity.Device;

import Management.Visitable;
import Management.Visitor;
import Operation.OperationalCapable;
import Operation.WorkType.WorkType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Machine extends Device implements OperationalCapable, Visitable {

    private int hourlyMaintenanceCost = 3;


    public Machine(WorkType workType) {
        super(workType);
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
