package Management;

import Factory.Factory;
import Factory.ProductionLine;
import Operation.OperationalCapable;
import Product.Product;
import ProductionEntity.Human.Repairman;

public class Director extends Management{
    public Director(Factory factory) {
        super(factory);
    }

    @Override
    public void executeInspection() {

        getVisitables().add(getFactory());
        for(Repairman repairman : getFactory().getRepairmen()) {
            getVisitables().add(repairman);
        }
        for(ProductionLine productionLine : getFactory().getProductionLines()) {
            getVisitables().add(productionLine);
            for(OperationalCapable operationalCapable : productionLine.getOperationalCapables()) {
                getVisitables().add(operationalCapable);
            }
            for(Product product : productionLine.getProductSeries().getProducts()) {
                getVisitables().add(product);
            }
        }

        super.executeInspection();
    }
}
