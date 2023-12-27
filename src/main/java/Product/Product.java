package Product;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String name;
    private String description;
    private int quantity;
    private List<ProductMaterial> productMaterials = new ArrayList<>(){
        @Override
        public boolean contains(Object o) {
            if (o instanceof ProductMaterial productMaterial) {
                return this.stream().anyMatch(material -> material.getType().equals(productMaterial.getType()));
            }
            return false;
        }
    };

    public void addProductMaterial(ProductMaterial productMaterial) throws Exception {
        if (productMaterials.contains(productMaterial)) {
            throw new Exception("Product already contains this material");
        }
        productMaterials.add(productMaterial);
    }
}
