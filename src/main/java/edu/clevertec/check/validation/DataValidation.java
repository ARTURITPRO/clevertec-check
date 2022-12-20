package edu.clevertec.check.validation;


import edu.clevertec.check.exception.DataException;

/**
 * Validation
 * <p>
 * Used to validate an array of strings.
 * </p>
 *
 * @version JDK 17
 * @author Artur Malashkov
 */

public interface DataValidation {
    /**
     * Checking the date for validity.
     *
     * @throws DataException if the parameters are valid.
     */
     void isEmpty();

    boolean validationFile();
    boolean validationStringArray();

    /**
     * {@value #PATTERN_DATA_VALIDATION_STRING_ARRAY} Pattern for validating input data String mass.
     */
    String PATTERN_DATA_VALIDATION_STRING_ARRAY = "\\s*((\\d+-\\d+)?(\\s+\\d+-\\d+)+\\s+(\\w{8}|\\w{10})-\\d{1,20})\\s*|" +
            "(\\s*(\\w{8}|\\w{10})-\\d{1,20}\\s*(\\d+-\\d+)?(\\s+\\d+-\\d+)+)\\s*|" +
            "\\s*((\\d+-\\d+)?(\\s+\\d+-\\d+)+\\s+(\\w{8}|\\w{10})-\\d{1,20}\\s+(\\d+-\\d+)?(\\s+\\d+-\\d+)+)\\s*";
    /**
     * {@value #PATTERN_DATA_VALIDATION_STRING_FILE} Pattern for validating input data the path to the file.
     */
    String PATTERN_DATA_VALIDATION_STRING_FILE = ".txt";

    String MESSAGE_DATA_INVALID = "The %s + is not valid. Check the correctness of the entered data." +
            "They must be entered in regular expression format:" +
            " %s Valid data example: 1-1 2-2 3-3 4-4 5-5 6-6 7-7 8-8 9-9 mastercard-11111" +
            " or enter the path of the source file, e.g. src\\main\\resources\\data.txt";
}
