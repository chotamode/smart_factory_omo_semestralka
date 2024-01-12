package ProductionEntity.Device.Resourse;

import Exceptions.OilException;

public class Oil extends DeviceResourceAbstract{
    public Oil(int max, int current) {
        super(max, current);
    }

    @Override
    public void spend() throws Exception {
        try {
            super.spend();
        } catch (Exception e) {
            throw new OilException(e.getMessage());
        }
    }
}
