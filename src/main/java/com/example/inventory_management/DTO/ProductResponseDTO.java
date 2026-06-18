package com.example.inventory_management.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponseDTO {
    private String name;
    private BigDecimal price;
}
