package edu.clevertec.check.service.impl;

import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.repository.impl.DiscountCardRepoImpl;
import edu.clevertec.check.service.DiscountCardService;
import edu.clevertec.check.util.ConnectionManager;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiscountCardServiceImplTest {

    List<DiscountCard> listDiscountCard;
    DiscountCardService<Integer, DiscountCard> discountCardService;
    Connection connection;

    @BeforeEach
    void init() {
        connection = ConnectionManager.get();
        discountCardService = new DiscountCardServiceImpl(new DiscountCardRepoImpl());
        listDiscountCard = new ArrayList<>();
        DiscountCard mastercard1 = DiscountCard.builder().id(1).name("mastercard").discount(10).number(11111).build();
        DiscountCard mastercard2 = DiscountCard.builder().id(2).name("mastercard").discount(20).number(22222).build();
        DiscountCard mastercard3 = DiscountCard.builder().id(3).name("mastercard").discount(30).number(33333).build();
        DiscountCard mastercard4 = DiscountCard.builder().id(4).name("mastercard").discount(40).number(44444).build();
        DiscountCard mastercard5 = DiscountCard.builder().id(5).name("mastercard").discount(50).number(55555).build();
        listDiscountCard.add(mastercard1);
        listDiscountCard.add(mastercard2);
        listDiscountCard.add(mastercard3);
        listDiscountCard.add(mastercard4);
        listDiscountCard.add(mastercard5);

    }
    @Test
    void findAll() {
        assertEquals(listDiscountCard, discountCardService.findAll(5, 1));
    }

    @Test
    void testFindAll() {
        assertEquals(listDiscountCard, discountCardService.findAll(5));
    }

    @Test
    void save() {
        DiscountCard mastercard5 = DiscountCard.builder().id(6).name("mastercard").discount(60).number(66666).build();
        assertEquals(mastercard5, discountCardService.save(mastercard5));
    }

    @Test
    void findById() {
        assertEquals(listDiscountCard.get(0), discountCardService.findById(1).get());
    }

    @Test
    void findByNumber() {
        assertEquals(listDiscountCard.get(0), discountCardService.findByNumber(11111).get());
    }

    @Test
    void update() {
        DiscountCard mastercard5 = DiscountCard.builder().id(5).name("mastercard").discount(70).number(77777).build();
        assertEquals(mastercard5, discountCardService.update(mastercard5).get());
    }

    @Test
    void delete() {
        assertTrue(discountCardService.delete(3));
    }
    @SneakyThrows
    @AfterEach
    void after() {
        @Cleanup PreparedStatement dropTableProduct = connection.prepareStatement(
                "drop table discount_card;");
        dropTableProduct.executeUpdate();
        @Cleanup PreparedStatement createTableProduct = connection.prepareStatement(
                "create table discount_card" +
                        "(" +
                        "    id   SERIAL PRIMARY KEY," +
                        "    name        varchar(25)," +
                        "    discount int," +
                        "    number  int" +
                        ");");
        createTableProduct.executeUpdate();
        @Cleanup PreparedStatement insertTableProduct = connection.prepareStatement(
                "insert into discount_card (name, discount, number) values" +
                        "( 'mastercard',10, 11111)," +
                        "( 'mastercard',20, 22222)," +
                        "( 'mastercard',30, 33333)," +
                        "( 'mastercard',40, 44444)," +
                        "( 'mastercard',50, 55555)," +
                        "( 'mastercard',60, 66666)," +
                        "( 'mastercard',70, 77777)," +
                        "( 'mastercard',80, 88888)," +
                        "( 'mastercard',90, 99999);");
        insertTableProduct.executeUpdate();
    }
}