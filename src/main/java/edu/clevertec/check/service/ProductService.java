package edu.clevertec.check.service;

import edu.clevertec.check.entity.Product;

import java.util.Collection;
import java.util.Optional;

public interface ProductService<K, T> {
    
    Collection<Product> findAll(Integer pageSize, Integer size);

    Collection<Product> findAll(Integer pageSize);

    T save(T entity);

    Optional<T> findById(K id);

    Optional<T> update(T entity);

    boolean delete(K id);
}
