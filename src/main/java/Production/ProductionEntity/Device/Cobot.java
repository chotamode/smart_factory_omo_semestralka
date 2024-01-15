package Production.ProductionEntity.Device;

import Management.Visitable;
import Management.Visitor;
import Operation.WorkType.WorkType;
import Production.ProductionEntity.OperationalCapable;
import Util.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Cobot extends Device implements OperationalCapable, Visitable {

    private static int lastId = 0;
    private final int id = lastId++;

    private int hourlyMaintenanceCost = Constants.DEFAULT_COBOT_HOURLY_MAINTENANCE_COST;

    public Cobot(@JsonProperty("workType") WorkType workType) {
        super(workType);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
