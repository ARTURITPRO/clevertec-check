package edu.clevertec.check.service.impl;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.repository.impl.ProductRepoImpl;
import edu.clevertec.check.service.ProductService;
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
class ProductServiceImplTest {
    private List<Product> expectedCustomShoppingList;

    @Mock
    private ProductService<Integer, Product> productService;

    @Mock
    private ConnectionManagerTest connectionManagerTest;

    @Mock
    ProductRepoImpl productRepo = new ProductRepoImpl();

    @BeforeEach
    void init() {
        connectionManagerTest = new ConnectionManagerTest();
        productService = new ProductServiceImpl(productRepo);
        expectedCustomShoppingList = new ArrayList<>();
        Product milk = Product.builder().id(1).name("milk").price(1.0).isPromotional(true).build();
        Product brot = Product.builder().id(2).name("brot").price(1.5).isPromotional(false).build();
        Product icecream = Product.builder().id(3).name("icecream").price(2.0).isPromotional(true).build();
        Product chocolate = Product.builder().id(4).name("chocolate").price(2.5).isPromotional(false).build();
        Product chicken = Product.builder().id(5).name("chicken").price(3.0).isPromotional(true).build();
        Product pizza = Product.builder().id(6).name("pizza").price(3.5).isPromotional(false).build();
        Product pudding = Product.builder().id(7).name("pudding").price(4.0).isPromotional(true).build();
        Product popcorn = Product.builder().id(8).name("popcorn").price(4.5).isPromotional(false).build();
        Product pie = Product.builder().id(9).name("pie").price(5.0).isPromotional(true).build();
        expectedCustomShoppingList.add(milk);
        expectedCustomShoppingList.add(brot);
        expectedCustomShoppingList.add(icecream);
        expectedCustomShoppingList.add(chocolate);
        expectedCustomShoppingList.add(chicken);
        expectedCustomShoppingList.add(pizza);
        expectedCustomShoppingList.add(pudding);
        expectedCustomShoppingList.add(popcorn);
        expectedCustomShoppingList.add(pie);
    }

    @Nested
    class FindAllListProduct {
        @Test
        void findAllPageSizeAndSizeListProduct() {
            when(productRepo.findAll(connectionManagerTest, 9, 1)).thenReturn(expectedCustomShoppingList);
            Collection<Product> actualListProduct = productService.findAll(connectionManagerTest, 9, 1);
            assertEquals(expectedCustomShoppingList, actualListProduct);
        }

        @Test
        void findAllSizeProduct() {
            when(productRepo.findAll(connectionManagerTest, 9)).thenReturn(expectedCustomShoppingList);
            Collection<Product> actualListProduct = productService.findAll(connectionManagerTest, 9);
            assertEquals(expectedCustomShoppingList, actualListProduct);
        }
    }

    @Nested
    class SaveProductTest {
        @Test
        void saveProduct() {
            Product expectedProduct = Product.builder().id(9).name("pie").price(5.0).isPromotional(true).build();
            when(productRepo.save(connectionManagerTest, expectedProduct)).thenReturn(expectedProduct);
            Product actualProduct = productService.save(connectionManagerTest, expectedProduct);
            assertEquals(expectedProduct, actualProduct);
        }
    }

    @Nested
    class FindByIdProductTest {
        @Test
        void findByIdProduct() {
            Product expectedProduct = expectedCustomShoppingList.get(0);
            when(productRepo.findById(connectionManagerTest, 1)).thenReturn(Optional.of(expectedProduct));
            Product actualProduct = productService.findById(connectionManagerTest, 1).get();
            assertEquals(expectedCustomShoppingList.get(0), actualProduct);
        }
    }

    @Nested
    class updateProductTest {
        @Test
        void updateProduct() {
            Product expectedProduct = Product.builder().id(9).name("pie").price(700.0).isPromotional(true).build();
            when(productRepo.update(connectionManagerTest, expectedProduct)).thenReturn(Optional.ofNullable(expectedProduct));
            Product actualProduct = productService.update(connectionManagerTest, expectedProduct).get();
            assertEquals(expectedProduct, actualProduct);
        }
    }

    @Nested
    class DeleteBuIdProductTest {
        @Test
        void deleteBuIdProduct() {
            when(productRepo.delete(connectionManagerTest, 5)).thenReturn(true);
            assertTrue(productService.delete(connectionManagerTest, 5));
        }
    }
}