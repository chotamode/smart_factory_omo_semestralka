package Production.ProductionEntity.Human;

import Management.Visitable;
import Management.Visitor;
import Operation.WorkType.WorkType;
import Production.ProductionEntity.ProductionEntity;
import Util.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Worker is a human that can perform operations.
 * It can be assigned to a production line.
 * WorkType is a type of work that worker can perform.
 */

@Setter
@Getter
public class Worker extends ProductionEntity implements Visitable {

    private static int lastId = 0;
    private final int id = lastId++;
    private int hourlyMaintenanceCost = Constants.DEFAULT_WORKER_HOURLY_MAINTENANCE_COST;


    public Worker(@JsonProperty("workType") WorkType workType) {
        super(workType);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
