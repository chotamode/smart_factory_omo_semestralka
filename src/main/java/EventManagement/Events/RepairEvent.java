package EventManagement.Events;

import Production.ProductionEntity.Device.Device;
import Production.ProductionEntity.Human.Repairman;
import Util.TimeAndReportManager;
import lombok.Getter;

import java.util.Optional;

@Getter
public class RepairEvent extends Event {

    private final Device device;
    private final Long repairTime = 3L;
    private Repairman repairman;
    private Long repairStartTime;
    private int priority;

    public RepairEvent(String name, Device device, EventType type, Long timeStamp) {
        super(name, type, timeStamp, null, null);
        this.device = device;
        this.source = device;
    }

    public RepairEvent(String test, Device device, EventType eventType, long timeStamp, int linePriority) {
        super(test, eventType, timeStamp, device, null);
        this.device = device;
        this.priority = linePriority;
    }

    public void setRepairman(Repairman repairman) {
        this.repairman = repairman;
        this.repairStartTime = TimeAndReportManager.getInstance().getCurrentTime();
        this.target = repairman;
    }

    /**
     * Returns the time when the repair will be finished. If not returns Optional.
     *
     */
    public Optional<Long> getRepairEndTime() {
        if (repairStartTime == null) {
            return Optional.empty();
        } else {
            return Optional.of(repairStartTime + repairTime);
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Long getWaitTime() {
        if (repairStartTime == null) {
            return TimeAndReportManager.getInstance().getCurrentTime() - getTimeStamp();
        } else {
            return repairStartTime - getTimeStamp();
        }
    }

    public Long getOutageTime() {
        if (repairStartTime == null) {
            return TimeAndReportManager.getInstance().getCurrentTime() - getTimeStamp();
        } else {
            return repairStartTime + repairTime - getTimeStamp();
        }
    }
}
