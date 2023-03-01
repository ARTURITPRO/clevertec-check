package edu.clevertec.check.service.impl;

import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.repository.DiscountCardRepo;
import edu.clevertec.check.service.DiscountCardService;
import edu.clevertec.check.util.ConnectionManager;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;


public  class DiscountCardServiceImpl implements DiscountCardService<Integer, DiscountCard> {
    private final DiscountCardRepo<Integer, DiscountCard> entityRepo;

    public DiscountCardServiceImpl(
            DiscountCardRepo<Integer, DiscountCard> entityRepo) {
        this.entityRepo = entityRepo;
    }

    @Override
    public Collection<DiscountCard> findAll(ConnectionManager connectionManager, Integer pageSize, Integer size) {
        return entityRepo.findAll(connectionManager,pageSize, size);
    }

    @Override
    public Collection<DiscountCard> findAll(ConnectionManager connectionManager,Integer pageSize) {
        return entityRepo.findAll(connectionManager,pageSize);
    }

    @SneakyThrows
    @Override
    public DiscountCard save(ConnectionManager connectionManager,DiscountCard discountCard) {
        return entityRepo.save(connectionManager, discountCard);
    }

    @SneakyThrows
    @Override
    public Optional<DiscountCard> findById(ConnectionManager connectionManager, Integer id) {
        return entityRepo.findById(connectionManager, id);
    }

    @Override
    public Optional<DiscountCard> findByNumber(ConnectionManager connectionManager, Integer id) {
        return entityRepo.findByNumber(connectionManager, id);
    }

    @SneakyThrows
    @Override
    public Optional<DiscountCard> update(ConnectionManager connectionManager, DiscountCard discountCard) {
        return entityRepo.update(connectionManager, discountCard);
    }

    @SneakyThrows
    @Override
    public boolean delete(ConnectionManager connectionManager, Integer id) {
        return entityRepo.delete(connectionManager, id);
    }

    public DiscountCardRepo<Integer, DiscountCard> entityRepo() {
        return entityRepo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DiscountCardServiceImpl) obj;
        return Objects.equals(this.entityRepo, that.entityRepo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityRepo);
    }

    @Override
    public String toString() {
        return "DiscountCardServiceImpl[" +
                "entityRepo=" + entityRepo + ']';
    }

}
