package edu.clevertec.check.exception;

import edu.clevertec.check.validation.DiscountCardValidation;
import edu.clevertec.check.validation.impl.DiscountCardValidationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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