package EventManagement.Events;

import ProductionEntity.Device.Device;
import ProductionEntity.Device.Machine;
import ProductionEntity.Human.Repairman;
import Util.TimeAndReportManager;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RepairEvent extends Event{

    private Device device;
    private Repairman repairman;
    private Long repairTime = 3L;
    private Long repairStartTime;
    private int priority;

    public RepairEvent(String name, Device device, EventType type, Long timeStamp) {
        super(name, type, timeStamp);
        this.device = device;
    }

    public RepairEvent(String test, Object device, EventType eventType, long timeStamp, int linePriority) {
        super(test, eventType, timeStamp);
        this.device = (Device) device;
        this.priority = linePriority;
    }

    public void setRepairman(Repairman repairman) {
        this.repairman = repairman;
        this.repairStartTime = TimeAndReportManager.getInstance().getCurrentTime();
    }

    public Long getRepairEndTime(){
        return repairStartTime + repairTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
