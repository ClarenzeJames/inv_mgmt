package com.example.inventory_management.service.Impl;

import com.example.inventory_management.model.Product;
import com.example.inventory_management.repository.ProductRepository;
import com.example.inventory_management.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public Product getProductById(Long id) {
        return null;
    }

    @Override
    public Product updateProduct(Long id) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }
}
