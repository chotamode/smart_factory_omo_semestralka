package Operation;

import Operation.WorkType.WorkType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Operation {
    private WorkType workType;
    private int cost;
    private boolean performed = false;
    private Operation nextOperation;

    public Operation(WorkType workType, int cost, boolean performed) {
        this.workType = workType;
        this.cost = cost;
        this.performed = performed;
    }

    public boolean isFinished() {
        return performed;
    }

    public void perform() {
        performed = true;
    }
}
