package com.example.inventory_management;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Product API",
                version = "1.0",
                description = "API documentation for managing inventory"
        )
)

@SpringBootApplication
public class InventoryManagementApplication {

    public static void main(String[] args) {

        SpringApplication.run(InventoryManagementApplication.class, args);
        System.out.println("Third commit");
    }

}
