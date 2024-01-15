package Product;

import Operation.WorkType.WorkType;
import Product.Material.MaterialType;
import Product.Material.ProductMaterial;
import Product.Material.StockMaterial;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class ProductSeries {
    List<Product> products = new ArrayList<>();
    String productName;
    List<WorkType> workerSequence;

    boolean seriesFinished = false;

    public ProductSeries(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "ProductSeries{" +
                "productName='" + productName + '\'' +
                "} in amount of " + products.size();
    }

    public void addProduct(Product product) {
        if (!Objects.equals(productName, product.getName())) {
            throw new IllegalArgumentException("Product name does not match product series name");
        }
        if (workerSequence == null) {
            workerSequence = product.getWorkerSequence();
        }
        if (!Objects.equals(workerSequence, product.getWorkerSequence())) {
            throw new IllegalArgumentException("Product worker sequence does not match product series worker sequence");
        }

        if (!products.isEmpty()) {
            product.setSeriesIndex(products.get(products.size() - 1).getSeriesIndex() + 1);
        } else {
            product.setSeriesIndex(0);
        }

        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public Product getProductByIndex(int index) {
        return products.get(index);
    }

    public boolean isFinished() {
        for (Product product : products) {
            if (!product.isFinished()) {
                return false;
            }
        }
        seriesFinished = true;
        return true;
    }

    public Map<MaterialType, Integer> getTotalMaterialNeeded() {
        Map<MaterialType, Integer> totalMaterialNeeded = new HashMap<>();
        for (Product product : products) {
            for (ProductMaterial productMaterial : product.getProductMaterials()) {
                totalMaterialNeeded.merge(productMaterial.getType(), productMaterial.getQuantityNeeded(), Integer::sum);
            }
        }
        return totalMaterialNeeded;
    }

    public void setStockMaterials(List<StockMaterial> stockMaterials) {
        for (Product product : products) {
            product.setStockMaterials(stockMaterials);
        }
    }

    public Map<MaterialType, Integer> getNotSetStockMaterials() {
        Map<MaterialType, Integer> notSetStockMaterials = new HashMap<>();
        for (Product product : products) {
            for (ProductMaterial productMaterial : product.getProductMaterials()) {
                if (productMaterial.getStockMaterial().isEmpty()) {
                    notSetStockMaterials.merge(productMaterial.getType(), productMaterial.getQuantityNeeded(), Integer::sum);
                }
            }
        }
        return notSetStockMaterials;
    }

    public void addMaterials() throws Exception {
        for (Product product : products) {
            product.addMaterials();
        }
    }

    public int getAmountInSeries() {
        return products.size();
    }
}
