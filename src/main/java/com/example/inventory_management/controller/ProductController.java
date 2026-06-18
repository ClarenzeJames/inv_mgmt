package com.example.inventory_management.controller;

import com.example.inventory_management.DTO.ProductMapper;
import com.example.inventory_management.DTO.ProductPatchDTO;
import com.example.inventory_management.DTO.ProductRequestDTO;
import com.example.inventory_management.DTO.ProductResponseDTO;
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

    /**
     * Takes in request DTO (automapped by Jackson)
     * @param requestDTO the product object that we want to be inserted
     * @return returns the same requestDTO, if we return the actual object, then we will return the ID also.
     */
    @PostMapping
    @Operation(summary = "Operations related to creating product")
    public ResponseEntity<ProductRequestDTO> create(@Valid @RequestBody ProductRequestDTO requestDTO) {

        service.createProduct(ProductMapper.toEntity(requestDTO));
        return new ResponseEntity<>(requestDTO, HttpStatus.CREATED);
    }

    /**
     * This function gets all the products from service.getAllProducts(), streams and maps it to ProductMapper::toResponseDTO
     * and then toList()
     * @return List of responseDTOs
     */
    @GetMapping
    @Operation(summary = "Operations related to getting all product")
    public ResponseEntity<List<ProductResponseDTO>> list() {
        log.info("GET /product called");
        List<ProductResponseDTO> responseDTOs = service.getAllProducts()
                .stream()
                .map(ProductMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }
//    public ResponseEntity<List<Product>> list() {
//        log.info("GET /product called");
//        List<Product> products = service.getAllProducts();
//        return ResponseEntity.ok(products);
//    }



    /**
     * This function takes in an ID, searches the database and returns a response DTO
     * @param id id of product
     * @return responseDTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Operations related to getting one product by ID")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        log.info("GET /product/{id} called");
        ProductResponseDTO responseDTO = ProductMapper.toResponseDTO(service.getProductById(id));
        return ResponseEntity.ok(responseDTO);

    }

    /**
     * This function takes in an ID, and a product object, searches the database, updates the product record
     * and returns a response DTO
     * @param id id of product
     * @param prod product object with values inserted
     * @return responseDTO
     */
    @PutMapping("/{id}")
    @Operation(summary = "Operations related to updating product")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody Product prod) {
        log.info("PUT /product/{id} called");
        ProductResponseDTO responseUpdatedProd = ProductMapper.toResponseDTO(service.updateProduct(id, prod));
        return ResponseEntity.ok(responseUpdatedProd);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Operations related to patching a product record")
    public ResponseEntity<ProductResponseDTO> patchProduct(@PathVariable Long id, @Valid @RequestBody ProductPatchDTO dto) {
        log.info("PATCH /product/{id} called");
        return ResponseEntity.ok(service.patchProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Operations related to deleting product")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /product/{id} called");
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
