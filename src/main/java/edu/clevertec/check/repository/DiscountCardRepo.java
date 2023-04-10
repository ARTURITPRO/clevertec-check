package edu.clevertec.check.repository;

import edu.clevertec.check.util.ConnectionManager;

import java.util.Collection;
import java.util.Optional;

public interface DiscountCardRepo<K, T> {

    Collection<T> findAll(ConnectionManager connectionManager, K pageSize);

    Collection<T> findAll(ConnectionManager connectionManager, K pageSize, K size);

    T save(ConnectionManager connectionManager, T entity);

    Optional<T> findById(ConnectionManager connectionManager, K id);

    Optional<T> update(ConnectionManager connectionManager, T entity);

    boolean delete(ConnectionManager connectionManager, K id);

    Optional<T> findByNumber(ConnectionManager connectionManager, K number);

}
