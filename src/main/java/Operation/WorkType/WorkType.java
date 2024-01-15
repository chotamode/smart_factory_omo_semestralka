package Operation.WorkType;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "workType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HumanWorkType.class, name = "HUMAN"),
        @JsonSubTypes.Type(value = MachineWorkType.class, name = "MACHINE"),
        @JsonSubTypes.Type(value = CobotWorkType.class, name = "COBOT")
})
public interface WorkType {

    @JsonValue
    default String toValue() {
        return this.toString();
    }

}
