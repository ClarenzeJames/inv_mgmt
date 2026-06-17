package com.example.inventory_management.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "inventory")
@Schema(description = "Product object")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Product ID", example = "1")
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    @Schema(description = "Product Name", example = "Laptop")
    private String name;

    @Column(nullable = false)
    @DecimalMin(value = "0", message="Price has to be minimally 0")
    @Schema(description = "Product price", example = "10.00")
    @NotNull(message = "Price cannot be null")
    private BigDecimal price;
}
