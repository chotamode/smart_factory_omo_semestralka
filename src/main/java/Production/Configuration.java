package Production;

import Util.TimeAndReportManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for storing configuration of {@link Configurable} object to show it in {@link Reporting.Report.ConfigurationReport}.
 */
@Getter
@AllArgsConstructor
public class Configuration {

    private Configurable source;
    private List<Map<String, String>> configurations;
    private Long timeStamp = TimeAndReportManager.getInstance().getCurrentTime();
    private Map<Configurable, Configuration> configurableConfigurationMap = new LinkedHashMap<>();

    public Configuration(List<Map<String, String>> configurations, Configurable source) {
        this.configurations = configurations;
        this.source = source;
    }

    public List<Map<String, String>> getConfigurationsList() {
        return configurations;
    }

    public String getSourceName() {
        return source.getName();
    }
}
