package edu.clevertec.check.validation.impl;

import edu.clevertec.check.validation.FileValidation;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * File validation
 * <p>
 * Used for file validation.
 * </p>
 *
 * @author Artur Malashkov
 * @version JDK 17
 */
@Slf4j
public class FileValidationImpl implements FileValidation {
    /**
     * The file, which should contain the following parameters: products-quantity discount card.
     */
    private static File file;

    /**
     * File validation
     * <p>
     * Checking the value of string args against the file path.
     * </p>
     *
     * @param args an array containing the path to the file with parameters or parameters.
     * @return try, if  the args is the path to the source file.
     */
    @Override
    public boolean isValidFile(String[] args) {
        file = new File(args[0].trim());
        log.debug("file found: " + file.isFile());
        return file.isFile();
    }

    /**
     * File validation
     * <p>
     * Checking the value of string args against the file path.
     * </p>
     *
     * @param predicate this is a functional interface.
     * @param str       this is the file path.
     * @return try, if  the args is the path to the source file.
     */
    public static boolean isValid(Predicate<String[]> predicate, String[] str) {
        predicate.test(str);
        return true;
    }

    /**
     * Load Strings from file.
     */
    public static class FileUpLoad {
        /**
         * Load Strings from file.
         * <p>
         * Reads the buyer's request and his card from the file.
         * </p>
         *
         * @return array of strings containing the list of ordered products and the quantity of products, as well as
         * the name of the discount card and its number.
         * @throws FileNotFoundException
         * @throws IOException
         */
        @SneakyThrows({FileNotFoundException.class, IOException.class})
        public static String[] loadStringsFromFile() {
            @Cleanup
            BufferedReader bufferedReader = new BufferedReader(new FileReader(FileValidationImpl.file));
            List<String> listOfStrings = new ArrayList<>();
            String line;
            while (Objects.nonNull(line = bufferedReader.readLine())) {
                listOfStrings.add(line);
            }
            String[] fileContents = listOfStrings.toArray(new String[0]);
            log.debug("file contents:" + Arrays.toString(fileContents));
            return fileContents;
        }
    }
}
