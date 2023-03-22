package edu.clevertec.check.validation;

/**
 * Validation
 * <p>
 * Used to validate an array of strings.
 * </p>
 *
 * @author Artur Malashkov
 * @version JDK 17
 */
public interface DiscountCardValidation {
    /**
     * Checking the date for validity.
     *
     * @return true if the parameters are valid, otherwise false
     */
    boolean isValid();

    /**
     * {@value #PATTERN_NAME_CARD} Pattern for validating name card.
     */
    String PATTERN_NAME_CARD = "(?i)\\s*masterCard\\s*";
    /**
     * {@value #PATTERN_NUMBER_CARD}Pattern for validating number card.
     */
    String PATTERN_NUMBER_CARD = "\\s*\\d{5}\\s*";
}
