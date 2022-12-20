package edu.clevertec.check.repository.impl;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.service.impl.ProductServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;

class ProductRepoImplTest {

    List<Product> customShoppingList;
    ProductRepoImpl productRepo;
    Connection connection;

    @BeforeEach
    void init() {
        connection = ConnectionManager.get();
        productRepo = new ProductRepoImpl();
        customShoppingList = new ArrayList<>();
        Product milk = Product.builder().id(1).name("milk").price(1.0).isPromotional(true).build();
        Product brot = Product.builder().id(2).name("brot").price(1.5).isPromotional(false).build();
        Product icecream = Product.builder().id(3).name("icecream").price(2.0).isPromotional(true).build();
        Product chocolate = Product.builder().id(4).name("chocolate").price(2.5).isPromotional(false).build();
        Product chicken = Product.builder().id(5).name("chicken").price(3.0).isPromotional(true).build();
        Product pizza = Product.builder().id(6).name("pizza").price(3.5).isPromotional(false).build();
        Product pudding = Product.builder().id(7).name("pudding").price(4.0).isPromotional(true).build();
        Product popcorn = Product.builder().id(8).name("popcorn").price(4.5).isPromotional(false).build();
        Product pie = Product.builder().id(9).name("pie").price(5.0).isPromotional(true).build();
        customShoppingList.add(milk);
        customShoppingList.add(brot);
        customShoppingList.add(icecream);
        customShoppingList.add(chocolate);
        customShoppingList.add(chicken);
        customShoppingList.add(pizza);
        customShoppingList.add(pudding);
        customShoppingList.add(popcorn);
        customShoppingList.add(pie);
    }

    @Test
    void findAll() {
        assertEquals(customShoppingList, productRepo.findAll(9, 1));
    }

    @Test
    void testFindAll() {
        assertEquals(customShoppingList, productRepo.findAll(9));
    }

    @SneakyThrows
    @Test
    void save() {
        Product coco = Product.builder().id(9).name("pie").price(5.0).isPromotional(true).build();
        assertEquals(coco, productRepo.save(coco));
    }

    @Test
    void findById() {
        assertEquals(customShoppingList.get(0), productRepo.findById(1).get());
    }

    @Test
    void update() {
        Product banana = Product.builder().id(9).name("banana").price(10.0).isPromotional(true).build();
        assertEquals(banana, productRepo.update(banana).get());
    }

    @Test
    void delete() {
        assertTrue(productRepo.delete(5));
    }

    @SneakyThrows
    @AfterEach
    void after() {
        @Cleanup PreparedStatement dropTableProduct = connection.prepareStatement(
                "DROP TABLE product;");
        dropTableProduct.executeUpdate();
        @Cleanup PreparedStatement createTableProduct = connection.prepareStatement(
                "create table product" +
                        "(" +
                        "    id          SERIAL PRIMARY KEY," +
                        "    name        varchar(25)," +
                        "    price        double precision," +
                        "    is_promotional boolean" +
                        ");");
        createTableProduct.executeUpdate();
        @Cleanup PreparedStatement insertTableProduct = connection.prepareStatement(
                "insert into product ( name, price, is_promotional ) values" +
                        "('milk', 1.0, true)," +
                        "('brot', 1.5, false)," +
                        "('icecream', 2.0, true)," +
                        "('chocolate', 2.5, false)," +
                        "('chicken', 3.0, true)," +
                        "('pizza', 3.5, false)," +
                        "('pudding', 4.0, true)," +
                        "('popcorn', 4.5, false)," +
                        "('pie', 5.0, true);");
        insertTableProduct.executeUpdate();
    }
}