package edu.clevertec.check.exception;

import edu.clevertec.check.validation.ProductValidation;
import edu.clevertec.check.validation.impl.ProductValidationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UnitTest")
class ProductExceptionTest {

    private String data;

    private ProductValidation productValidation;

    @BeforeEach
    void init (){
        productValidation = new ProductValidationImpl();
        data = "1-0";
    }
    @Test
    void getMessage() {
        assertThrows(ProductException.class, () -> productValidation.isValidProductAndQuantity(data));
    }
}
