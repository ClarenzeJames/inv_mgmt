package com.example.inventory_management.controller;

import com.example.inventory_management.model.Product;
import com.example.inventory_management.service.ProductService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @PostMapping
    public ResponseEntity<Product> create(@Validated @RequestBody Product prod) {
        log.info("POST /product called");
        Product created = service.createProduct(prod);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> list() {
        log.info("GET /product called");
        List<Product> products = service.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Product prod = service.getProductById(id);
        return ResponseEntity.ok(prod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Validated @RequestBody Product prod) {
        Product newProd = service.updateProduct(id, prod);
        return ResponseEntity.ok(newProd);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
