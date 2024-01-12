package ProductionEntity.Human;

import EventManagement.Channels.RepairEventChannel;
import EventManagement.EventListener.RepairEventListener;
import EventManagement.EventPublisher.RepairEventEventPublisher;
import EventManagement.Events.RepairEvent;
import EventManagement.Events.EventType;
import Exceptions.RepairmanBusyException;
import Management.Visitable;
import Management.Visitor;
import Util.TimeAndReportManager;
import Util.TimeObserver;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Repairman implements RepairEventListener, RepairEventEventPublisher, TimeObserver, Visitable {

    private static int lastId = 0;
    private final int id = lastId++;

    private boolean isBusy = false;

    private RepairEventChannel repairEventChannel;
    private RepairEvent currentEvent;

    private int maintenancesDone = 0;

    public Repairman() {
        subscribeToTimeAndReportManager();
    }

    @Override
    public void react(RepairEvent event) throws RepairmanBusyException {

        if(isBusy){
            throw new RepairmanBusyException("Repairman is busy");
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
        RepairEventEventPublisher.super.subscribeAsPublisher(eventChannel);
        repairEventChannel = eventChannel;
    }

    @Override
    public boolean onTimeUpdate(Long time) {

        if(currentEvent != null && currentEvent.getRepairEndTime() > time){
            return true;
        }

        if( currentEvent != null && currentEvent.getRepairEndTime() <= time){
            if(currentEvent.getType() == EventType.NEEDS_REPAIR){
                currentEvent.getDevice().getCondition().fullRefill();
                publishEvent(new RepairEvent("Repair finished", currentEvent.getDevice(), EventType.REPAIR_DONE, TimeAndReportManager.getInstance().getCurrentTime()));
                currentEvent.getDevice().repair();
                currentEvent.getDevice().getCondition().increaseConsuption(1);
                maintenancesDone++;
            }else if(currentEvent.getType() == EventType.NEEDS_OIL_REFILL){
                currentEvent.getDevice().getOil().fullRefill();
                publishEvent(new RepairEvent("Oil refill finished", currentEvent.getDevice(), EventType.OIL_REFILL_DONE, TimeAndReportManager.getInstance().getCurrentTime()));
                currentEvent.getDevice().repair();
                currentEvent.getDevice().getOil().increaseConsuption(1);
                maintenancesDone++;
            }
            isBusy = false;
            setCurrentEvent(null);
            return true;
        }
        return false;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
