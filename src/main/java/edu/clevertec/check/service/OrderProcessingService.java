package edu.clevertec.check.service;

import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.entity.Product;

import java.util.Map;

/**
 * Create the main part of the store receipt.
 *
 * @author Artur Malashkov
 * @version JDK 1.8
 */
public interface OrderProcessingService {

    /**
     * Order processing
     * <p>
     * Converts an array of strings into a set of products.
     * </p>
     *
     * @return OrderProcessingService
     */
    OrderProcessingService orderProcessing();

    /**
     * Formation of a check
     * <p>
     * Formation of the main body of the check.
     * </p>
     */
    void formationOfCheck();

    /**
     * Check details are entered here.
     *
     * @return the main body of the check
     */
    StringBuilder getOrderFromCheck();

    /**
     * This is a list of products with quantities, which was obtained by converting through.
     *
     * @return list of products with quantities.
     */
    Map<Product, Integer> getCustomShoppingList();

    /**
     * The total cost of products, taking into account promotional goods and a discount on a discount card.
     *
     * @return total cost.
     */
    double getTotalCost();

    /**
     * Pattern for validating input mastercard.
     */
    String PATTERN_MASTERCARD = "(?i)\\w{10}-\\d(?x)#Used to search for a mastercard with 5 digits";

    /**
     * Pattern for decimal format.
     */
    String PATTERN_DECIMAL_FORMAT = "#.##";

    /**
     * String format for non-promotional products
     */
    String FORMAT_STRING_FROM_CHECK_BODY_NOT_PROMOTIONAL = "%-5s %-17s %-10s %-10s\n";

    /**
     * String format for promotional products
     */
    String FORMAT_STRING_FROM_CHECK_BODY_PROMOTIONAL = "%-5s %-23s\n";

    /**
     * Used to get the final data that will be used to generate the receipt.
     *
     * @return final data.
     */
    OrderProcessingService getResultProcessedData();

    /**
     * Set type of DiscountCard.
     */
    void setDiscountCard(DiscountCard discountCard);

    /**
     * Get type of DiscountCard.
     */
    DiscountCard getDiscountCard();

    /**
     * Set a list of products with quantities.
     */
    void setCustomShoppingList(Map<Product, Integer> mapCustomShoppingList);
}
