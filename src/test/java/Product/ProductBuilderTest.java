package Product;

import Operation.WorkType.CobotWorkType;
import Operation.WorkType.HumanWorkType;
import Operation.WorkType.MachineWorkType;
import Operation.WorkType.WorkType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductBuilderTest {

    List<WorkType> workerSequenceSmartPhone = List.of(
            HumanWorkType.HUMAN_MOLDING,
            CobotWorkType.COBOT_PRESSING,
            MachineWorkType.MACHINE_CUTTING
    );

    @Test
    void buildProductSmartphone() {
        ProductBuilder productBuilder = new ProductBuilder();
        Product product = productBuilder.buildProductSmartphone();
        assertEquals("Smartphone", product.getName());
        assertEquals(3, product.getOperations().size());
        assertEquals(5, product.getProductMaterials().size());
        assertEquals(workerSequenceSmartPhone, product.getWorkerSequence());
    }

    @Test
    void createProductSeries() {
        ProductBuilder productBuilder = new ProductBuilder();
        ProductSeries productSeries = new ProductSeries("Smartphone");
        for(int i = 0; i < 100; i++) {
            productSeries.addProduct(productBuilder.buildProductSmartphone());
        }
        assertEquals(100, productSeries.getProducts().size());
        for (Product product : productSeries.getProducts()) {
            assertEquals("Smartphone", product.getName());
            assertEquals(3, product.getOperations().size());
            assertEquals(5, product.getProductMaterials().size());
            assertEquals(workerSequenceSmartPhone, product.getWorkerSequence());
        }
    }

}