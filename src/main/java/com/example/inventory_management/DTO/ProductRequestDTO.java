package com.example.inventory_management.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @DecimalMin(value = "0", message = "Price has to minimally be 0")
    @NotNull(message = "Price cannot be null")
    private BigDecimal price;
}
