package edu.clevertec.check.exception;

/**
 * Class for creating Exception DataException.
 *
 * @author Artur Malashkov
 * @version JDK 1.8
 */
public class DataException extends RuntimeException {

    private final String message;

    public DataException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}