package edu.clevertec.check.exception;

import edu.clevertec.check.validation.DiscountCardValidation;
import edu.clevertec.check.validation.impl.DiscountCardValidationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UnitTest")
class DiscountCardExceptionTest {
    private String[] data;
    private DiscountCardValidation  discountCardValidation = new DiscountCardValidationImpl();;

    @BeforeEach
    void init (){
        data = new String[2];
        data[0] = "mastercard";
        data[1] = "1111";
    }
    @Test
    void getMessage() {
     assertThrows(DiscountCardException.class, ()->discountCardValidation.isValid(data));
    }
}
