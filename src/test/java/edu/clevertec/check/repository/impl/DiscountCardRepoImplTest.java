package edu.clevertec.check.repository.impl;

import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.util.ConnectionManager;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiscountCardRepoImplTest {
    List<DiscountCard> listDiscountCard;
    Connection connection;
    DiscountCardRepoImpl  discountCardRepo;
    @BeforeEach
    void init() {
        discountCardRepo = new DiscountCardRepoImpl();
        connection = ConnectionManager.get();
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
        assertEquals(listDiscountCard , discountCardRepo.findAll(connection, 5,1));
    }

    @Test
    void testFindAll() {
        assertEquals(listDiscountCard , discountCardRepo.findAll(connection, 5));
    }

    @Test
    void testFindAll1() {
        assertEquals(listDiscountCard , discountCardRepo.findAll(5));
    }

    @Test
    void testFindAll2() {
        assertEquals(listDiscountCard , discountCardRepo.findAll(5,1));
    }

    @Test
    void save() {
        DiscountCard mastercard1 = DiscountCard.builder().id(1).name("mastercard").discount(10).number(11111).build();
        assertEquals(mastercard1, discountCardRepo.save(mastercard1));
    }

    @Test
    void findById() {
        assertEquals(listDiscountCard.get(0), discountCardRepo.findById(1).get());
    }

    @Test
    void delete() {
       assertTrue(discountCardRepo.delete(4));
    }

    @Test
    void update() {
        DiscountCard mastercard5 = DiscountCard.builder().id(5).name("mastercard").discount(70).number(77777).build();
        assertEquals(mastercard5, discountCardRepo.update(mastercard5).get());
    }

    @Test
    void findByNumber() {
        assertEquals(listDiscountCard.get(0), discountCardRepo.findByNumber(11111).get());
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