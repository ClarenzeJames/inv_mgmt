package com.example.inventory_management.service;

import com.example.inventory_management.DTO.ProductPatchDTO;
import com.example.inventory_management.DTO.ProductResponseDTO;
import com.example.inventory_management.model.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    ProductResponseDTO patchProduct(Long id, ProductPatchDTO dto);
}
