package edu.clevertec.check.validation;

/**
 * File validation
 * <p>
 * Used for file validation.
 * </p>
 *
 * @author Artur Malashkov
 * @version JDK 17
 */
public interface FileValidation {

    /**
     * File validation
     * <p>
     * Checking the value of string args against the file path.
     * </p>
     *
     * @param args an array containing the path to the file with parameters or parameters.
     * @return try, if  the args is the path to the source file.
     */
    boolean isValidFile(String[] args);
}
