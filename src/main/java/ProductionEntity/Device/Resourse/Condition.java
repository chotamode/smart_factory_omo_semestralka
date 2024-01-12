package ProductionEntity.Device.Resourse;

import Exceptions.ConditionException;

public class Condition extends DeviceResourceAbstract{
    public Condition(int max, int current) {
        super(max, current);
    }

    @Override
    public void spend() throws Exception {
        try {
            super.spend();
        } catch (Exception e) {
            throw new ConditionException("Condition is too low, please repair device");
        }
    }
}
