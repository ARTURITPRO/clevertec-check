package edu.clevertec.check.exception;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.service.OrderProcessingService;
import edu.clevertec.check.service.impl.OrderProcessingServiceImpl;
import edu.clevertec.check.validation.DataValidation;
import edu.clevertec.check.validation.DiscountCardValidation;
import edu.clevertec.check.validation.impl.DataValidationImpl;
import edu.clevertec.check.validation.impl.DiscountCardValidationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DiscountCardExceptionTest {
    private String[] data;
    private DiscountCardValidation discountCardValidation;

    @BeforeEach
    void init (){
        data = new String[2];
        data[0] = "mastercard";
        data[1] = "1111";
        discountCardValidation = new DiscountCardValidationImpl(data);
    }
    @Test
    void getMessage() {
     assertThrows(DiscountCardException.class, discountCardValidation::isValid);
    }
}