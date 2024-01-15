package Production.Factory;

import Production.ProductionEntity.Human.Repairman;
import Production.ProductionEntity.OperationalCapable;
import Production.ProductionLine.ProductionLine;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * FactoryConfigurator is a class that configures the factory based on a json file.
 * It uses the FactoryConfiguration class to store the configuration.
 */
public class FactoryConfigurator {

    private static final Logger logger = LogManager.getLogger(FactoryConfigurator.class);

    public static ElectronicsFactory configureElectronicsFactory(String jsonFilePath) {

        ObjectMapper objectMapper = new ObjectMapper();
        FactoryConfiguration factoryConfiguration;
        ElectronicsFactory factory = null;

        try {

            factoryConfiguration = objectMapper.readValue(new File(jsonFilePath), FactoryConfiguration.class);

            if (factoryConfiguration.getFactoryType() != ElectronicsFactory.class) {
                throw new RuntimeException("Wrong factory type in configuration file.");
            }

            factory = (ElectronicsFactory) factoryConfiguration.getFactoryType().getDeclaredConstructor().newInstance();

            for (Integer priority : factoryConfiguration.getProductionLines()) {
                factory.addProductionLine(new ProductionLine(priority));
            }

            for (int i = 0; i < factoryConfiguration.getRepairmen(); i++) {
                factory.addRepairman(new Repairman());
            }

            for (OperationalCapable o : factoryConfiguration.getWorkers()) {
                factory.addWorker(o);
            }

            for (Map<String, Integer> orderedProduct : factoryConfiguration.getOrderedProducts()) {
                for (Map.Entry<String, Integer> entry : orderedProduct.entrySet()) {
                    factory.acceptOrder(entry.getKey(), entry.getValue());
                }
            }

        } catch (IOException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            logger.error(e.getMessage());
        }

        return factory;
    }

}