package Management;

import Product.Product;
import Production.Factory.Factory;
import Production.ProductionEntity.Human.Repairman;
import Production.ProductionEntity.OperationalCapable;
import Production.ProductionLine.ProductionLine;

import java.util.ArrayList;

public class Director extends Management {
    public Director(Factory factory) {
        super(factory);
    }

    @Override
    public void executeInspection() {

        getVisitables().add(getFactory());
        for (Repairman repairman : getFactory().getRepairmen()) {
            getVisitables().add(repairman);
        }
        for (ProductionLine productionLine : getFactory().getProductionLines()) {
            getVisitables().add(productionLine);
            for (OperationalCapable operationalCapable : productionLine.getOperationalCapables()) {
                getVisitables().add(operationalCapable);
            }
            for (Product product : productionLine.getProductSeries() != null ? productionLine.getProductSeries().getProducts() : new ArrayList<Product>()) {
                getVisitables().add(product);
            }
        }

        super.executeInspection();
    }
}
