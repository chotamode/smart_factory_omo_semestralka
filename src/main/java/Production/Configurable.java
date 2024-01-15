package Production;

/**
 * Configurable interface is used for classes that can be configured.
 * It is used for creating configuration reports.
 */
public interface Configurable {

    /**
     * Returns list of configurations.
     *
     */
    Configuration getConfigurations();

    String getName();
}
