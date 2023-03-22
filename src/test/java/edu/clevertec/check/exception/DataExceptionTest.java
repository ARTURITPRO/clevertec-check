package edu.clevertec.check.exception;

import edu.clevertec.check.validation.impl.DataValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UnitTest")
class DataExceptionTest {
    String[] emptyData;

    @BeforeEach
    void init() {
        emptyData = new String[0];
    }

    @Test
    void getMessage() {
        assertThrows(DataException.class, () -> DataValidation.requiredNotEmptyArgs(emptyData));
    }
}
