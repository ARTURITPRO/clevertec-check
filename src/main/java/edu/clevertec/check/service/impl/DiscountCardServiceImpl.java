package edu.clevertec.check.service.impl;

import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.repository.DiscountCardRepo;
import edu.clevertec.check.service.DiscountCardService;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.Optional;



public record DiscountCardServiceImpl(
        DiscountCardRepo<Integer, DiscountCard> entityRepo) implements DiscountCardService<Integer, DiscountCard> {

    @Override
    public Collection<DiscountCard> findAll(Integer pageSize, Integer size) {
        return entityRepo.findAll(pageSize, size);
    }

    @Override
    public Collection<DiscountCard> findAll(Integer pageSize) {
        return entityRepo.findAll(pageSize);
    }

    @SneakyThrows
    @Override
    public DiscountCard save(DiscountCard discountCard) {
        return entityRepo.save(discountCard);
    }

    @SneakyThrows
    @Override
    public Optional<DiscountCard> findById(Integer id) {
        return entityRepo.findById(id);
    }

    @Override
    public Optional<DiscountCard> findByNumber(Integer id) {
        return entityRepo.findByNumber(id);
    }

    @SneakyThrows
    @Override
    public Optional<DiscountCard> update(DiscountCard discountCard) {
        return entityRepo.update(discountCard);
    }

    @SneakyThrows
    @Override
    public boolean delete(Integer id) {
        return entityRepo.delete(id);
    }
}
