import Operation.WorkType.CobotWorkType;
import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import Product.ProductSeries;
import Production.Factory.ElectronicsFactory;
import Production.Factory.FactoryConfigurator;
import Production.ProductionEntity.Device.Cobot;
import Production.ProductionEntity.Device.Machine;
import Production.ProductionEntity.Human.Repairman;
import Production.ProductionEntity.Human.Worker;
import Production.ProductionLine.ProductionLine;
import Reporting.Report.EventGroup;
import Reporting.ReportCreator.ConfigurationReportCreator;
import Reporting.ReportCreator.ConsumptionReportCreator;
import Reporting.ReportCreator.EventReportCreator;
import Reporting.ReportCreator.OutagesReportCreator;
import Util.TimeAndReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SimulationElectronicsFactory{

    Logger logger = LogManager.getLogger(SimulationElectronicsFactory.class);

    ElectronicsFactory electronicsFactory;

    public void runJson1() {
        electronicsFactory = FactoryConfigurator.configureElectronicsFactory("src/main/resources/FactoryConfigurations/configuration1.json");
        runSimulation();
    }

    public void runJson2() {
        electronicsFactory = FactoryConfigurator.configureElectronicsFactory("src/main/resources/FactoryConfigurations/configuration2.json");
        runSimulation();
    }

    public void runDefault(){
        electronicsFactory = new ElectronicsFactory();
        defaultScenario(electronicsFactory);
    }

    public void runOwnJson(String fileName){
        electronicsFactory = FactoryConfigurator.configureElectronicsFactory("src/main/resources/FactoryConfigurations/" + fileName);
        runSimulation();
    }

    private void runSimulation() {
        final ConfigurationReportCreator configurationReportCreator = new ConfigurationReportCreator();
        final EventReportCreator eventReportCreator = new EventReportCreator();
        final ConsumptionReportCreator consumptionReportCreator = new ConsumptionReportCreator();
        final OutagesReportCreator outagesReportCreator = new OutagesReportCreator();

        try {
            electronicsFactory.startProduction();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        while (electronicsFactory.isWorking()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }


//                                     Inspectors and directors execute inspections                                 //
//------------------------------------------------------------------------------------------------------------------//

        electronicsFactory.executeInspectorInspection();
        electronicsFactory.executeDirectorInspection();

//                                                 Generate reports                                                 //
//------------------------------------------------------------------------------------------------------------------//

        Long startTime = TimeAndReportManager.getInstance().getStartTime();
        Long endTime = TimeAndReportManager.getInstance().getEndTime();

        configurationReportCreator.createReportForTime(TimeAndReportManager.getInstance().getStartTime() + 100L);

        eventReportCreator.createReportForTimePeriod(startTime, endTime, EventGroup.BY_HANDLER);
        eventReportCreator.createReportForTimePeriod(startTime, endTime, EventGroup.BY_SOURCE);
        eventReportCreator.createReportForTimePeriod(startTime, endTime, EventGroup.BY_TYPE);

        consumptionReportCreator.createReportForTimePeriod(startTime, endTime);

        outagesReportCreator.createReportForTimePeriod(startTime, endTime);

//--------------------------------------------------------------------------------------------------------------------//

        electronicsFactory.getDevicesState(473454L);

        logger.info("Factory delivers order to client");
        try {
            for (ProductSeries productSeries : electronicsFactory.deliverAllOrders()) {
                logger.info("Product series: " + productSeries.getProductName() + " delivered in amount of " + productSeries.getProducts().size());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        TimeAndReportManager.getInstance().stop();
    }

    private void defaultScenario(ElectronicsFactory electronicsFactory) {

        electronicsFactory.setProductionLines(List.of(new ProductionLine(1), new ProductionLine(2), new ProductionLine(3)));

        electronicsFactory.setOperationalCapables(List.of(new Machine(MachineWorkType.MACHINE_PRESSING), new Machine(MachineWorkType.MACHINE_PRESSING), new Machine(MachineWorkType.MACHINE_PRESSING), new Machine(MachineWorkType.MACHINE_MOLDING), new Machine(MachineWorkType.MACHINE_CUTTING), new Machine(MachineWorkType.MACHINE_CUTTING),

                new Worker(HumanWorkType.HUMAN_MOLDING), new Worker(HumanWorkType.HUMAN_PRESSING), new Worker(HumanWorkType.HUMAN_CUTTING), new Worker(HumanWorkType.HUMAN_CUTTING), new Worker(HumanWorkType.HUMAN_CUTTING),

                new Cobot(CobotWorkType.COBOT_MOLDING), new Cobot(CobotWorkType.COBOT_MOLDING), new Cobot(CobotWorkType.COBOT_MOLDING), new Cobot(CobotWorkType.COBOT_PRESSING), new Cobot(CobotWorkType.COBOT_CUTTING)));

        electronicsFactory.setRepairmen(List.of(new Repairman()
//                ,
//                new Repairman(),
//                new Repairman(),
//                new Repairman()
        ));

        final int PRODUCTION_AMOUNT1 = 200;
        final int PRODUCTION_AMOUNT2 = 100;
        final int PRODUCTION_AMOUNT3 = 50;

        electronicsFactory.acceptOrder("Smartphone", PRODUCTION_AMOUNT1);
        electronicsFactory.acceptOrder("Smartwatch", PRODUCTION_AMOUNT2);
        electronicsFactory.acceptOrder("Laptop", PRODUCTION_AMOUNT3);

        try {
            electronicsFactory.startProduction();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        while (electronicsFactory.isWorking()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }

        final int PRODUCTION_AMOUNT4 = 100;
        final int PRODUCTION_AMOUNT5 = 200;
        final int PRODUCTION_AMOUNT6 = 300;
        electronicsFactory.acceptOrder("Laptop", PRODUCTION_AMOUNT4);
        electronicsFactory.acceptOrder("Laptop", PRODUCTION_AMOUNT5);
        electronicsFactory.acceptOrder("Laptop", PRODUCTION_AMOUNT6);

        try {
            electronicsFactory.startProduction();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        while (electronicsFactory.isWorking()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }

        TimeAndReportManager.getInstance().stop();
    }
}
