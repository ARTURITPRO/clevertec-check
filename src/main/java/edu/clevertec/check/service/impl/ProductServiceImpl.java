package edu.clevertec.check.service.impl;

import edu.clevertec.check.entity.Product;
import edu.clevertec.check.repository.ProductRepo;
import edu.clevertec.check.service.ProductService;
import edu.clevertec.check.util.ConnectionManager;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public  class ProductServiceImpl implements ProductService<Integer, Product> {
    private final ProductRepo<Integer, Product> productRepo;

    public ProductServiceImpl(ProductRepo<Integer, Product> productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Collection<Product> findAll(ConnectionManager connectionManager, Integer pageSize, Integer size) {
        return productRepo.findAll(connectionManager, pageSize, size);
    }

    @Override
    public Collection<Product> findAll(ConnectionManager connectionManager, Integer pageSize) {
        return productRepo.findAll(connectionManager, pageSize);
    }

    @SneakyThrows
    @Override
    public Product save(ConnectionManager connectionManager, Product product) {
        return productRepo.save(connectionManager, product);
    }

    @SneakyThrows
    @Override
    public Optional<Product> findById(ConnectionManager connectionManager, Integer id) {
        return productRepo.findById(connectionManager, id);
    }

    @SneakyThrows
    @Override
    public Optional<Product> update(ConnectionManager connectionManager, Product product) {
        return productRepo.update(connectionManager, product);
    }

    @SneakyThrows
    @Override
    public boolean delete(ConnectionManager connectionManager, Integer id) {
        return productRepo.delete(connectionManager, id);
    }

    public ProductRepo<Integer, Product> productRepo() {
        return productRepo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ProductServiceImpl) obj;
        return Objects.equals(this.productRepo, that.productRepo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productRepo);
    }

    @Override
    public String toString() {
        return "ProductServiceImpl[" +
                "productRepo=" + productRepo + ']';
    }

}
