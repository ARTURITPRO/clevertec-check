package edu.clevertec.check.service.impl;

import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.entity.Product;
import edu.clevertec.check.service.OrderProcessingService;
import edu.clevertec.check.util.ConnectionManagerTest;
import edu.clevertec.check.validation.ProductValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Tag("OrderProcessingServiceImplTest")
@ExtendWith(MockitoExtension.class)
public class OrderProcessingServiceImplTest {

    @Mock
    private ProductValidation productValidation;
    String[] data;

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private OrderProcessingService orderProcessingService;

    @Mock
    private DiscountCardServiceImpl discountCardService;

    private  Map<Product, Integer> expectedCustomShoppingMap;
    ConnectionManagerTest connectionManagerTest;
    private DiscountCard expectedDiscountCard;

    @BeforeEach
    void initDataExpectedCustomShoppingMap (){
        data = new String[10];
        data[0] = "1-1";
        data[1] = "2-2";
        data[2] = "3-3";
        data[3] = "4-4";
        data[4] = "5-5";
        data[5] = "6-6";
        data[6] = "7-7";
        data[7] = "8-8";
        data[8] = "9-9";
        data[9] = "mastercard-11111";

        connectionManagerTest = new ConnectionManagerTest();

        orderProcessingService = new OrderProcessingServiceImpl(connectionManagerTest, productService, data,   discountCardService);

        expectedCustomShoppingMap = new HashMap<>();
        Product milk = Product.builder().id(1).name("milk").price(1.0).isPromotional(true).build();
        Product brot = Product.builder().id(2).name("brot").price(1.5).isPromotional(false).build();
        Product icecream = Product.builder().id(3).name("icecream").price(2.0).isPromotional(true).build();
        Product chocolate = Product.builder().id(4).name("chocolate").price(2.5).isPromotional(false).build();
        Product chicken = Product.builder().id(5).name("chicken").price(3.0).isPromotional(true).build();
        Product pizza = Product.builder().id(6).name("pizza").price(3.5).isPromotional(false).build();
        Product pudding = Product.builder().id(7).name("pudding").price(4.0).isPromotional(true).build();
        Product popcorn = Product.builder().id(8).name("popcorn").price(4.5).isPromotional(false).build();
        Product pie = Product.builder().id(9).name("pie").price(5.0).isPromotional(true).build();

        expectedCustomShoppingMap.put(milk,1);
        expectedCustomShoppingMap.put(brot,2);
        expectedCustomShoppingMap.put(icecream,3);
        expectedCustomShoppingMap.put(chocolate,4);
        expectedCustomShoppingMap.put(chicken,5);
        expectedCustomShoppingMap.put(pizza,6);
        expectedCustomShoppingMap.put(pudding,7);
        expectedCustomShoppingMap.put(popcorn,8);
        expectedCustomShoppingMap.put(pie,9);
        expectedDiscountCard = DiscountCard.builder().id(1).name("mastercard").discount(10).number(11111).build();


    }

    @Test
    void orderProcessingCheckHashMapProduct() {
        when(discountCardService.findByNumber(connectionManagerTest, 11111))
                .thenReturn(Optional.of(DiscountCard.builder().id(1).name("mastercard").discount(10).number(11111).build()));

        when(productService.findById(connectionManagerTest,1)).thenReturn(Optional.of(Product.builder().id(1).name("milk").price(1.0).isPromotional(true).build()));
        when(productService.findById(connectionManagerTest,2)).thenReturn(Optional.of(Product.builder().id(2).name("brot").price(1.5).isPromotional(false).build()));
        when(productService.findById(connectionManagerTest,3)).thenReturn(Optional.of(Product.builder().id(3).name("icecream").price(2.0).isPromotional(true).build()));
        when(productService.findById(connectionManagerTest,4)).thenReturn(Optional.of(Product.builder().id(4).name("chocolate").price(2.5).isPromotional(false).build()));
        when(productService.findById(connectionManagerTest,5)).thenReturn(Optional.of(Product.builder().id(5).name("chicken").price(3.0).isPromotional(true).build()));
        when(productService.findById(connectionManagerTest,6)).thenReturn(Optional.of(Product.builder().id(6).name("pizza").price(3.5).isPromotional(false).build()));
        when(productService.findById(connectionManagerTest,7)).thenReturn(Optional.of(Product.builder().id(7).name("pudding").price(4.0).isPromotional(true).build()));
        when(productService.findById(connectionManagerTest,8)).thenReturn(Optional.of(Product.builder().id(8).name("popcorn").price(4.5).isPromotional(false).build()));
        when(productService.findById(connectionManagerTest,9)).thenReturn(Optional.of(Product.builder().id(9).name("pie").price(5.0).isPromotional(true).build()));

        orderProcessingService.orderProcessing();

        Map<Product, Integer> actualCustomShoppingMap = orderProcessingService.getCustomShoppingList();

        assertEquals(expectedCustomShoppingMap,  actualCustomShoppingMap);
    }

    @Test
    void formationOfCheck() {
        StringBuilder expectedOrderFromCheck = new StringBuilder();
        expectedOrderFromCheck.append(
                        "*     The item chicken is promotional\n" +
                        "*     Its amount is more than 5\n" +
                        "*     You get a 10% discount \n" +
                        "*     The cost chicken will be:$2,7\n" +
                        "*     Instead of $3.0        \n" +
                        "5     chicken           $2,7       $13,5     \n" +
                        "1     milk              $1.0       $1        \n" +
                        "2     brot              $1.5       $3        \n" +
                        "6     pizza             $3.5       $21       \n" +
                        "*     The item pie is promotional\n" +
                        "*     Its amount is more than 5\n" +
                        "*     You get a 10% discount \n" +
                        "*     The cost pie will be:$4,5\n" +
                        "*     Instead of $5.0        \n" +
                        "9     pie               $4,5       $40,5     \n" +
                        "8     popcorn           $4.5       $36       \n" +
                        "*     The item pudding is promotional\n" +
                        "*     Its amount is more than 5\n" +
                        "*     You get a 10% discount \n" +
                        "*     The cost pudding will be:$3,6\n" +
                        "*     Instead of $4.0        \n" +
                        "7     pudding           $3,6       $25,2     \n" +
                        "4     chocolate         $2.5       $10       \n" +
                        "3     icecream          $2.0       $6        \n" +
                        "----------------------------------------\n" +
                        "#\t  DiscountCard mastercard number 11111\n" +
                        "#\t  has been provided\n" +
                        "#\t  Included 10% discount\n" +
                        "----------------------------------------\n" +
                        "####  Total cost:                 $140,58");
        when(discountCardService.findByNumber(connectionManagerTest, 11111))
                .thenReturn(Optional.of(DiscountCard.builder().id(1).name("mastercard").discount(10).number(11111).build()));

        when(productService.findById(connectionManagerTest,1)).thenReturn(Optional.of(Product.builder().id(1).name("milk").price(1.0).isPromotional(true).build()));
        when(productService.findById(connectionManagerTest,2)).thenReturn(Optional.of(Product.builder().id(2).name("brot").price(1.5).isPromotional(false).build()));
        when(productService.findById(connectionManagerTest,3)).thenReturn(Optional.of(Product.builder().id(3).name("icecream").price(2.0).isPromotional(true).build()));
        when(productService.findById(connectionManagerTest,4)).thenReturn(Optional.of(Product.builder().id(4).name("chocolate").price(2.5).isPromotional(false).build()));
        when(productService.findById(connectionManagerTest,5)).thenReturn(Optional.of(Product.builder().id(5).name("chicken").price(3.0).isPromotional(true).build()));
        when(productService.findById(connectionManagerTest,6)).thenReturn(Optional.of(Product.builder().id(6).name("pizza").price(3.5).isPromotional(false).build()));
        when(productService.findById(connectionManagerTest,7)).thenReturn(Optional.of(Product.builder().id(7).name("pudding").price(4.0).isPromotional(true).build()));
        when(productService.findById(connectionManagerTest,8)).thenReturn(Optional.of(Product.builder().id(8).name("popcorn").price(4.5).isPromotional(false).build()));
        when(productService.findById(connectionManagerTest,9)).thenReturn(Optional.of(Product.builder().id(9).name("pie").price(5.0).isPromotional(true).build()));

        orderProcessingService.orderProcessing().formationOfCheck();

        StringBuilder actualOrderFromCheck = orderProcessingService.getOrderFromCheck();
        assertEquals(expectedOrderFromCheck.toString(),  actualOrderFromCheck.toString());
    }

    @Test
    void getResultProcessedData() {
    }

    @Test
    void getCustomShoppingList() {
    }

    @Test
    void getOrderFromCheck() {
    }

    @Test
    void getTotalCost() {}

    @Test
    void setDiscountCard() {
        orderProcessingService.setDiscountCard(expectedDiscountCard);
        DiscountCard actualDiscountCard = orderProcessingService.getDiscountCard();
        assertEquals(expectedDiscountCard, actualDiscountCard);
    }
}