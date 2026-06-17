package com.example.inventory_management;

import com.example.inventory_management.exception.ProductNotFoundException;
import com.example.inventory_management.model.Product;
import com.example.inventory_management.repository.ProductRepository;
import com.example.inventory_management.service.Impl.ProductServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryManagementApplicationTests {
    private final Product prod = new Product();
    private final Product savedProduct = new Product();


    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    @BeforeEach
    void setup() {
        prod.setName("Laptop");
        prod.setPrice(BigDecimal.valueOf(10).setScale(2, RoundingMode.HALF_UP));

        savedProduct.setName("Laptop");
        savedProduct.setPrice(BigDecimal.valueOf(10).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void shouldWorkWithSpringContext() {
        when(repository.findById(1L)).thenReturn(Optional.of(prod));

        String name = service.getProductById(1L).getName();

        assertEquals("Laptop", name);
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct() {
        when(repository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = service.createProduct(prod);
        Mockito.verify(repository, times(1)).save(any(Product.class));
    }
}
