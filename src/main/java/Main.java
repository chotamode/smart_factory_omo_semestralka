import Management.Director;
import Management.Inspector;
import ProductionEntity.Device.Cobot;
import ProductionEntity.Device.Machine;
import Factory.ElectronicsFactory;
import Factory.ProductionLine;
import ProductionEntity.Human.Repairman;
import ProductionEntity.Human.Worker;
import Operation.WorkType.CobotWorkType;
import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import Product.ProductBuilder;
import Product.Product;
import Util.TimeAndReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
//        Factory.Factory accepts order to produce Product in amount of 100
//        Factory.Factory produces Product in amount of 100
//        Factory.Factory constructs production Factory.ProductionLine of particular type, with particular Devices and Workers
//        Factory.Factory starts production Factory.ProductionLine

//        Scenario for Electronics Manufacturing Factory.Factory

        final ElectronicsFactory electornicsFactory = new ElectronicsFactory();

        electornicsFactory.setProductionLines(List.of(
                new ProductionLine(1),
                new ProductionLine(2),
                new ProductionLine(3)
        ));

        electornicsFactory.setOperationalCapables(List.of(
                new Machine(MachineWorkType.MACHINE_PRESSING),
                new Machine(MachineWorkType.MACHINE_MOLDING),
                new Machine(MachineWorkType.MACHINE_CUTTING),
                new Machine(MachineWorkType.MACHINE_CUTTING),
                new Worker(HumanWorkType.HUMAN_MOLDING),
                new Worker(HumanWorkType.HUMAN_PRESSING),
                new Worker(HumanWorkType.HUMAN_CUTTING),
                new Worker(HumanWorkType.HUMAN_CUTTING),
                new Cobot(CobotWorkType.COBOT_MOLDING),
                new Cobot(CobotWorkType.COBOT_MOLDING),
                new Cobot(CobotWorkType.COBOT_PRESSING),
                new Cobot(CobotWorkType.COBOT_CUTTING)
        ));

        electornicsFactory.setRepairmen(List.of(
                new Repairman()
//                ,
//                new Repairman(),
//                new Repairman(),
//                new Repairman()
        ));

        final int PRODUCTION_AMOUNT1 = 200;
        final int PRODUCTION_AMOUNT2 = 100;
        final int PRODUCTION_AMOUNT3 = 50;
        final ProductBuilder productBuilder = new ProductBuilder();

        Director director = new Director(electornicsFactory);
        Inspector inspector = new Inspector(electornicsFactory);

        logger.info("Electronics factory accepts order to produce Smartphone in amount of " + PRODUCTION_AMOUNT1);
        electornicsFactory.acceptOrder("Smartphone", PRODUCTION_AMOUNT1);
        electornicsFactory.acceptOrder("Smartwatch", PRODUCTION_AMOUNT2);
        electornicsFactory.acceptOrder("Laptop", PRODUCTION_AMOUNT3);

//        TimeAndReportManager.getInstance().start();

        electornicsFactory.startProduction();

        while(electornicsFactory.isWorking()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        director.executeInspection();
        inspector.executeInspection();

        logger.info("Factory delivers order to client");
        logger.info(electornicsFactory.deliverOrder("Smartphone", PRODUCTION_AMOUNT1));

        TimeAndReportManager.getInstance().stop();

    }
}
