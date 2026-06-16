package com.example.inventory_management;

import com.example.inventory_management.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class InventoryManagementApplicationIntegrationTests {

    @Autowired
    private ProductService service;

    @Test
    void shouldReturnName(){
        String name = String.valueOf(service.getProductById(1L).getName());;
        assertEquals("Laptop ", name);
    }

    @Test
    void shouldBeMoreThanZero(){
        Float price = service.getProductById(1L).getPrice();
        assertTrue(price > 0);
    }

    @Test
    void contextLoads() {
    }

}
