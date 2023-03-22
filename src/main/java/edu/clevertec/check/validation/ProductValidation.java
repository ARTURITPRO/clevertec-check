package edu.clevertec.check.validation;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.exception.ProductException;

import java.util.function.Predicate;

public interface ProductValidation {
    String PATTERN_IDPRODUCT_QUANTITY = "\\d+-\\d+";

    /**
     * Is Valid Product And Quantity
     * <p>
     * This method checks the string that contains the products and their quantities for the validity of the identifier
     * and the quantity of products
     * </p>
     *
     * @param str contains the name of the product and its quantity.
     * @return true, if the ID and quantity of the products are validated.
     * @throws ProductException if the format of the type and quantity of the product is incorrectly specified
     *                          in line args, or  if the quantity of the product exceeds the store's stock of 9999 the
     *                          quantity of any product
     * @see Product
     */
    boolean isValidProductAndQuantity(String str);

    /**
     * Product validation
     * <p>
     * Checks the string that contains the products and their quantities for the validity of the identifier and
     * the quantity of products.
     * </p>
     *
     * @param predicate this is a functional interface.
     * @param str       a string containing the id and quantity of the product.
     * @return true, if the ID and quantity of the products are validated.
     */
    boolean isValid(Predicate<String> predicate, String str);
}
