package Factory;

import Product.ProductBuilder;
import Product.ProductSeries;

public class ToyFactory extends Factory{

    public ToyFactory() {
        super();
    }

    @Override
    public void acceptOrder(String productName, int amount) {
        ProductSeries productSeries = new ProductSeries(productName);
        ProductBuilder productBuilder = new ProductBuilder();

        switch (productName) {
            case "Learning Tablet":
                for (int i = 0; i < amount; i++) {
                    productSeries.addProduct(productBuilder.buildProductLearnTablet());
                }
                break;
            case "Remote-Controlled Car":
                for (int i = 0; i < amount; i++) {
                    productSeries.addProduct(productBuilder.buildProductRemControlCar());
                }
                break;
            case "Building Block Set":
                for (int i = 0; i < amount; i++) {
                    productSeries.addProduct(productBuilder.buildProductBlockSet());
                }
                break;
            default:
                throw new RuntimeException("No such product");
        }

        getOrderedProductSeries().add(productSeries);
    }
}
