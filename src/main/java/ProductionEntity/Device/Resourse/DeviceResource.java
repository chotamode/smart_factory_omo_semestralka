package ProductionEntity.Device.Resourse;

public interface DeviceResource {
    public void spend() throws Exception;
    public void refill(int amount);

    public void fullRefill();

    public void increaseConsuption(int amount);
}
