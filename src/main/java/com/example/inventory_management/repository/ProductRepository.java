package com.example.inventory_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<com.example.inventory_management.model.Product, Long> {
}
