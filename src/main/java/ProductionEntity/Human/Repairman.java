package ProductionEntity.Human;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.EventListener.RepairEventListener;
import EventManagement.EventPublisher.RepairEventPublisher;
import EventManagement.Events.RepairEvent;
import EventManagement.Events.EventType;
import Util.TimeAndReportManager;
import Util.TimeObserver;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Repairman implements RepairEventListener, RepairEventPublisher, TimeObserver {

    private boolean isBusy = false;

    private RepairEventChannel repairEventChannel;
    private RepairEvent currentEvent;

    public Repairman() {
        subscribeToTimeAndReportManager();
    }

    @Override
    public void react(RepairEvent event) {

        if(isBusy){
            return;
        }

        if(event.getType() == EventType.NEEDS_REPAIR){
            isBusy = true;
        }else if(event.getType() == EventType.NEEDS_OIL_REFILL) {
            isBusy = true;
        }else{
            return;
        }

        event.setRepairman(this);
        setCurrentEvent(event);

    }

    @Override
    public void subscribeAsListener(RepairEventChannel eventChannel) {
        RepairEventListener.super.subscribeAsListener(eventChannel);
        repairEventChannel = eventChannel;
    }

    @Override
    public void subscribeAsPublisher(RepairEventChannel eventChannel) {
        RepairEventPublisher.super.subscribeAsPublisher(eventChannel);
        repairEventChannel = eventChannel;
    }

    @Override
    public boolean onTimeUpdate(Long time) {

        if(currentEvent != null && currentEvent.getRepairEndTime() > time){
            return true;
        }

        if( currentEvent != null && currentEvent.getRepairEndTime() < time){
            if(currentEvent.getType() == EventType.NEEDS_REPAIR){
                currentEvent.getDevice().getCondition().fullRefill();
                publishEvent(new RepairEvent("Repair finished", currentEvent.getDevice(), EventType.REPAIR_DONE, TimeAndReportManager.getInstance().getCurrentTime()));
            }else if(currentEvent.getType() == EventType.NEEDS_OIL_REFILL){
                currentEvent.getDevice().getOil().fullRefill();
                publishEvent(new RepairEvent("Oil refill finished", currentEvent.getDevice(), EventType.OIL_REFILL_DONE, TimeAndReportManager.getInstance().getCurrentTime()));
            }
            isBusy = false;
            setCurrentEvent(null);
            return true;
        }
        return false;
    }
}
