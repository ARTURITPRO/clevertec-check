package edu.clevertec.check.exception;

/**
 * Class for creating Exception ShoppingListException.
 *
 * @author Artur Malashkov
 * @version JDK 1.8
 */
public class ShoppingListException extends RuntimeException {
    private final String message;

    public ShoppingListException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
