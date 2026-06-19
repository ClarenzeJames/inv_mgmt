package com.example.inventory_management;

import com.example.inventory_management.DTO.ProductPatchDTO;
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
import java.util.List;
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

        // Assertion checks
        assertEquals("Laptop", result.getName());
        assertEquals(BigDecimal.valueOf(10).setScale(2, RoundingMode.HALF_UP), result.getPrice());

        // Checks whether it only ran once
        Mockito.verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_shouldThrowWhenPriceIsNull() {
        Product noPrice = new Product();
        noPrice.setName("Laptop");

        // Assert that the method throws the exception since there was no price set.
        assertThrows(ProductNotFoundException.class, () -> service.createProduct(noPrice));

        // The repo.save should not run since there was an exception thrown.
        Mockito.verify(repository, Mockito.never()).save(any(Product.class));
    }

    @Test
    void getProductById_shouldThrow_whenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.getProductById(999L));
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {
        when(repository.findAll()).thenReturn(List.of(prod, savedProduct));

        List<Product> result = service.getAllProducts();

        assertEquals(2, result.size());

        Mockito.verify(repository, times(1)).findAll();
    }

    @Test
    void patchProduct_shouldUpdateOnlyName_whenPriceIsNull() {
        Product existing = Product.builder()
                .name("Laptop")
                .price(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP))
                .build();

        ProductPatchDTO patch = new ProductPatchDTO();
        patch.setName("NewName");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Product.class))).thenReturn(existing);

        service.patchProduct(1L, patch);

        assertEquals("NewName", existing.getName());
        assertEquals(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP), existing.getPrice());

    }

    @Test
    void deleteProduct_shouldDelete_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(prod));

        service.deleteProduct(1L);

        Mockito.verify(repository, times(1)).delete(prod);
    }
}
