package com.example.inventory_management.controller;

import com.example.inventory_management.model.Product;
import com.example.inventory_management.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/product")
@Tag(name = "Product API", description = "Operations related to the Product")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @PostMapping
    @Operation(summary = "Operations related to creating product")
    public ResponseEntity<Product> create(@Valid @RequestBody Product prod) {
        log.info("POST /product called");
        Product created = service.createProduct(prod);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Operations related to getting all product")
    public ResponseEntity<List<Product>> list() {
        log.info("GET /product called");
        List<Product> products = service.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Operations related to getting one product by ID")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        log.info("GET /product/{id} called");
        Product prod = service.getProductById(id);
        return ResponseEntity.ok(prod);

    }

    @PutMapping("/{id}")
    @Operation(summary = "Operations related to updating product")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product prod) {
        log.info("PUT /product/{id} called");
        Product newProd = service.updateProduct(id, prod);
        return ResponseEntity.ok(newProd);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Operations related to deleting product")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /product/{id} called");
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
