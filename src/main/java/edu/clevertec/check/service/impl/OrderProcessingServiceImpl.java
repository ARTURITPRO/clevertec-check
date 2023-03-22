package edu.clevertec.check.service.impl;

import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.entity.Product;
import edu.clevertec.check.exception.DiscountCardException;
import edu.clevertec.check.exception.ProductException;
import edu.clevertec.check.exception.ShoppingListException;
import edu.clevertec.check.service.DiscountCardService;
import edu.clevertec.check.service.OrderProcessingService;
import edu.clevertec.check.service.ProductService;
import edu.clevertec.check.util.ConnectionManager;
import edu.clevertec.check.validation.DiscountCardValidation;
import edu.clevertec.check.validation.ProductValidation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to create the main part of the store receipt, based on an array of strings containing
 * conditional product purchases by the customer.
 *
 * @author Artur Malashkov
 * @version JDK 1.8
 */
@Slf4j
@RequiredArgsConstructor
public class OrderProcessingServiceImpl implements OrderProcessingService {

    private final ConnectionManager connectionManager;
    private final ProductValidation productValidation;
    private final  DiscountCardValidation discountCardValidation;

    /**
     * The ProductServiceImpl constructor defines the database implementation.
     */
    private final ProductService<Integer, Product> productService;

    /**
     * An array of strings that will later be converted into a set of products
     * {@link OrderProcessingServiceImpl#customShoppingList}
     */
    private final String[] dataMass;

    /**
     * The DiscountCardServiceImpl constructor defines the database implementation.
     */
    private final DiscountCardService<Integer, DiscountCard> discountCardService;

    /**
     * The order processing is used only after string array validation.
     */
    @Getter
    private OrderProcessingService resultProcessedData;

    /**
     * This is a list of products with quantities, which was obtained by converting through
     * {@link OrderProcessingServiceImpl#customShoppingList} from an array of strings {@link OrderProcessingServiceImpl#dataMass}
     */

    @Getter
    @Setter
    private Map<Product, Integer> customShoppingList;

    /**
     * Check details are entered here.
     */
    @Getter
    private StringBuilder orderFromCheck;

    /**
     * The total cost of products, taking into account promotional goods and a discount on a discount card.
     */
    @Getter
    private double totalCost;
    /**
     * Type of Product.
     */
    private Product product;
    /**
     * Type of DiscountCard.
     */
    @Setter
    @Getter
    private DiscountCard discountCard;

    /**
     * Order Processing
     * <p>
     * This method checks the array of strings {@link OrderProcessingServiceImpl#dataMass} to see if each element is filled in
     * correctly and converts the given array of strings into a set {@link OrderProcessingServiceImpl#customShoppingList}
     * of products and quantities. This method also searches for a discount card {@link DiscountCard} and its type in
     * an array of strings.
     * Products are searched in this class  using the method {getProductFromFromDAtaBaseById(int,
     * ProductService)}.
     * Discount card are searched in this class  using the method:
     * {{@link OrderProcessingServiceImpl#getDiscountCardFromDataBaseByNumber(int, DiscountCardService)}, or
     * {{@link OrderProcessingServiceImpl#getDiscountCardFromDataBaseById(int, DiscountCardService)}.
     * </p>
     *
     * @return this, an object that will initialize such fields as: {@link OrderProcessingServiceImpl#discountCard},
     * {@link OrderProcessingServiceImpl#customShoppingList}
     * @see DiscountCard
     * @see Product
     */
    public OrderProcessingServiceImpl orderProcessing() {

        Pattern patternMastercard;
        customShoppingList = new HashMap<>();
        patternMastercard = Pattern.compile(PATTERN_MASTERCARD);

        for (String data : dataMass) {
            String str = data.trim();
            log.debug("String data :" + data);
            Matcher matcherMastercard = patternMastercard.matcher(str);
            boolean foundMasterCard = matcherMastercard.find();

            if (foundMasterCard) {
                String[] argsCard = str.split("-");
                log.debug("argsCard: " + Arrays.toString(argsCard));
                discountCardValidation.isValid(argsCard);
                String nameCard = argsCard[0];
                log.debug("nameCard: " + nameCard);
                int numberCard = Integer.parseInt(argsCard[1]);

                log.debug("numberCard: " + numberCard);
                discountCard = getDiscountCardFromDataBaseByNumber(numberCard, discountCardService);
                System.out.println("!!!!" + getDiscountCardFromDataBaseByNumber(numberCard, discountCardService));
                continue;
            }

            Predicate<String> predicate = productValidation::isValidProductAndQuantity;
            productValidation.isValid(predicate, data);

            String[] argsProduct = str.split("-");
            int idProduct = Integer.parseInt(argsProduct[0]);
            product = getProductFromDAtaBaseById(idProduct, productService);
            log.debug("argsProduct: " + Arrays.toString(argsProduct) + " \n product found in Data Base: " + product);
            int amount = Integer.parseInt(argsProduct[1]);
            if (customShoppingList.containsKey(product)) {
                Integer amountProductInList = customShoppingList.get(product) + amount;
                customShoppingList.put(product, amountProductInList);
                log.warn("Product " + product.getName() + "again contained in the purchase list.");
            } else {
                customShoppingList.put(product, amount);
            }
        }
        return this;
    }

    /**
     * Formation of a check
     * <p>
     * Formation of the main body of the check, including such indicators as: quantity, price, description, cost,
     * total cost. Based on the products ordered from the store and the type of discount card.
     * </p>
     *
     * @throws ShoppingListException if order is empty.
     */

    public void formationOfCheck() {
        if (!Objects.nonNull(customShoppingList))
            throw new ShoppingListException("Products not added to List. Check your order.");
        DecimalFormat decimalFormat = new DecimalFormat(PATTERN_DECIMAL_FORMAT);
        orderFromCheck = new StringBuilder();
        Integer amount;

        double totalCostOfPromotionalProduct;

        for (Map.Entry<Product, Integer> entry : customShoppingList.entrySet()) {
            product = entry.getKey();
            amount = entry.getValue();

            /*---------------------------------------------------------------------------------------------------*/
            //Если продукт не акционный
            if (!product.getIsPromotional()) {
                orderFromCheck.append(String.format(FORMAT_STRING_FROM_CHECK_BODY_NOT_PROMOTIONAL,
                        amount, product.getName(), "$" + product.getPrice(), "$" +
                                decimalFormat.format(product.getPrice() * amount)));
            }
            /*-------------------------------------------------------------------------------------------------------*/
            // Если продукт акционный, и его количество меньше 5
            if (product.getIsPromotional() && amount < 5) {
                orderFromCheck.append(String.format(FORMAT_STRING_FROM_CHECK_BODY_NOT_PROMOTIONAL,
                        amount, product.getName(), "$" + product.getPrice(), "$" +
                                decimalFormat.format(product.getPrice() * amount)));
            }
            /*-------------------------------------------------------------------------------------------------------*/
            //Если продукт  акционный и его количество 5 и выше
            if (product.getIsPromotional() && amount >= 5) {

                //Скидочный коэффициент на цену товара
                double discountCoefficient = 0.1d;
                double cost = product.getPrice() - product.getPrice() * discountCoefficient;
                String result_Cost = decimalFormat.format(cost);

                orderFromCheck.append(String.format(
                        FORMAT_STRING_FROM_CHECK_BODY_PROMOTIONAL, "*", "The item " +
                                product.getName() + " is promotional"
                )).append(String.format("%-5s %-23s", "*", "Its amount is more than 5\n"));

                totalCostOfPromotionalProduct = product.getPrice() * amount;
                totalCostOfPromotionalProduct -= totalCostOfPromotionalProduct / 100 * 10;


                String result = decimalFormat.format(totalCostOfPromotionalProduct);
                orderFromCheck.append(String.format(
                        FORMAT_STRING_FROM_CHECK_BODY_PROMOTIONAL, "*", "You get a 10% discount "));

                totalCost += totalCostOfPromotionalProduct;
                orderFromCheck.append(String.format(
                        FORMAT_STRING_FROM_CHECK_BODY_PROMOTIONAL, "*", "The cost " + product.getName() +
                                " will be:" + "$" + result_Cost
                ));
                orderFromCheck.append(String.format(FORMAT_STRING_FROM_CHECK_BODY_PROMOTIONAL, "*",
                        "Instead of " + "$" + product.getPrice()));
                orderFromCheck.append(String.format(FORMAT_STRING_FROM_CHECK_BODY_NOT_PROMOTIONAL,
                        amount, product.getName(), "$" + result_Cost, "$" + result));

            } else {
                totalCost += product.getPrice() * amount;

            }
        }
        /*------------------------------------------------------------------------------------------------------------*/
        if (Objects.nonNull(discountCard)) {
            orderFromCheck.append("----------------------------------------\n");
            orderFromCheck.append("#\t  ")
                    .append(discountCard)
                    .append("\n#\t  has been provided\n")
                    .append("#\t  Included " + discountCard.getDiscount() + "% discount\n");
            totalCost -= totalCost / 100 * discountCard.getDiscount();
        }
        orderFromCheck.append("----------------------------------------\n");
        orderFromCheck.append(String.format("%-5s %-27s %1s%.2f", "####", "Total cost:", "$", totalCost));
        System.out.println(orderFromCheck);
    }

    /**
     * Product search by id
     * <p>
     * Simple search for a product by id in a product set in the Data Base.
     * </p>
     *
     * @param idProduct      accepts the product id by which it searches for a product in the store database.
     * @param productService service for working with  the store database.
     * @return one product from the store list.
     * @throws ProductException if the product could not be found.
     */

    private Product getProductFromDAtaBaseById(int idProduct, ProductService<Integer, Product> productService) {
        log.debug("search product with id = " + idProduct);
        return productService.findById(connectionManager, idProduct).orElseThrow(() ->
                new ProductException(String.format("Product id= %s not found in Data Base!", idProduct)));
    }

    /**
     * Discount card search by id
     * <p>
     * Simple search for a discount card by id in a discountCard set in the database.
     * </p>
     *
     * @param idDiscountCard      accepts the discount card id by which it searches for a discount card in the
     *                            store database.
     * @param discountCardService service for working with  the store database.
     * @return one discount card from the store list.
     * @throws DiscountCardException if the discount Card could not be found.
     */
    private DiscountCard getDiscountCardFromDataBaseById(int idDiscountCard,
                                                         DiscountCardService<Integer, DiscountCard> discountCardService) {
        log.debug("search DiscountCard with id = " + idDiscountCard);
        return discountCardService.findById(connectionManager, idDiscountCard).orElseThrow(() ->
                new DiscountCardException(String.format("DiscountCard id= %s not found in Data Base!",
                        idDiscountCard)));
    }

    /**
     * Discount card search by number
     * <p>
     * Simple search for a discount card by id number a discountCard set in the database.
     * </p>
     *
     * @param numberDiscountCard  accepts the discount card number by which it searches for a discount card in the store
     *                            database.
     * @param discountCardService service for working with  the store database.
     * @return one discount card from the store list.
     * @throws DiscountCardException if the discount Card could not be found.
     */
    private DiscountCard getDiscountCardFromDataBaseByNumber(int numberDiscountCard,
                                                             DiscountCardService<Integer, DiscountCard> discountCardService) {
        log.debug("search DiscountCard with number = " + numberDiscountCard);
        System.out.println("!!!" + discountCardService.findAll(connectionManager, 10));
        return discountCardService.findByNumber(connectionManager, numberDiscountCard).orElseThrow(() ->
                new DiscountCardException(String.format("DiscountCard number= %s not found in Data Base!",
                        numberDiscountCard)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProcessingServiceImpl that = (OrderProcessingServiceImpl) o;
        return customShoppingList.equals(that.customShoppingList) && discountCard.equals(that.discountCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customShoppingList, discountCard);
    }

    public ProductService<Integer, Product> getProductService() {
        return productService;
    }
}
