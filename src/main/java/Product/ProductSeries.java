package Product;

import Operation.WorkType.WorkType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    public void addProduct(Product product) {
        if(!Objects.equals(productName, product.getName())) {
            throw new IllegalArgumentException("Product name does not match product series name");
        }
        if(workerSequence == null) {
            workerSequence = product.getWorkerSequence();
        }
        if(!Objects.equals(workerSequence, product.getWorkerSequence())) {
            throw new IllegalArgumentException("Product worker sequence does not match product series worker sequence");
        }

        if(!products.isEmpty()) {
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
        for(Product product : products) {
            if(!product.isFinished()) {
                return false;
            }
        }
        seriesFinished = true;
        return true;
    }
}
