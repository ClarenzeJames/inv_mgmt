package com.example.inventory_management;

import com.example.inventory_management.model.Product;
import com.example.inventory_management.repository.ProductRepository;
import com.example.inventory_management.service.Impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryManagementApplicationTests {
    private final Product prod = new Product();


    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    @BeforeEach
    void setup() {
        prod.setName("Laptop");
        prod.setPrice(BigDecimal.valueOf(10));
    }

    @Test
    void shouldWorkWithSpringContext() {
        when(repository.findById(1L)).thenReturn(Optional.of(prod));

        String name = service.getProductById(1L).getName();

        assertEquals("Laptop", name);
    }
}
