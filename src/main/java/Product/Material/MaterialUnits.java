package Product.Material;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum class for material units
 */
@AllArgsConstructor
@Getter
public enum MaterialUnits {
    KG("Kilograms"),
    L("Liters"),
    M("Meters"),
    M2("Square Meters"),
    M3("Cubic Meters"),
    UNIT("Unit");

    private final String description;
}
