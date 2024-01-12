package Product.Material;

/**
 * Enum class for material types
 */
public enum MaterialType {
    PROCESSOR(10),
    BATTERY(11),
    RAM(12),
    STORAGE(9),
    SMARTPHONE_SCREEN(8),
    CAMERA_LENS(11),
    FINGERPRINT_SENSOR(10),
    SMARTPHONE_CASING(7),
    SMARTWATCH_SCREEN(8),
    HEART_RATE_SENSOR(10),
    KEYBOARD(10),
    MOTHERBOARD(10),
    COOLING_FAN(8),
    PLASTIC(5),
    PAINT(6),
    ELECTRONIC_COMPONENT(10),
    REMOTE_CONTROL(10),
    WHEELS(6),
    DECORATIVE_ELEMENTS(5),
    BASEPLATES(5),
    MINIFIGURES(5);

    private int price = 0;

    MaterialType(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return this.name().toLowerCase().replace("_", " ");
    }
}
