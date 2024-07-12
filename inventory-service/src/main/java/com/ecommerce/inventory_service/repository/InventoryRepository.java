package com.ecommerce.inventory_service.repository;

import com.ecommerce.inventory_service.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface InventoryRepository extends JpaRepository<InventoryItem, String> {
    Optional<InventoryItem> findByProductIdAndColor(String productId, String color);
}
