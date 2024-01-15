package Management;

import Product.Product;
import Production.Factory.Factory;
import Production.ProductionEntity.Device.Cobot;
import Production.ProductionEntity.Device.Machine;
import Production.ProductionEntity.Human.Repairman;
import Production.ProductionEntity.Human.Worker;
import Production.ProductionLine.ProductionLine;

public interface Visitor {

    void visit(Factory factory);

    void visit(Machine machine);

    void visit(Cobot cobot);

    void visit(Worker worker);

    void visit(Repairman repairman);

    void visit(Product product);

    void visit(ProductionLine productionLine);

    void executeInspection();
}
