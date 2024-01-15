package Operation.WorkType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;

public enum MachineWorkType implements WorkType {
    MACHINE_CUTTING("MACHINE_CUTTING"),
    MACHINE_PRESSING("MACHINE_PRESSING"),
    MACHINE_MOLDING("MACHINE_MOLDING");

    private final String name;

    MachineWorkType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static MachineWorkType forValue(JsonNode jsonNode) {
        String value = jsonNode.get("name").asText();
        for (MachineWorkType type : MachineWorkType.values()) {
            if (type.name.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid name: " + value);
    }

}