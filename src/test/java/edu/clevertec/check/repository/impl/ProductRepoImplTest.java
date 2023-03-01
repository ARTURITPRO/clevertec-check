package edu.clevertec.check.repository.impl;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.util.ConnectionManagerTest;
import lombok.SneakyThrows;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ProductRepoImplTest")
class ProductRepoImplTest {

    static List<Product> customShoppingList;

    static ProductRepoImpl productRepo;

    static ScriptRunner scriptRunner;

    static Connection connection;

    static Reader reader;

    static ConnectionManagerTest connectionManagerTest = new ConnectionManagerTest();

    @BeforeAll
    static void initListProduct() {
        connection = connectionManagerTest.get();
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

    @Nested
    class FindAllListProductTest {
        @Test
        void findAllPageSizeAndSizeListProduct() {
            assertEquals(customShoppingList, productRepo.findAll(connectionManagerTest, 9, 1));
        }

        @Test
        void testFindAllPageSizeListProduct() {
            assertEquals(customShoppingList, productRepo.findAll(connectionManagerTest, 9));
        }
    }

    @Nested
    class SaveProductTest {

        @Test
        void saveProduct() {
            Product coco = Product.builder().id(9).name("pie").price(5.0).isPromotional(true).build();
            assertEquals(coco, productRepo.save(connectionManagerTest, coco));
        }

        @Test
        void saveNullProductThrows() {
            assertThrows(NullPointerException.class, () -> productRepo.save(connectionManagerTest, null));
        }

        @Test
        void saveNegativeIDProduct() {
            Product coco = Product.builder().id(-20).name("pie").price(5.0).isPromotional(true).build();
            assertEquals(coco, productRepo.save(connectionManagerTest, coco));
        }
    }

    @Nested
    class FindByIdProductTest {
        @Test
        void findByIdProduct() {
            assertEquals(customShoppingList.get(0), productRepo.findById(connectionManagerTest, 1).get());
        }

        @Test
        void findByNullIdProductThrows() {
            assertThrows(NullPointerException.class, () -> productRepo.findById(connectionManagerTest, null).get());
        }

        @Test
        void findByInvalidIdProductThrows() {
            assertThrows(NoSuchElementException.class, () -> productRepo.findById(connectionManagerTest, -1).get());
        }

        @Test
        void findByNonExistentIdProductThrows() {
            assertThrows(NoSuchElementException.class, () -> productRepo.findById(connectionManagerTest, 100).get());
        }
    }

    @Nested
    class UpdateProductTest {
        @Test
        void updateProduct() {
            Product banana = Product.builder().id(9).name("banana").price(10.0).isPromotional(true).build();
            assertEquals(banana, productRepo.update(connectionManagerTest, banana).get());
        }

        @Test
        void updateNullProductThrows() {
            assertThrows(NullPointerException.class, () -> productRepo.update(connectionManagerTest, null).get());
        }

        @Test
        void updateNegativeElementIdProductThrows() {
            Product banana = Product.builder().id(-4).name("banana").price(10.0).isPromotional(true).build();
            assertThrows(NoSuchElementException.class, () -> productRepo.update(connectionManagerTest, banana).get());
        }
    }

    @Nested
    class DeleteProductTest {
        @Test
        void deleteProductById() {
            assertTrue(productRepo.delete(connectionManagerTest, 5));
        }

        @Test
        void deleteNullProductThrows() {
            assertThrows(NullPointerException.class, () -> productRepo.delete(connectionManagerTest, null));
        }

        @Test
        void deleteNullNonExistentElementProductAssertFalse() {
            assertFalse(productRepo.delete(connectionManagerTest, 100));
        }
    }

    @SneakyThrows
    @AfterEach
    void after() {
        System.out.println(" @AfterEach");
        connection = connectionManagerTest.get();
        scriptRunner = new ScriptRunner(connection);
        reader = new BufferedReader(new FileReader("src/test/resources/db.migration/test.sql"));
        scriptRunner.runScript(reader);
    }
}