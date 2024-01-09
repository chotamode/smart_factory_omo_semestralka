package Factory;

import Product.Product;
import Product.ProductBuilder;
import Product.ProductSeries;

public class ElectronicsFactory extends Factory{

    public ElectronicsFactory() {
        super();
    }


    @Override
    public void acceptOrder(String productName, int amount) {

        if(!canAcceptOrder()){
            System.out.println("Can't accept more than " + getProductionLines().size() + " orders.");
            return;
        }

        ProductSeries productSeries = new ProductSeries(productName);
        ProductBuilder productBuilder = new ProductBuilder();

        Product product;

        switch (productName) {
            case "Smartphone":
                for(int i = 0; i < amount; i++) {
                    productSeries.addProduct(productBuilder.buildProductSmartphone());
                }
                break;
            case "Smartwatch":
                for(int i = 0; i < amount; i++) {
                    productSeries.addProduct(productBuilder.buildProductSmartWatch());
                }
                break;
            case "Laptop":
                for(int i = 0; i < amount; i++) {
                    productSeries.addProduct(productBuilder.buildProductLaptop());
                }
                break;
            default:
                throw new RuntimeException("No such product");
        }

        getOrderedProductSeries().add(productSeries);
    }
}
