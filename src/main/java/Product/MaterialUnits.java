package Product;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
