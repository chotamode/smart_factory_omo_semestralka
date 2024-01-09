package EventManagement.Events;

import ProductionEntity.Device.Device;
import ProductionEntity.Device.Machine;
import ProductionEntity.Human.Repairman;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RepairEvent extends Event{

    private Device device;
    private Repairman repairman;
    private Long repairTime = 3L;

    public RepairEvent(String name, Device device, EventType type, Long timeStamp) {
        super(name, type, timeStamp);
        this.device = device;
    }

    public void setRepairman(Repairman repairman) {
        this.repairman = repairman;
    }

    public Long getRepairEndTime(){
        return getTimeStamp() + repairTime;
    }
}
