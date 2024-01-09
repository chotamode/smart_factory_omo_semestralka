package ProductionEntity.Device.Resourse;

import Exceptions.DeviceResource.OilException;

public class Oil extends DeviceResourceAbstract{
    public Oil(int max, int current) {
        super(max, current);
    }

    @Override
    public void spend(int amount) throws Exception {
        try {
            super.spend(amount);
        } catch (Exception e) {
            throw new OilException(e.getMessage());
        }
    }
}
