package edu.clevertec.check.exception;

import edu.clevertec.check.service.OrderProcessingService;
import edu.clevertec.check.service.impl.OrderProcessingServiceImpl;
import edu.clevertec.check.validation.ProductValidation;
import edu.clevertec.check.validation.impl.ProductValidationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingListExceptionTest {


    private String [] data;
    OrderProcessingService resultProcessedData;
    @BeforeEach
    void init (){
         resultProcessedData = new OrderProcessingServiceImpl(data);

    }
    @Test
    void getMessage() {
        assertThrows(ShoppingListException.class, () -> resultProcessedData.formationOfCheck());
    }
}