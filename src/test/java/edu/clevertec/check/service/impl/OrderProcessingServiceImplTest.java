package edu.clevertec.check.service.impl;

import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.entity.Product;
import edu.clevertec.check.repository.impl.DiscountCardRepoImpl;
import edu.clevertec.check.repository.impl.ProductRepoImpl;
import edu.clevertec.check.service.OrderProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderProcessingServiceImplTest {
    String[] data;
    OrderProcessingService resultProcessedData;
    private  Map<Product, Integer> customShoppingList;

    @BeforeEach
    void init (){
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

        resultProcessedData = new OrderProcessingServiceImpl(new ProductServiceImpl
                (new ProductRepoImpl()), data,  new DiscountCardServiceImpl(new DiscountCardRepoImpl()));
        customShoppingList = new HashMap<>();
        Product milk = Product.builder().id(1).name("milk").price(1.0).isPromotional(true).build();
        Product brot = Product.builder().id(2).name("brot").price(1.5).isPromotional(false).build();
        Product icecream = Product.builder().id(3).name("icecream").price(2.0).isPromotional(true).build();
        Product chocolate = Product.builder().id(4).name("chocolate").price(2.5).isPromotional(false).build();
        Product chicken = Product.builder().id(5).name("chicken").price(3.0).isPromotional(true).build();
        Product pizza = Product.builder().id(6).name("pizza").price(3.5).isPromotional(false).build();
        Product pudding = Product.builder().id(7).name("pudding").price(4.0).isPromotional(true).build();
        Product popcorn = Product.builder().id(8).name("popcorn").price(4.5).isPromotional(false).build();
        Product pie = Product.builder().id(9).name("pie").price(5.0).isPromotional(true).build();
        customShoppingList.put(milk,1);
        customShoppingList.put(brot,2);
        customShoppingList.put(icecream,3);
        customShoppingList.put(chocolate,4);
        customShoppingList.put(chicken,5);
        customShoppingList.put(pizza,6);
        customShoppingList.put(pudding,7);
        customShoppingList.put(popcorn,8);
        customShoppingList.put(pie,9);
    }

    @Test
    void orderProcessing() {
        OrderProcessingService orderProcessingService = new OrderProcessingServiceImpl(new ProductServiceImpl
                (new ProductRepoImpl()), data,  new DiscountCardServiceImpl(new DiscountCardRepoImpl()));
        DiscountCard discountCard = DiscountCard.builder().id(1).name("mastercard").discount(10).number(11111).build();
        orderProcessingService.setDiscountCard(discountCard);
        orderProcessingService.setCustomShoppingList(customShoppingList);
        assertEquals(orderProcessingService,  resultProcessedData.orderProcessing());
    }

    @Test
    void formationOfCheck() {

    }

    @Test
    void getResultProcessedData() {
    }

    @Test
    void getCustomShoppingList() {
       assertEquals(customShoppingList,  resultProcessedData.orderProcessing().getCustomShoppingList());
    }

    @Test
    void getOrderFromCheck() {
        StringBuilder stringBuilder =  new StringBuilder();
        stringBuilder   .append("*     The item chicken is promotional\n" +
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
        resultProcessedData.orderProcessing().formationOfCheck();
        assertEquals(stringBuilder.toString() ,resultProcessedData.getOrderFromCheck().toString());

    }

    @Test
    void getTotalCost() {
        double totalCost = 140.58;
        resultProcessedData.orderProcessing().formationOfCheck();
        MathContext context = new MathContext(5, RoundingMode.HALF_UP);
        double value =  resultProcessedData.getTotalCost();
        BigDecimal result = new BigDecimal(value, context);
        String totalCostString2 =  result.toString();
        String totalCostString1 = Double.toString(totalCost);
        assertEquals(totalCostString1,totalCostString2);
    }

    @Test
    void setDiscountCard() {
        DiscountCard discountCard = DiscountCard.builder().id(1).name("mastercard").discount(10).number(1111).build();
        resultProcessedData.setDiscountCard(discountCard);
        assertEquals(discountCard, resultProcessedData.getDiscountCard());
    }
}