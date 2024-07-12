package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.model.InventoryItem;
import com.ecommerce.inventory_service.model.OrderRequest;
import com.ecommerce.inventory_service.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;


    @PostMapping("/check-stock")
    public ResponseEntity<Boolean> checkAndUpdateStock(@RequestBody OrderRequest orderRequest) {
        for (OrderRequest.Item item : orderRequest.getItems()) {
            for (OrderRequest.Item.Size size : item.getSizes()) {
                boolean isAvailable = inventoryService.checkAndUpdateStock(
                        orderRequest.getProductId(),
                        item.getColor(),
                        size.getSize(),
                        size.getQuantity()
                );
                if (!isAvailable) {
                    return ResponseEntity.ok(false);
                }
            }
        }
        return ResponseEntity.ok(true);
    }

    @PostMapping("/add-item")
    public ResponseEntity<Void> addInventoryItem(@RequestBody InventoryItem item) {
        inventoryService.addInventoryItem(item);
        return ResponseEntity.ok().build();
    }
}



