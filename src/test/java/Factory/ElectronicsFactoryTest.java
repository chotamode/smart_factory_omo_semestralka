package Factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElectronicsFactoryTest {

    @Test
    void acceptOrder() {
        ElectronicsFactory electronicsFactory = new ElectronicsFactory();
        electronicsFactory.acceptOrder("Smartphone", 100);
        assertEquals(1, electronicsFactory.getOrderedProductSeries().size());
        assertEquals(100, electronicsFactory.getOrderedProductSeries().get(0).getProducts().size());
        assertEquals("Smartphone", electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getName());
        assertEquals(3, electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getOperations().size());
        assertEquals(5, electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getProductMaterials().size());
        assertEquals(3, electronicsFactory.getOrderedProductSeries().get(0).getProducts().get(0).getWorkerSequence().size());
    }

}