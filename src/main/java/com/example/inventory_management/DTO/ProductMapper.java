package com.example.inventory_management.DTO;

import com.example.inventory_management.model.Product;

import java.math.RoundingMode;

public class ProductMapper {
    /**
     * Turns requestDTOs into Product Entities
     * @param dto the request DTO
     * @return Product entities
     */
    public static Product toEntity(ProductRequestDTO dto) {
//        Product prod = new Product();
//        prod.setName(dto.getName());
//        prod.setPrice(dto.getPrice().setScale(2, RoundingMode.HALF_UP));
        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice().setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    /**
     * Turns Product entities into responseDTOs
     * @param prod product object
     * @return Response DTOs
     */
    public static ProductResponseDTO toResponseDTO (Product prod) {
//        ProductResponseDTO responseDTO = new ProductResponseDTO();
//        responseDTO.setName(prod.getName());
//        responseDTO.setPrice(prod.getPrice());
        return ProductResponseDTO.builder()
                .name(prod.getName())
                .price(prod.getPrice().setScale(2, RoundingMode.HALF_UP))
                .build();
    }
}
