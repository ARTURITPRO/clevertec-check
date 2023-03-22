package edu.clevertec.check.validation.impl;

import edu.clevertec.check.exception.DiscountCardException;
import edu.clevertec.check.validation.DiscountCardValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DiscountCard validation
 * <p>
 * Used to check an array that should contain the name and number of the discount card.
 * </p>
 *
 * @author Artur Malashkov
 * @version JDK 17
 */
@Slf4j
@RequiredArgsConstructor
public class DiscountCardValidationImpl implements DiscountCardValidation {
    private final String[] argsDiscountCard;

    /**
     * DiscountCard validation
     * <p>
     * Used to check an array that should contain the name and number of the discount card. This method determines the
     * name and number of the Discount card.
     * </p>
     *
     * @return try, if  the discount card has been validated.
     * @throws DiscountCardException if the card name does not match masterCard, or if the discount card number
     *                               contains more or less than 5 digits.
     */
    @Override
    public boolean isValid() {
        String nameCard = argsDiscountCard[0];
        String numberCardStr = argsDiscountCard[1];
        boolean isValidNumber = numberCardStr.matches(PATTERN_NUMBER_CARD);
        boolean isValidName = nameCard.matches(PATTERN_NAME_CARD);
        if (!isValidName)
            throw new DiscountCardException("Card name must be mastercard, case insensitive");
        if (!isValidNumber)
            throw new DiscountCardException("Card number must be 5 digits");
        log.info("number DiscountCard is valid");
        log.info("name DiscountCard is valid");
        return true;
    }
}
