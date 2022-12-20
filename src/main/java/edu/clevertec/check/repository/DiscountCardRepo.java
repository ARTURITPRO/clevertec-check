package edu.clevertec.check.repository;

import java.util.Collection;
import java.util.Optional;

public interface DiscountCardRepo<K, T> {

    Collection<T> findAll(K pageSize);

    Collection<T> findAll(K pageSize, K size);

    T save(T entity);

    Optional<T> findById(K id);

    Optional<T> update(T entity);

    boolean delete(K id);

    Optional<T> findByNumber(K number);

}
