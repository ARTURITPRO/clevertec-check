package edu.clevertec.check.repository.impl;

import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.repository.DiscountCardRepo;
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
public class DiscountCardRepoImpl implements DiscountCardRepo<Integer, DiscountCard> {

    @SneakyThrows
    @Override
    public Collection<DiscountCard> findAll(ConnectionManager connectionManager, Integer pageSize) {
        Connection connection = connectionManager.get();
        return findAll(connection, pageSize);
    }

    @SneakyThrows
    public Collection<DiscountCard> findAll(Connection connection, Integer pageSize) {
        PreparedStatement stmt = connection.prepareStatement(
                "select * from discount_card ORDER BY id " + "LIMIT ?;");
        stmt.setObject(1, pageSize);
        ResultSet resultSet = stmt.executeQuery();
        List<DiscountCard> discountCards = new ArrayList<>();
        while (resultSet.next()) {
            DiscountCard discountCard = discountCardCreation(resultSet);
            log.info("The entity was found in the database: {}", discountCard);
            discountCards.add(discountCard);
        }
        return discountCards;
    }

    @SneakyThrows
    @Override
    public Collection<DiscountCard> findAll(ConnectionManager connectionManager, Integer pageSize, Integer size) {
        Connection connection = connectionManager.get();
        return findAll(connection, pageSize, size);
    }

    @SneakyThrows
    public Collection<DiscountCard> findAll(Connection connection, Integer pageSize, Integer size) {
        PreparedStatement stmt = connection.prepareStatement(
                "select * from discount_card ORDER BY id " + "OFFSET ? " + "  LIMIT ?;");
        final Integer CONST = 1;
        Integer i = pageSize * (size - CONST);
        stmt.setObject(1, i);
        stmt.setObject(2, pageSize);
        ResultSet resultSet = stmt.executeQuery();
        List<DiscountCard> discountCards = new ArrayList<>();
        while (resultSet.next()) {
            DiscountCard discountCard = discountCardCreation(resultSet);
            log.info("The entity was found in the database: {}", discountCard);
            discountCards.add(discountCard);
        }
        return discountCards;
    }

    @Override
    @SneakyThrows
    public DiscountCard save(ConnectionManager connectionManager, DiscountCard discountCard) {
        Connection connection = connectionManager.get();
        return save(connection, discountCard);
    }

    @SneakyThrows
    private DiscountCard save(Connection connection, DiscountCard discountCard) {
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO discount_card (name, discount, number) " +
                        "VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setObject(1, discountCard.getName());
        preparedStatement.setObject(2, discountCard.getDiscount());
        preparedStatement.setObject(3, discountCard.getNumber());

        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        discountCard.setId(generatedKeys.getObject("id", Integer.class));
        log.info("The discountCard is saved in the database: {}", discountCard);

        return discountCard;
    }

    @SneakyThrows
    @Override
    public Optional<DiscountCard> findById(ConnectionManager connectionManager, Integer id) {
        Connection connection = connectionManager.get();
        return Optional.ofNullable(findById(connection, id));
    }

    @SneakyThrows
    private DiscountCard findById(Connection connection, int id) {
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT id, name, discount, number " +
                        "FROM discount_card " +
                        "WHERE id = ?");

        preparedStatement.setObject(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        DiscountCard discountCard = null;

        if (resultSet.next()) {
            discountCard = discountCardCreation(resultSet);
        }
        log.info("The discountCard is find in the database: {}", discountCard);
        return discountCard;
    }

    @Override
    @SneakyThrows
    public boolean delete(ConnectionManager connectionManager, Integer id) {
        Connection connection = connectionManager.get();
        return delete(connection, id);
    }

    @SneakyThrows
    private boolean delete(Connection connection, int id) {
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM discount_card " +
                        "WHERE id = ?;"
        );
        preparedStatement.setObject(1, id);
        int i = preparedStatement.executeUpdate();
        if (i > 0) {
            log.info("The entity was deleted in the database: id: {}", id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<DiscountCard> update(ConnectionManager connectionManager, DiscountCard discountCard) {
        Connection connection = connectionManager.get();
        return Optional.ofNullable(update(connection, discountCard));
    }

    @SneakyThrows
    private DiscountCard update(Connection connection, DiscountCard discountCardUpdate) {
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE discount_card " +
                        "SET name = ?, discount = ?, number = ?" +
                        "WHERE id = ? " +
                        "RETURNING id, name, discount,  number;"
        );
        preparedStatement.setObject(1, discountCardUpdate.getName());
        preparedStatement.setObject(2, discountCardUpdate.getDiscount());
        preparedStatement.setObject(3, discountCardUpdate.getNumber());
        preparedStatement.setObject(4, discountCardUpdate.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        DiscountCard discountCard = null;
        if (resultSet.next()) {
            discountCard = discountCardCreation(resultSet);
            log.info("The entity has been updated in the database: {}", discountCard);
        }
        return discountCard;
    }

    @SneakyThrows
    @Override
    public Optional<DiscountCard> findByNumber(ConnectionManager connectionManager, Integer number) {
        Connection connection = connectionManager.get();
        return Optional.ofNullable(findByNumber(connection, number));
    }

    @SneakyThrows
    private DiscountCard findByNumber(Connection connection, int number) {
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT id, name, discount, number " +
                        "FROM discount_card " +
                        "WHERE number = ?");

        preparedStatement.setObject(1, number);
        ResultSet resultSet = preparedStatement.executeQuery();
        DiscountCard discountCard = null;

        if (resultSet.next()) {
            discountCard = discountCardCreation(resultSet);
        }
        return discountCard;
    }

    @SneakyThrows
    private static DiscountCard discountCardCreation(ResultSet resultSet) {
        return DiscountCard
                .builder()
                .id(resultSet.getObject("id", Integer.class))
                .name(resultSet.getObject("name", String.class))
                .discount(resultSet.getObject("discount", Integer.class))
                .number(resultSet.getObject("number", Integer.class))
                .build();
    }
}
