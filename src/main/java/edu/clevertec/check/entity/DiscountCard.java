package edu.clevertec.check.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;


/**
 * Class for creating discount card.
 *
 * @author Artur Malashkov
 * @version JDK 1.8
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DiscountCard implements Entity{
    /**
     * ID is used to identify the card
     */
    @NonNull Integer id;
    /**
     * Discount card name. Used to generate a check for purchases in a store.
     */
    @NonNull String name;

    /**
     * The discount depends on the type of discount card.
     */
    @NonNull Integer discount;

    /**
     * Discount card number. Used to generate a check for purchases in a store.
     */
    @NonNull Integer number;

    /**
     * @return discount card in the required format for generating a check.
     */
    @Override
    public String toString() {
        return "DiscountCard " + name +
                " number " + number;
    }
}
