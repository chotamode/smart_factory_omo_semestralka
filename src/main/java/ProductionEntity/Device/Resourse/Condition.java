package ProductionEntity.Device.Resourse;

import Exceptions.DeviceResource.ConditionException;

public class Condition extends DeviceResourceAbstract{
    public Condition(int max, int current) {
        super(max, current);
    }

    @Override
    public void spend(int amount) throws Exception {
        try {
            super.spend(amount);
        } catch (Exception e) {
            throw new ConditionException("Condition is too low, please repair device");
        }
    }
}
