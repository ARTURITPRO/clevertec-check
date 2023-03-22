package edu.clevertec.check.exception;

/**
 * Class for creating Exception ProductException.
 *
 * @author Artur Malashkov
 * @version JDK 1.8
 */
public class ProductException extends RuntimeException {

    private String message;

    public ProductException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
