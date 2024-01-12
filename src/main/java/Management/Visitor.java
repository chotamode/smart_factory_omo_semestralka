package Management;

import Factory.Factory;
import ProductionEntity.Device.Cobot;
import ProductionEntity.Device.Machine;
import ProductionEntity.Human.Repairman;
import ProductionEntity.Human.Worker;
import Product.Product;
import Factory.ProductionLine;

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
