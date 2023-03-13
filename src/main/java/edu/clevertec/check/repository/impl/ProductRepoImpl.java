package edu.clevertec.check.repository.impl;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.repository.ProductRepo;
import edu.clevertec.check.util.ConnectionManager;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ProductRepoImpl implements ProductRepo<Integer, Product> {

    @SneakyThrows
    @Override
    public Collection<Product> findAll(ConnectionManager connectionManager, Integer pageSize) {
        Connection connection = connectionManager.get();
        return findAll( connection, pageSize);
    }
    @SneakyThrows
    public Collection<Product> findAll(Connection connection, Integer pageSize) {
                PreparedStatement stmt = connection.prepareStatement(
                "select * from product ORDER BY id " + "LIMIT ?;");
        stmt.setObject(1, pageSize);
        ResultSet resultSet = stmt.executeQuery();
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            Product product = productCreation(resultSet);
            log.info("The entity was found in the database: {}", product);
            products.add(product);
        }
        return products;
    }

    @SneakyThrows
    @Override
    public Collection<Product> findAll(ConnectionManager connectionManager, Integer pageSize, Integer size) {
        Connection connection = connectionManager.get();
        return findAll(connection,pageSize, size );
    }

    @SneakyThrows
    public Collection<Product> findAll(Connection connection, Integer pageSize, Integer size) {
        PreparedStatement stmt = connection.prepareStatement(
                "select * from product ORDER BY id " + "OFFSET ? " + "  LIMIT ?;");
        final Integer CONST = 1;
        Integer i = pageSize * (size - CONST);
        stmt.setObject(1, i);
        stmt.setObject(2, pageSize);
        ResultSet resultSet = stmt.executeQuery();
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            Product product = productCreation(resultSet);
            log.info("The entity was found in the database: {}", product);
            products.add(product);
        }
        return products;
    }
    @SneakyThrows
    @Override
    public Product save(ConnectionManager connectionManager, Product product) {
        Connection connection = connectionManager.get();
        return save(connection, product);
    }

    @SneakyThrows
    private Product save(Connection connection, Product product) {
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO product (name, price, is_promotional) " +
                        "VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setObject(1, product.getName());
        preparedStatement.setObject(2, product.getPrice());
        preparedStatement.setObject(3, product.getIsPromotional());
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        product.setId(generatedKeys.getObject("id", Integer.class));

        log.info("The product is saved in the database: {}", product);
        return product;
    }


    @SneakyThrows
    @Override
    public Optional<Product> findById(ConnectionManager connectionManager, Integer id) {
        Connection connection = connectionManager.get();
        return Optional.ofNullable(findById(connection, id));
    }

    @SneakyThrows
    private Product findById(Connection connection, int id) {
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT id, name, price, is_promotional " +
                        "FROM product " +
                        "WHERE id = ?");

        preparedStatement.setObject(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Product product = null;

        if (resultSet.next()) {
            product = productCreation(resultSet);
            log.info("The entity was found in the database: {}", product);
        }
        return product;
    }

    @Override
    public Optional<Product> update(ConnectionManager connectionManager, Product product) {
        Connection connection = connectionManager.get();
        return Optional.ofNullable(update(connection, product));
    }

    @SneakyThrows
    private Product update(Connection connection, Product productUpdate) {
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE product " +
                        "SET name = ?, price = ?, is_promotional = ? " +
                        "WHERE id = ? " +
                        "RETURNING id, name,  price, is_promotional;");
        preparedStatement.setObject(4, productUpdate.getId());
        preparedStatement.setObject(1, productUpdate.getName());
        preparedStatement.setObject(2, productUpdate.getPrice());
        preparedStatement.setObject(3, productUpdate.getIsPromotional());
        ResultSet resultSet = preparedStatement.executeQuery();
        Product product = null;
        if (resultSet.next()) {
            product = productCreation(resultSet);
            log.info("The entity has been updated in the database: {}", product);
        }
        return product;
    }

    @SneakyThrows
    @Override
    public boolean delete(ConnectionManager connectionManager, Integer id) {
        Connection connection = connectionManager.get();
        return delete(connection, id);
    }

    @SneakyThrows
    private boolean delete(Connection connection, int id) {
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM product " +
                        "WHERE id = ?;"
        );

        preparedStatement.setObject(1, id);
        int result = preparedStatement.executeUpdate();
        if (result > 0) {
            log.info("The entity was deleted in the database: id: {}", id);
            return true;
        }
        return false;
    }

    @SneakyThrows
    private static Product productCreation(ResultSet resultSet) {
        return Product
                .builder()
                .id(resultSet.getObject("id", Integer.class))
                .name(resultSet.getObject("name", String.class))
                .price(resultSet.getObject("price", Double.class))
                .isPromotional(resultSet.getObject("is_promotional", Boolean.class))
                .build();
    }
}
