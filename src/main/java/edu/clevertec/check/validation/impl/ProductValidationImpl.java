package edu.clevertec.check.validation.impl;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.exception.ProductException;
import edu.clevertec.check.validation.ProductValidation;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

/**
 * Product validation
 * <p>
 * Checks the Product and their quantities.
 * </p>
 *
 * @author Artur Malashkov
 * @version JDK 17
 */
@Slf4j
public class ProductValidationImpl implements ProductValidation {

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
     *                          in line args, or  if the quantity of the product exceeds the store's stock of 9999 the quantity of any product
     * @see Product
     */
    public boolean isValidProductAndQuantity(String str) {
        log.debug("isValidProductAndQuantity(String str) = " + str);
        if (!str.matches(PATTERN_IDPRODUCT_QUANTITY)) throw new ProductException("Exception!, minimal " +
                "edu.clevertec.check.dto.product index = 1, minimal edu.clevertec.check.dto.product quantity = 1");
        String[] argsProduct = str.split("-");
        int amount = Integer.parseInt(argsProduct[1]);
        if (amount <= 0) throw new ProductException("Quantity edu.clevertec.check.dto.product is < = 0");
        return true;
    }

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
    public boolean isValid(Predicate<String> predicate, String str) {
        predicate.test(str);
        return true;
    }
}
