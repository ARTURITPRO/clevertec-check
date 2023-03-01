package edu.clevertec.check.exception;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.repository.impl.ProductRepoImpl;
import edu.clevertec.check.service.ProductService;
import edu.clevertec.check.service.impl.ProductServiceImpl;
import edu.clevertec.check.validation.DiscountCardValidation;
import edu.clevertec.check.validation.ProductValidation;
import edu.clevertec.check.validation.impl.DiscountCardValidationImpl;
import edu.clevertec.check.validation.impl.ProductValidationImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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