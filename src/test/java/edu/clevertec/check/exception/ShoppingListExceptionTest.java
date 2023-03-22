package edu.clevertec.check.exception;

import edu.clevertec.check.repository.impl.DiscountCardRepoImpl;
import edu.clevertec.check.repository.impl.ProductRepoImpl;
import edu.clevertec.check.service.OrderProcessingService;
import edu.clevertec.check.service.impl.DiscountCardServiceImpl;
import edu.clevertec.check.service.impl.OrderProcessingServiceImpl;
import edu.clevertec.check.service.impl.ProductServiceImpl;
import edu.clevertec.check.util.ConnectionManagerImpl;
import edu.clevertec.check.validation.impl.DiscountCardValidationImpl;
import edu.clevertec.check.validation.impl.ProductValidationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UnitTest")
class ShoppingListExceptionTest {

    private String [] data;

    private OrderProcessingService resultProcessedData;

    @BeforeEach
    void init (){
         resultProcessedData = new OrderProcessingServiceImpl(
                 new ConnectionManagerImpl(),
                 new ProductValidationImpl(),
                 new DiscountCardValidationImpl(),
                 new ProductServiceImpl(new ProductRepoImpl()),
                 data,
                 new DiscountCardServiceImpl(new DiscountCardRepoImpl()));
    }
    @Test
    void getMessage() {
        assertThrows(ShoppingListException.class, () -> resultProcessedData.formationOfCheck());
    }
}
