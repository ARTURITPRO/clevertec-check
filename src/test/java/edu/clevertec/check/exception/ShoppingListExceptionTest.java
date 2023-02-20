package edu.clevertec.check.exception;

import edu.clevertec.check.repository.impl.DiscountCardRepoImpl;
import edu.clevertec.check.repository.impl.ProductRepoImpl;
import edu.clevertec.check.service.OrderProcessingService;
import edu.clevertec.check.service.impl.DiscountCardServiceImpl;
import edu.clevertec.check.service.impl.OrderProcessingServiceImpl;
import edu.clevertec.check.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ShoppingListExceptionTest {


    private String [] data;
    OrderProcessingService resultProcessedData;
    @BeforeEach
    void init (){
         resultProcessedData = new OrderProcessingServiceImpl(new ProductServiceImpl
                 (new ProductRepoImpl()), data,  new DiscountCardServiceImpl(new DiscountCardRepoImpl()));

    }
    @Test
    void getMessage() {
        assertThrows(ShoppingListException.class, () -> resultProcessedData.formationOfCheck());
    }
}