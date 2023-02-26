package edu.clevertec.check.service.impl;

import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.repository.impl.DiscountCardRepoImpl;
import edu.clevertec.check.service.DiscountCardService;
import edu.clevertec.check.util.ConnectionManagerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountCardServiceImplTest {

    private List<DiscountCard> expectedListDiscountCard;

    @Mock
    private DiscountCardService<Integer, DiscountCard> discountCardService;

    @Mock
    private ConnectionManagerTest connectionManagerTest = new ConnectionManagerTest();

    @Mock
    private DiscountCardRepoImpl discountCardRepo = new DiscountCardRepoImpl();

    @BeforeEach
    void initListDiscountCard() {
        discountCardService = new DiscountCardServiceImpl(discountCardRepo);
        expectedListDiscountCard = new ArrayList<>();
        DiscountCard mastercard1 = DiscountCard.builder().id(1).name("mastercard").discount(10).number(11111).build();
        DiscountCard mastercard2 = DiscountCard.builder().id(2).name("mastercard").discount(20).number(22222).build();
        DiscountCard mastercard3 = DiscountCard.builder().id(3).name("mastercard").discount(30).number(33333).build();
        DiscountCard mastercard4 = DiscountCard.builder().id(4).name("mastercard").discount(40).number(44444).build();
        DiscountCard mastercard5 = DiscountCard.builder().id(5).name("mastercard").discount(50).number(55555).build();
        expectedListDiscountCard.add(mastercard1);
        expectedListDiscountCard.add(mastercard2);
        expectedListDiscountCard.add(mastercard3);
        expectedListDiscountCard.add(mastercard4);
        expectedListDiscountCard.add(mastercard5);
    }

    @Nested
    class FindAllDiscountCardTest {
        @Test
        void findAllAllPageSizeAndSizeListDiscountCard() {
            when(discountCardRepo.findAll(connectionManagerTest, 5, 1)).thenReturn(expectedListDiscountCard);
            Collection<DiscountCard> actualListDiscountCard = discountCardService.findAll(connectionManagerTest, 5, 1);
            assertEquals(expectedListDiscountCard, actualListDiscountCard);
        }

        @Test
        void findAllAllPageSizeListDiscountCard() {
            when(discountCardRepo.findAll(connectionManagerTest, 5)).thenReturn(expectedListDiscountCard);
            Collection<DiscountCard> actualListDiscountCard = discountCardService.findAll(connectionManagerTest, 5);
            assertEquals(expectedListDiscountCard, actualListDiscountCard);
        }
    }

    @Nested
    class SaveDiscountCardTest {
        @Test
        void saveDiscountCard() {
            DiscountCard expectedDiscountCard = DiscountCard.builder().id(6).name("mastercard").discount(60).number(66666).build();
            when(discountCardRepo.save(connectionManagerTest, expectedDiscountCard)).thenReturn(expectedDiscountCard);
            DiscountCard actualDiscountCard = discountCardService.save(connectionManagerTest, expectedDiscountCard);
            assertEquals(expectedDiscountCard, actualDiscountCard);
        }
    }


    @Nested
    class FindByIdDiscountCardTest {
        @Test
        void findByIdDiscountCard() {
            DiscountCard expectedDiscountCard = expectedListDiscountCard.get(0);
            when(discountCardRepo.findById(connectionManagerTest, 1)).thenReturn(Optional.of(expectedDiscountCard));
            DiscountCard actualDiscountCard = discountCardService.findById(connectionManagerTest, 1).get();
            assertEquals(expectedDiscountCard, actualDiscountCard);
        }
    }

    @Nested
    class FindByNumberDiscountCardTest {
        @Test
        void findByNumberDiscountCard() {
            DiscountCard expectedDiscountCard = expectedListDiscountCard.get(0);
            when(discountCardRepo.findByNumber(connectionManagerTest, 11111)).thenReturn(Optional.of(expectedDiscountCard));
            DiscountCard actualDiscountCard = discountCardService.findByNumber(connectionManagerTest, 11111).get();
            assertEquals(expectedDiscountCard, actualDiscountCard);
        }
    }

    @Nested
    class UpdateDiscountCardTest {

        @Test
        void updateDiscountCard() {
            DiscountCard expectedDiscountCard = DiscountCard.builder().id(5).name("mastercard").discount(66).number(66666).build();
            when(discountCardRepo.update(connectionManagerTest, expectedDiscountCard)).thenReturn(Optional.of(expectedDiscountCard));
            DiscountCard actuaDiscountCard = discountCardService.update(connectionManagerTest, expectedDiscountCard).get();
            assertEquals(expectedDiscountCard, actuaDiscountCard);
        }
    }

    @Nested
    class DeleteDiscountCardTest {

        @Test
        void deleteDiscountCardById() {
            when(discountCardRepo.delete(connectionManagerTest, 3)).thenReturn(true);
            assertTrue(discountCardService.delete(connectionManagerTest, 3));
        }
    }
}