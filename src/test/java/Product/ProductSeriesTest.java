package Product;

import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import Operation.WorkType.WorkType;
import Operation.Operation;
import Product.Material.MaterialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductSeriesTest {

    ProductBuilder productBuilder = new ProductBuilder();
    ProductSeries productSeries1;
    Map<MaterialType, Integer> totalNeededMaterials1 = new HashMap<>();

    @BeforeEach
    void setUp() {
        int PRODUCTION_AMOUNT1 = 200;
        productSeries1 = new ProductSeries("Smartphone");

        for (int i = 0; i < PRODUCTION_AMOUNT1; i++) {
            Product product = productBuilder.buildProductSmartphone();
            productSeries1.addProduct(product);
        }

        totalNeededMaterials1.put(MaterialType.PROCESSOR, PRODUCTION_AMOUNT1);
        totalNeededMaterials1.put(MaterialType.BATTERY, PRODUCTION_AMOUNT1);
        totalNeededMaterials1.put(MaterialType.RAM, PRODUCTION_AMOUNT1);
        totalNeededMaterials1.put(MaterialType.STORAGE, PRODUCTION_AMOUNT1);
        totalNeededMaterials1.put(MaterialType.SMARTPHONE_SCREEN, PRODUCTION_AMOUNT1);
    }

    @Test
    void isFinished() throws Exception {
        ProductSeries productSeries = new ProductSeries("TestProduct");
        Product product1 = new Product();
        product1.setName("TestProduct");
        product1.setFinished(true);

        product1.addOperation(new Operation(HumanWorkType.HUMAN_CUTTING, 0, true));

        productSeries.addProduct(product1);

        boolean isFinished = productSeries.isFinished();

        assertTrue(isFinished);


    }

    @Test
    void getTotalMaterialNeeded() {
        assertEquals(totalNeededMaterials1, productSeries1.getTotalMaterialNeeded());
    }
}