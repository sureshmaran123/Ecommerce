package com.ecommerce.inventory_service.Config;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryConsumer {

    @KafkaListener(topics = "inventory-topic", groupId = "inventory-group")
    public void consume(String message) {
        // Process the incoming message
        System.out.println("Received message: " + message);
    }
}
