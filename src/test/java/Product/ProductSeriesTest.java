package Product;

import Operation.WorkType.HumanWorkType;
import Operation.WorkType.WorkType;
import Operation.Operation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductSeriesTest {

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
}