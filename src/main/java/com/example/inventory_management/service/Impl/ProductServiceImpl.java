package com.example.inventory_management.service.Impl;

import com.example.inventory_management.DTO.ProductMapper;
import com.example.inventory_management.DTO.ProductPatchDTO;
import com.example.inventory_management.DTO.ProductResponseDTO;
import com.example.inventory_management.exception.ProductNotFoundException;
import com.example.inventory_management.model.Product;
import com.example.inventory_management.repository.ProductRepository;
import com.example.inventory_management.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    @Override
    public Product createProduct(Product product) {
        if (product.getPrice() == null) {
            throw new ProductNotFoundException("Product price cannot be null");
        }
        Product prod = new Product();

        prod.setName(product.getName());
        prod.setPrice(product.getPrice().setScale(2, RoundingMode.HALF_UP));
        return repository.save(prod);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product " + id + " was not found"));
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product prod = getProductById(id);

        // Checks the incoming request body if price is null
        if (product.getPrice() == null) {
            throw new ProductNotFoundException("Product price cannot be null");
        }
        prod.setPrice(product.getPrice().setScale(2, RoundingMode.HALF_UP));
        prod.setName(product.getName());
        return repository.save(prod);
    }

    @Override
    public ProductResponseDTO patchProduct(Long id, ProductPatchDTO dto) {
        Product existing = repository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product " + id + " was not found"));

        if(dto.getName() != null) {
            existing.setName(dto.getName());
        }
        if(dto.getPrice() != null) {
            existing.setPrice(dto.getPrice().setScale(2, RoundingMode.HALF_UP));
        }
        return ProductMapper.toResponseDTO(repository.save(existing));
    }

    @Override
    public void deleteProduct(Long id) {
        Product prod = getProductById(id);
        repository.delete(prod);
    }
}
