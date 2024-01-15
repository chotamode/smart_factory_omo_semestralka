package Product;

import Operation.Operation;
import Operation.WorkType.CobotWorkType;
import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import Product.Material.MaterialType;
import Product.Material.MaterialUnits;
import Product.Material.ProductMaterial;

public class ProductBuilder {
    private Product product;

    public ProductBuilder() {
        product = new Product();
    }

    public ProductBuilder addProductMaterial(ProductMaterial productMaterial) throws Exception {
        product.addProductMaterial(productMaterial);
        return this;
    }

    public ProductBuilder addOperation(Operation operation) throws Exception {
        product.addOperation(operation);
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

    /**
     * Prebuilt Product objects.
     */
    public Product buildProductSmartphone() {
        reset();
        product.setName("Smartphone");
        product.setDescription("A high-end smartphone with advanced features.");
        try {
            addProductMaterial(new ProductMaterial(1, MaterialUnits.UNIT, MaterialType.PROCESSOR))
                    .addProductMaterial(new ProductMaterial(1, MaterialUnits.UNIT, MaterialType.BATTERY))
                    .addProductMaterial(new ProductMaterial(1, MaterialUnits.UNIT, MaterialType.RAM))
                    .addProductMaterial(new ProductMaterial(1, MaterialUnits.UNIT, MaterialType.STORAGE))
                    .addProductMaterial(new ProductMaterial(1, MaterialUnits.UNIT, MaterialType.SMARTPHONE_SCREEN))
                    .addOperation(new Operation(HumanWorkType.HUMAN_MOLDING, 1, false))
                    .addOperation(new Operation(CobotWorkType.COBOT_PRESSING, 1, false))
                    .addOperation(new Operation(MachineWorkType.MACHINE_CUTTING, 1, false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return build();
    }

    public Product buildProductSmartWatch() {
        reset();
        product.setName("Smartwatch");
        product.setDescription("A versatile smartwatch with health tracking features.");
        try {
            addProductMaterial(new ProductMaterial(MaterialType.BATTERY))
                    .addProductMaterial(new ProductMaterial(MaterialType.SMARTWATCH_SCREEN))
                    .addProductMaterial(new ProductMaterial(MaterialType.HEART_RATE_SENSOR))
                    .addOperation(new Operation(HumanWorkType.HUMAN_CUTTING, 1, false))
                    .addOperation(new Operation(CobotWorkType.COBOT_MOLDING, 1, false))
                    .addOperation(new Operation(MachineWorkType.MACHINE_CUTTING, 1, false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return build();
    }

    public Product buildProductLaptop() {
        reset();
        product.setName("Laptop");
        product.setDescription("A portable computer suitable for work and entertainment.");
        try {
            addProductMaterial(new ProductMaterial(MaterialType.PROCESSOR))
                    .addProductMaterial(new ProductMaterial(MaterialType.BATTERY))
                    .addProductMaterial(new ProductMaterial(2, MaterialType.RAM))
                    .addProductMaterial(new ProductMaterial(MaterialType.STORAGE))
                    .addProductMaterial(new ProductMaterial(MaterialType.KEYBOARD))
                    .addProductMaterial(new ProductMaterial(MaterialType.MOTHERBOARD))
                    .addProductMaterial(new ProductMaterial(2, MaterialType.COOLING_FAN))
                    .addOperation(new Operation(HumanWorkType.HUMAN_CUTTING, 1, false))
                    .addOperation(new Operation(CobotWorkType.COBOT_MOLDING, 1, false))
                    .addOperation(new Operation(MachineWorkType.MACHINE_PRESSING, 1, false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return build();
    }

    public Product buildProductLearnTablet() {
        reset();
        product.setName("Learning Tablet");
        product.setDescription("A tablet computer for children to learn and play.");
        try {
            addProductMaterial(new ProductMaterial(MaterialType.PROCESSOR))
                    .addProductMaterial(new ProductMaterial(MaterialType.BATTERY))
                    .addProductMaterial(new ProductMaterial(2, MaterialType.RAM))
                    .addProductMaterial(new ProductMaterial(MaterialType.STORAGE))
                    .addProductMaterial(new ProductMaterial(MaterialType.SMARTPHONE_SCREEN))
                    .addProductMaterial(new ProductMaterial(MaterialType.CAMERA_LENS))
                    .addProductMaterial(new ProductMaterial(MaterialType.FINGERPRINT_SENSOR))
                    .addProductMaterial(new ProductMaterial(2, MaterialType.SMARTPHONE_CASING))
                    .addOperation(new Operation(HumanWorkType.HUMAN_CUTTING, 1, false))
                    .addOperation(new Operation(CobotWorkType.COBOT_MOLDING, 1, false))
                    .addOperation(new Operation(MachineWorkType.MACHINE_MOLDING, 1, false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return build();
    }

    public Product buildProductRemControlCar() {
        reset();
        product.setName("Remote-Controlled Car");
        product.setDescription("A high-speed, durable remote-controlled car for indoor and outdoor play.");
        try {
            addProductMaterial(new ProductMaterial(MaterialType.PLASTIC))
                    .addProductMaterial(new ProductMaterial(2, MaterialUnits.L, MaterialType.PAINT))
                    .addProductMaterial(new ProductMaterial(MaterialType.REMOTE_CONTROL))
                    .addProductMaterial(new ProductMaterial(MaterialType.WHEELS))
                    .addProductMaterial(new ProductMaterial(MaterialType.ELECTRONIC_COMPONENT))
                    .addProductMaterial(new ProductMaterial(MaterialType.BATTERY))
                    .addOperation(new Operation(HumanWorkType.HUMAN_CUTTING, 1, false))
                    .addOperation(new Operation(CobotWorkType.COBOT_MOLDING, 1, false))
                    .addOperation(new Operation(MachineWorkType.MACHINE_MOLDING, 1, false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return build();
    }

    public Product buildProductBlockSet() {
        reset();
        product.setName("Building Block Set");
        product.setDescription("A set of interlocking plastic bricks for creative building and play.");
        try {
            addProductMaterial(new ProductMaterial(MaterialType.PLASTIC))
                    .addProductMaterial(new ProductMaterial(MaterialType.DECORATIVE_ELEMENTS))
                    .addProductMaterial(new ProductMaterial(MaterialType.BASEPLATES))
                    .addProductMaterial(new ProductMaterial(MaterialType.MINIFIGURES))
                    .addProductMaterial(new ProductMaterial(MaterialType.PAINT))
                    .addOperation(new Operation(HumanWorkType.HUMAN_CUTTING, 1, false))
                    .addOperation(new Operation(CobotWorkType.COBOT_MOLDING, 1, false))
                    .addOperation(new Operation(MachineWorkType.MACHINE_MOLDING, 1, false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return build();
    }

}
