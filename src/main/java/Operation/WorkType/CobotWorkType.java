package Operation.WorkType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;

public enum CobotWorkType implements WorkType {
    COBOT_CUTTING("COBOT_CUTTING"),
    COBOT_PRESSING("COBOT_PRESSING"),
    COBOT_MOLDING("COBOT_MOLDING");

    private final String name;

    CobotWorkType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static CobotWorkType forValue(JsonNode jsonNode) {
        String value = jsonNode.get("name").asText();
        for (CobotWorkType type : CobotWorkType.values()) {
            if (type.name.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid name: " + value);
    }

}