package com.example.inventory_management.service.Impl;

import com.example.inventory_management.model.Product;
import com.example.inventory_management.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl {
    private final ProductRepository repository;

//    @Autowired
//    public ProductServiceImpl(ProductRepository repository) {
//        this.repository = repository;
//    }

    @Override
    public createProduct(Product product) {

    }
}
