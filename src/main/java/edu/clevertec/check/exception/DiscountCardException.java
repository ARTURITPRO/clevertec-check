package edu.clevertec.check.exception;

/**
 * Class for creating Exception DiscountCardException.
 *
 * @author Artur Malashkov
 * @version JDK 1.8
 */
public class DiscountCardException extends RuntimeException {

    private final String message;

    public DiscountCardException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
