package edu.clevertec.check.repository.impl;

import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.util.ConnectionManagerTest;
import lombok.SneakyThrows;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.*;
import org.postgresql.util.PSQLException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DiscountCardRepoImplTest")
class DiscountCardRepoImplTest {

    private static List<DiscountCard> listDiscountCard;

    private static ScriptRunner scriptRunner;

    private static Connection connection;

    private static Reader reader;

    private static ConnectionManagerTest connectionManagerTest = new ConnectionManagerTest();

    private static DiscountCardRepoImpl discountCardRepo = new DiscountCardRepoImpl();

    @SneakyThrows
    @BeforeAll
    static void initlistDiscountCard() {
        connection = connectionManagerTest.get();
        scriptRunner = new ScriptRunner(connection);
        reader = new BufferedReader(new FileReader("src/test/resources/db.migration/test.sql"));

        scriptRunner.runScript(reader);

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

    @Nested
    class FindAllDiscountCardRepoTest {

        @Test
        void testFindAllPageSizeAndSizeListDiscountCard() {
            assertEquals(listDiscountCard, discountCardRepo.findAll(connectionManagerTest, 5, 1));
        }

        @Test
        void testFindAllPageSizeListDiscountCard() {
            assertEquals(listDiscountCard, discountCardRepo.findAll(connectionManagerTest, 5));
        }

        @Test
        void testFindAllPageSizeNegativeNumberDiscountCardThrows() {
            assertThrows(PSQLException.class, () -> discountCardRepo.findAll(connectionManagerTest, -5));
        }
    }

    @Nested
    class SaveDiscountCardTest {

        @Test
        void saveDiscountCard() {
            DiscountCard mastercard1 = DiscountCard.builder().id(1).name("mastercard").discount(10).number(11111).build();
            assertEquals(mastercard1, discountCardRepo.save(connectionManagerTest, mastercard1));
        }

        @Test
        void saveNullDiscountCard() {
            assertThrows(NullPointerException.class, () -> discountCardRepo.save(connectionManagerTest, null));
        }
    }

    @Nested
    class FindByIdDiscountCardTest {

        @Test
        void findByIdDiscountCard() {
            assertEquals(listDiscountCard.get(0), discountCardRepo.findById(connectionManagerTest, 1).get());
        }

        @Test
        void findByIdNegativeNumberDiscountCardThrows() {
            assertThrows(NoSuchElementException.class, () -> discountCardRepo.findById(connectionManagerTest, -1).get());
        }

        @Test
        void findByIdBigValueDiscountCardThrows() {
            assertThrows(NoSuchElementException.class, () -> discountCardRepo.findById(connectionManagerTest, 100).get());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void deleteDiscountCard() {
            assertTrue(discountCardRepo.delete(connectionManagerTest, 4));
        }

        @Test
        void deleteNullDiscountCardThrows() {
            assertThrows(NullPointerException.class, () -> discountCardRepo.delete(connectionManagerTest, null));
        }

        @Test
        void deleteNullNonExistentElementDiscountCardThrows() {
            assertFalse(discountCardRepo.delete(connectionManagerTest, 100));
        }

    }

    @Nested
    class UpdateTestDiscountCard {

        @Test
        void updateDiscountCard() {
            DiscountCard mastercard5 = DiscountCard.builder().id(5).name("mastercard").discount(70).number(77777).build();
            assertEquals(mastercard5, discountCardRepo.update(connectionManagerTest, mastercard5).get());
        }

        @Test
        void updateNullDiscountCardThrows() {
            DiscountCard mastercard5 = DiscountCard.builder().id(5).name("mastercard").discount(70).number(77777).build();
            assertThrows(NullPointerException.class, () -> discountCardRepo.update(connectionManagerTest, null).get());
        }

        @Test
        void updateNegativeElementIdDiscountCardThrows() {
            DiscountCard mastercard5 = DiscountCard.builder().id(-3).name("mastercard").discount(70).number(77777).build();
            assertThrows(NoSuchElementException.class, () -> discountCardRepo.update(connectionManagerTest, mastercard5).get());
        }
    }

    @Nested
    class FindByNumberDiscountCardTest {
        @Test
        void findByNumberDiscountCard() {
            assertEquals(listDiscountCard.get(0), discountCardRepo.findByNumber(connectionManagerTest, 11111).get());
        }

        @Test
        void findByNullNumberDiscountCardThrows() {
            assertThrows(NullPointerException.class, () -> discountCardRepo.findByNumber(connectionManagerTest, null).get());
        }

        @Test
        void findByNegativeNumberDiscountCardThrows() {
            assertThrows(NoSuchElementException.class, () -> discountCardRepo.findByNumber(connectionManagerTest, -1).get());
        }

        @Test
        void findByNonExistentNumberDiscountCardThrows() {
            assertThrows(NoSuchElementException.class, () -> discountCardRepo.findByNumber(connectionManagerTest, 100).get());
        }
    }

    @SneakyThrows
    @AfterEach
    void after() {
        connection = connectionManagerTest.get();
        scriptRunner = new ScriptRunner(connection);
        reader = new BufferedReader(new FileReader("src/test/resources/db.migration/test.sql"));
        scriptRunner.runScript(reader);
    }
}