package ProductionEntity.Device.Resourse;

public interface DeviceResource {
    public void spend(int amount);
    public void refill(int amount);

    public void fullRefill();
}
