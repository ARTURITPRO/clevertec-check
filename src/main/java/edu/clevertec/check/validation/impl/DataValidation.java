package edu.clevertec.check.validation.impl;

import edu.clevertec.check.exception.DataException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Args validation
 * <p>
 * Used to validate an array of strings. And control the printing of a receipt to the console or file,
 * depending on the arguments passed to the args-array.
 * </p>
 *
 * @author Artur Malashkov
 * @version JDK 1.8
 */
@Slf4j
public class DataValidation {

    public static String PATTERN_DATA_VALIDATION_STRING_ARRAY = "\\s*((\\d+-\\d+)?(\\s+\\d+-\\d+)+\\s+(\\w{8}|\\w{10})-\\d{1,20})\\s*|(\\s*(\\w{8}|\\w{10})-\\d{1,20}\\s*(\\d+-\\d+)?(\\s+\\d+-\\d+)+)\\s*|\\s*((\\d+-\\d+)?(\\s+\\d+-\\d+)+\\s+(\\w{8}|\\w{10})-\\d{1,20}\\s+(\\d+-\\d+)?(\\s+\\d+-\\d+)+)\\s*";

    /**
     * Is valid
     * <p>
     * Checks an array of strings against a file path (which contains an array containing a shopping list in a store and
     * a discount card) or an array of strings (containing a list of shopping in a store and a discount card). Controls
     * whether the receipt is output to the console or file, depending on the arguments passed to the args array.
     * </p>
     *
     * @throws DataException if the array of strings is empty.
     */
    public static void requiredNotEmptyArgs(String[] args) {
        Arrays.stream(args).findAny().orElseThrow(() -> new DataException(" Empty Data"));
    }

    public static boolean isReadFromConsole(String[] args) {
        String inputData = String.join(" ", args);
        Pattern patternArgsConsoleString = Pattern.compile(PATTERN_DATA_VALIDATION_STRING_ARRAY);
        Matcher matcherArgsConsoleString = patternArgsConsoleString.matcher(inputData);
        if (matcherArgsConsoleString.matches()) {
            log.info("String args is Found");
            return true;
        }
        return false;
    }

    public static boolean isReadFromFile(String[] args) {
        String inputData = String.join(" ", args);
        Pattern patternConsoleFileString = Pattern.compile(".txt");
        Matcher matcherConsoleFileString = patternConsoleFileString.matcher(inputData);
        FileValidationImpl fileValidationImpl = new FileValidationImpl();
        Predicate<String[]> predicate = fileValidationImpl::isValidFile;
        if (matcherConsoleFileString.find() & FileValidationImpl.isValid(predicate, args)) {
            log.info("File is found");
            return true;
        }
        return false;
    }

    public static void requiredNotEmptyArgs() {
    }
}
