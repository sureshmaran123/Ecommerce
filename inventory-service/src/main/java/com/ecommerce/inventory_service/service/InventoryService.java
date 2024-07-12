package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.model.InventoryItem;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public boolean checkAndUpdateStock(String productId, String color, String size, int quantity) {
        Optional<InventoryItem> optionalItem = inventoryRepository.findByProductIdAndColor(productId, color);
        if (optionalItem.isPresent()) {
            InventoryItem item = optionalItem.get();
            for (InventoryItem.Size s : item.getSizes()) {
                if (s.getSize().equals(size)) {
                    if (s.getStock() >= quantity) {
                        s.setStock(s.getStock() - quantity);
                        inventoryRepository.save(item);
                        sendInventoryUpdateMessage(productId, color, size, s.getStock(), "true");
                        return true;
                    } else {
                        // Stock not sufficient
                        sendInventoryUpdateMessage(productId, color, size, s.getStock(), "false");
                        return false;
                    }
                }
            }
        }
        // Item not found or size not found
        return false;
    }

    public void addInventoryItem(InventoryItem item) {
        inventoryRepository.save(item);
    }

    private void sendInventoryUpdateMessage(String productId, String color, String size, int stock, String status) {
        String message = String.format("{\"productId\":\"%s\",\"color\":\"%s\",\"size\":\"%s\",\"stock\":%d,\"status\":\"%s\"}",
                productId, color, size, stock, status);
        kafkaTemplate.send("inventory-topic", message);
    }
}







