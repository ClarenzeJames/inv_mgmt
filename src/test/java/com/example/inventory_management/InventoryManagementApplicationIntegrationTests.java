package com.example.inventory_management;

import com.example.inventory_management.exception.ProductNotFoundException;
import com.example.inventory_management.model.Product;
import com.example.inventory_management.service.ProductService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InventoryManagementApplicationIntegrationTests {

    @Autowired
    private ProductService service;

    @Test
    void shouldReturnName(){
        String name = String.valueOf(service.getProductById(1L).getName());
        assertEquals("Laptop", name);
    }

    @Test
    void shouldBeMoreThanZero(){
        BigDecimal price = service.getProductById(1L).getPrice();
        assertTrue(price.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void createProduct_shouldPersistAndBeRetrievableById() {
        Product newProduct = new Product();
        newProduct.setPrice(BigDecimal.valueOf(400.0));
        newProduct.setName("incomingTest");
        Product createdProduct = createAndGetProduct(newProduct);

        // Check if ID is not null
        assert createdProduct.getId() != null;

        // Check if attributes are the same as the one that is saved
        Product getCreatedProduct = service.getProductById(createdProduct.getId());
        // Checking name
        assertEquals(newProduct.getName(), getCreatedProduct.getName());
        // Checking price
        assertEquals(newProduct.getPrice(), getCreatedProduct.getPrice());
    }

    @Test
    void getProductById_shouldThrowProductNotFoundException() {
        // Check if it throws a ProductNotFoundException
        assertThrows(ProductNotFoundException.class,() ->
            service.getProductById(99999L)
        );
    }

    @Test
    void updateProduct_shouldPersistChangesToDatabase() {
        Product createProduct = new Product();
        createProduct.setPrice(BigDecimal.valueOf(1234).setScale(2, RoundingMode.HALF_UP));
        createProduct.setName("Laptop");
        Product updatedProduct = service.updateProduct(2L,createProduct);
        // Checking if updated product and created product has the same attributes
        assertEquals(createProduct.getName(), updatedProduct.getName());
        assertEquals(createProduct.getPrice(), updatedProduct.getPrice());
    }

    @Test
    void deleteProduct_shouldRemoveRecordFromDatabase() {
        //Create a new Product
        Product newProd = new Product();
        newProd.setPrice(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP));
        newProd.setName("toDelete");
        Product createdProduct = createAndGetProduct(newProd);
        // Deleting product
        service.deleteProduct(createdProduct.getId());
        // This should throw a ProductNotFoundException since we deleted it
        assertThrows(ProductNotFoundException.class,() ->
            service.getProductById(createdProduct.getId())
        );
    }

    @Test
    void createProduct_shouldThrowWhenNameIsBlank() {
        //Create a new Product
        Product newProd = new Product();
        newProd.setPrice(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP));
        newProd.setName("");
        assertThrows(ConstraintViolationException.class, () ->
            service.createProduct(newProd)
        );
    }

    @Test
    void createProduct_shouldThrowWhenPriceIsNegative() {
        // create a new Product
        Product newProd = new Product();
        newProd.setPrice(BigDecimal.valueOf(-2).setScale(2, RoundingMode.HALF_UP));
        newProd.setName("Negative");
        assertThrows(ConstraintViolationException.class, () ->
            service.createProduct(newProd)
        );
    }

    @Test
    void updateProduct_shouldThrowWhenProductNotFound() {
        // create a new Product
        Product newProd = new Product();
        newProd.setPrice(BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_UP));
        newProd.setName("UpdateTest");
        newProd.setName("newUpdatedTest");
        assertThrows(ProductNotFoundException.class, () ->
            service.updateProduct(99999L, newProd)
        );
    }

    @Test
    void deleteProduct_shouldThrowWhenProductNotFound() {
        assertThrows(ProductNotFoundException.class, () ->
            service.deleteProduct(9999L)
        );
    }

    // -------------------------------
    // ------ Utility Functions ------
    // -------------------------------
    private Product createAndGetProduct(Product newProd) {
        Product createdProduct = service.createProduct(newProd);
        return service.getProductById(createdProduct.getId());
    }

}
