package ProductionEntity.Device.Resourse;

import lombok.Getter;

@Getter
public class DeviceResourceAbstract implements DeviceResource {
    int min = 0;
    int max = 100;
    int current = 100;
    int consumption = 1;
    int totalSpent = 0;

    public DeviceResourceAbstract(int max, int current) {
        this.max = max;
        this.current = current;
    }

    @Override
    public void spend() throws Exception {
        if (current - consumption <= min) {
            current -= consumption;
            totalSpent += consumption;
            throw new Exception("Not enough " + this.getClass().getSimpleName());
        } else {
            current -= consumption;
            totalSpent += consumption;
        }
    }

    @Override
    public void refill(int amount) {
        if (current + amount > max) {
            throw new RuntimeException("Too much " + this.getClass().getSimpleName());
        } else {
            current += amount;
        }
    }

    @Override
    public void fullRefill() {
        current = max;
    }

    @Override
    public void increaseConsuption(int amount) {
        consumption += amount;
    }
}
