package edu.clevertec.check.service;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.util.ConnectionManager;
import java.util.Collection;
import java.util.Optional;

public interface ProductService<K, T> {
    
    Collection<Product> findAll(ConnectionManager connectionManager, Integer pageSize, Integer size);

    Collection<Product> findAll(ConnectionManager connectionManager, Integer pageSize);

    T save(ConnectionManager connectionManager, T entity);

    Optional<T> findById(ConnectionManager connectionManager, K id);

    Optional<T> update(ConnectionManager connectionManager, T entity);

    boolean delete(ConnectionManager connectionManager, K id);
}
