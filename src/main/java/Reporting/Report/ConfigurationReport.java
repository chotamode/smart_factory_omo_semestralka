package Reporting.Report;

import Production.Configurable;
import Production.Configuration;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Creates ConfigurationReport. ConfigurationReport contains information about configurations.
 * For example:
 * <ul>
 *     <li>Line priority</li>
 *     <li>Oil consumption</li>
 *     <li>Condition consumption</li>
 *     <li>Max oil level</li>
 *     <li>Max condition level</li>
 *     <li>Work type</li>
 *     <li>Production line worker sequence</li>
 * </ul>
 */
public class ConfigurationReport extends Report {

    List<Configuration> configurations;

    public ConfigurationReport(Long timeFrom, Long timeTo, List<Configuration> configurations) throws FileNotFoundException {
        super(timeFrom, timeTo);
        this.configurations = configurations;
    }

    @Override
    public void printReport() {
        for (Configuration configuration : configurations) {
            printWriter.println("Configuration at time: " + configuration.getTimeStamp());

            printWriter.println("Configurations for " + configuration.getSourceName() + ":");
            for (Map<String, String> configurationMap : configuration.getConfigurationsList()) {
                for (Map.Entry<String, String> entry : configurationMap.entrySet()) {
                    printWriter.println(entry.getKey() + ": " + entry.getValue());
                }
            }

            for (Map.Entry<Configurable, Configuration> entry : configuration.getConfigurableConfigurationMap().entrySet()) {
                printWriter.println("");
                printWriter.println("Configurations for " + entry.getKey().getName() + ":");
                for (Map<String, String> configurationMap : entry.getValue().getConfigurationsList()) {
                    for (Map.Entry<String, String> entry2 : configurationMap.entrySet()) {
                        printWriter.println(entry2.getKey() + ": " + entry2.getValue());
                    }
                }
            }
        }
    }
}
