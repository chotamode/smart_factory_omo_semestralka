package Production.Factory;

import Production.ProductionEntity.OperationalCapable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * FactoryConfiguration is used for configuring the factory.
 * It contains attributes:
 * <ul>
 *     <li>factoryType - type of the factory</li>
 *     <li>productionLines - list of priorities of production lines</li>
 *     <li>repairmen - number of repairmen</li>
 *     <li>workers - list of workers</li>
 *     <li>orderedProducts - list of ordered products</li>
 * </ul>
 */
@Getter
@Setter
public class FactoryConfiguration {

    @JsonProperty("factoryType")
    private Class<? extends Factory> factoryType;

    @JsonProperty("productionLines")
    private List<Integer> productionLines;

    @JsonProperty("repairmen")
    private int repairmen;

    @JsonProperty("workers")
    private List<OperationalCapable> workers;

    @JsonProperty("orderedProducts")
    private List<Map<String, Integer>> orderedProducts;

}