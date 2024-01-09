package ProductionEntity.Device.Resourse;

public interface DeviceResource {
    public void spend(int amount) throws Exception;
    public void refill(int amount);

    public void fullRefill();
}
