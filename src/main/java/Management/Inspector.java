package Management;

import Production.Factory.Factory;
import Production.ProductionEntity.OperationalCapable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Inspector extends Management {
    public Inspector(Factory factory) {
        super(factory);
    }

    @Override
    public void executeInspection() {

        List<OperationalCapable> operationalCapables = new ArrayList<>(getFactory().getOperationalCapables());

        operationalCapables.sort(Comparator.comparingInt(OperationalCapable::getTotalMaintenanceCost).reversed());

        getVisitables().addAll(operationalCapables);

        super.executeInspection();
    }
}
