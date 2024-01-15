package Production.ProductionEntity.Device.Resourse;

/**
 * DeviceResource interface is used for resources that are used by devices.
 * It is used for creating consumption reports and preventing devices from using resources that are not available by throwing exceptions.
 */
public interface DeviceResource {
    void spend() throws Exception;

    void refill(int amount);

    void fullRefill();

    void increaseConsumption(int amount);
}
