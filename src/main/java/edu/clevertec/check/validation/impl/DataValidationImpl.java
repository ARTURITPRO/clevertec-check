package edu.clevertec.check.validation.impl;

import edu.clevertec.check.exception.DataException;
import edu.clevertec.check.validation.DataValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
 * @version JDK 1.8
 * @author Artur Malashkov
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DataValidationImpl implements DataValidation {

    String[] args;
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
    @Override
    public void isEmpty() {
        Arrays.stream(args).findAny().orElseThrow(() -> new DataException(" Empty Data"));
    }

    @Override
    public boolean validationStringArray() {
        String inputData = String.join(" ", args);
        Pattern patternArgsConsoleString = Pattern.compile(PATTERN_DATA_VALIDATION_STRING_ARRAY);
        Matcher matcherArgsConsoleString = patternArgsConsoleString.matcher(inputData);
        if (matcherArgsConsoleString.matches()){
            log.info("String args is Found");
            return true;
        }
        return false;
    }
    @Override
    public boolean validationFile() {
        String inputData = String.join(" ", args);
        Pattern patternConsoleFileString = Pattern.compile(PATTERN_DATA_VALIDATION_STRING_FILE);
        Matcher matcherConsoleFileString = patternConsoleFileString.matcher(inputData);
        FileValidationImpl fileValidationImpl = new FileValidationImpl();
        Predicate<String[]> predicate = fileValidationImpl::isValidFile;
        if (matcherConsoleFileString.find() & FileValidationImpl.isValid(predicate, args)) {
            log.info("File is found");
            return true;
        }
        return false;
    }
}
