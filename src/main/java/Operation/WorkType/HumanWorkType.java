package Operation.WorkType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;

public enum HumanWorkType implements WorkType {
    HUMAN_CUTTING("HUMAN_CUTTING"),
    HUMAN_PRESSING("HUMAN_PRESSING"),
    HUMAN_MOLDING("HUMAN_MOLDING");

    private final String name;

    HumanWorkType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static HumanWorkType forValue(JsonNode jsonNode) {
        String value = jsonNode.get("name").asText();
        for (HumanWorkType type : HumanWorkType.values()) {
            if (type.name.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid name: " + value);
    }

}
