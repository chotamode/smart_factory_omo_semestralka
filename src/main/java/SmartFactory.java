import Production.Factory.ElectronicsFactory;
import Production.Factory.FactoryConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;


public class SmartFactory {

    private static final Logger logger = LogManager.getLogger(SmartFactory.class);
    private static final SimulationElectronicsFactory simulationElectronicsFactory = new SimulationElectronicsFactory();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        logger.info("Do you want to run one of the configurations from the JSON file or the default configuration? (Enter 'json1', 'json2', 'default' or 'own json')");

        String userInput = scanner.nextLine();

        if (userInput.equalsIgnoreCase("json1")) {
            simulationElectronicsFactory.runJson1();
        } else if (userInput.equalsIgnoreCase("json2")) {
            simulationElectronicsFactory.runJson2();
        } else if (userInput.equalsIgnoreCase("default")) {
            simulationElectronicsFactory.runDefault();
        } else if (userInput.equalsIgnoreCase("own json")) {
            logger.info("Enter file name located in src/main/resources/FactoryConfigurations/");
            String fileName = scanner.nextLine();
            simulationElectronicsFactory.runOwnJson(fileName);
        } else {
            logger.info("Wrong input. Default scenario will be run.");
            simulationElectronicsFactory.runDefault();
        }

    }
}
