package edu.clevertec.check.service;

import edu.clevertec.check.entity.DiscountCard;

import java.util.Collection;
import java.util.Optional;

public interface DiscountCardService<K,T> {

    Collection<DiscountCard> findAll(Integer pageSize, Integer size);

    Collection<DiscountCard> findAll(Integer pageSize);

    T save(T entity);

    Optional<T> findById(K id);

    Optional<T> findByNumber(K id);
    Optional<T> update(T entity);

    boolean delete(K id);
}
