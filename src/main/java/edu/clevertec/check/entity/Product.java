package edu.clevertec.check.entity;

import edu.clevertec.check.service.impl.OrderProcessingServiceImpl;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

/**
 * Class for creating product.
 *
 * @author Artur Malashkov
 * @version JDK 1.8
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class Product implements Entity, Comparable<Product> {

    /**
     * Product ID. Used to compare products when searching or sorting.
     */
    @NonNull Integer id;

    /**
     * Product name. Used to generate a check for purchases in a store.
     */
    @NonNull final String name;

    /**
     * Product price. Used to generate a check for purchases in a store.
     */
    @NonNull final Double price;

    /**
     * The promotional item is used when calculating the discount for the product. If the product is promotional,
     * then the price for the product will be less than the specified one.
     */
    @NonNull final Boolean isPromotional;

    /**
     * Used when searching for products with the same ID and summing the number of products if their IDs match.
     * {@link OrderProcessingServiceImpl#orderProcessing() {@code if (customShoppingList.containsKey(product))} }
     *
     * @param product accepts the Product and compares its ID
     * @return {@docRoot Comparable#compareTo(Object)}
     */
    @Override
    public int compareTo(Product product) {
        return this.getId() - product.getId();
    }
}
