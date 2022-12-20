package edu.clevertec.check.exception;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.service.OrderProcessingService;
import edu.clevertec.check.service.impl.OrderProcessingServiceImpl;
import edu.clevertec.check.validation.DataValidation;
import edu.clevertec.check.validation.impl.DataValidationImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataExceptionTest {
    String[] emptyData;

    @BeforeEach
    void init() {
        emptyData = new String[0];
    }

    @Test
    void getMessage() {
        DataValidation dataValidation = new DataValidationImpl(emptyData);
        assertThrows(DataException.class, dataValidation::isEmpty);
    }
}
