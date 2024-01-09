package ProductionEntity.Human;

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
public class Worker extends ProductionEntity {

    public Worker(WorkType workType) {
        super(workType);
    }

}
