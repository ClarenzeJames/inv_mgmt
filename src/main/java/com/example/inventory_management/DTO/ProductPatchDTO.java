package com.example.inventory_management.DTO;

import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductPatchDTO {
    private String name;

    @DecimalMin(value = "0", message = "Price has to minimally 0")
    private BigDecimal price;
}
