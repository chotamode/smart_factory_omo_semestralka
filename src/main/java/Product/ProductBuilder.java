package Product;

public class ProductBuilder {
    private Product product;

    public ProductBuilder() {
        product = new Product();
    }

    /**
     * @param productMaterial
     * @return
     */
    public ProductBuilder addProductMaterial(ProductMaterial productMaterial) throws Exception {
        product.addProductMaterial(productMaterial);
        return this;
    }

    public Product build() {
        return product;
    }


    /**
     * Reset the builder to its initial state.
     */
    public void reset() {
        product = new Product();
    }

//    build particular product with particular materials
    public Product buildProductA(){

    }

}
