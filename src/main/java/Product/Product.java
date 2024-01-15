package Product;

import Management.Visitable;
import Management.Visitor;
import Operation.Operation;
import Operation.WorkType.WorkType;
import Product.Material.ProductMaterial;
import Product.Material.StockMaterial;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Product implements Visitable {

    private String name;
    private String description;
    private boolean finished;
    private Operation currentOperation;
    private Operation firstOperation;
    private Operation lastOperation;
    private int seriesIndex;

    private List<ProductMaterial> productMaterials = new ArrayList<>() {
        @Override
        public boolean contains(Object o) {
            if (o instanceof ProductMaterial productMaterial) {
                return this.stream().anyMatch(material -> material.getType().equals(productMaterial.getType()));
            }
            return false;
        }
    };

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void addProductMaterial(ProductMaterial productMaterial) throws Exception {

        if (productMaterials.contains(productMaterial)) {
            throw new Exception("Product already contains this material");
        }

        productMaterials.add(productMaterial);
    }

    public void addOperation(Operation operation) throws Exception {
        if (containsOperation(operation)) {
            throw new Exception("Product already contains this operation");
        }
        if (firstOperation == null) {
            firstOperation = operation;
            lastOperation = operation;
            currentOperation = operation;
        } else {
            lastOperation.setNextOperation(operation);
            lastOperation = operation;
        }
    }

    private boolean containsOperation(Operation operation) {
        Operation currentOperation = firstOperation;
        while (currentOperation != null && currentOperation.getNextOperation() != null) {
            if (currentOperation.getWorkType() == operation.getWorkType()) {
                return true;
            }
            currentOperation = currentOperation.getNextOperation();
        }
        return false;
    }

    public List<WorkType> getWorkerSequence() {
        List<WorkType> workerSequence = new ArrayList<>();
        Operation currentOperation = firstOperation;
        while (currentOperation != null) {
            workerSequence.add(currentOperation.getWorkType());
            currentOperation = currentOperation.getNextOperation();
        }
        return workerSequence;
    }

    public List<Operation> getOperations() {
        List<Operation> operations = new ArrayList<>();
        Operation currentOperation = firstOperation;
        while (currentOperation != null) {
            operations.add(currentOperation);
            currentOperation = currentOperation.getNextOperation();
        }
        return operations;
    }

    public void setNextOperation() {
        currentOperation = currentOperation.getNextOperation();
        if (currentOperation == null) {
            finished = true;
        }
    }

    public Operation getNextOperation() {
        return currentOperation.getNextOperation();
    }

    public boolean isFinished() {
        for (Operation operation : getOperations()) {
            if (!operation.isFinished()) {
                finished = false;
                return false;
            }
        }
        finished = true;
        return true;
    }

    public int getTotalCost() {
        int totalCost = 0;
        for (Operation operation : getOperations()) {
            totalCost += operation.getCost();
        }
        for (ProductMaterial productMaterial : productMaterials) {
            totalCost += productMaterial.getCost();
        }
        return totalCost;
    }

    public void setStockMaterials(List<StockMaterial> stockMaterials) {
        for (ProductMaterial productMaterial : productMaterials) {
            for (StockMaterial stockMaterial : stockMaterials) {
                if (productMaterial.getType() == stockMaterial.getType()) {
                    productMaterial.setStockMaterial(stockMaterial);
                }
            }
        }
    }

    public void addMaterials() throws Exception {
        for (ProductMaterial productMaterial : productMaterials) {
            productMaterial.addMaterial();
        }
    }
}
