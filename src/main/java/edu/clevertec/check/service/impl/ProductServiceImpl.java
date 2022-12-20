package edu.clevertec.check.service.impl;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.repository.ProductRepo;
import edu.clevertec.check.service.ProductService;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.Optional;

public record ProductServiceImpl(
        ProductRepo<Integer, Product> productRepo) implements ProductService<Integer, Product> {

    @Override
    public Collection<Product> findAll(Integer pageSize, Integer size) {
        return productRepo.findAll(pageSize, size);
    }

    @Override
    public Collection<Product> findAll(Integer pageSize) {
        return productRepo.findAll(pageSize);
    }

    @SneakyThrows
    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }

    @SneakyThrows
    @Override
    public Optional<Product> findById(Integer id) {
        return productRepo.findById(id);
    }

    @SneakyThrows
    @Override
    public Optional<Product> update(Product product) {
        return productRepo.update(product);
    }

    @SneakyThrows
    @Override
    public boolean delete(Integer id) {
        return productRepo.delete(id);
    }
}
