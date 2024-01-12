package ProductionEntity.Human;

import Management.Visitable;
import Management.Visitor;
import Operation.WorkType.WorkType;
import ProductionEntity.ProductionEntity;
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
    private final int childId = lastId++;
    private int hourlyMaintenanceCost = 1;


    public Worker(WorkType workType) {
        super(workType);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
