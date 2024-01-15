package Production.ProductionEntity.Device;

import Management.Visitable;
import Management.Visitor;
import Operation.WorkType.WorkType;
import Production.ProductionEntity.OperationalCapable;
import Util.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Machine extends Device implements OperationalCapable, Visitable {

    private static int lastId = 0;
    private final int id = lastId++;

    private int hourlyMaintenanceCost = Constants.DEFAULT_MACHINE_HOURLY_MAINTENANCE_COST;


    public Machine(@JsonProperty("workType") WorkType workType) {
        super(workType);
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
