package com.ecommerce.order_service.Config;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @KafkaListener(topics = "order-topic", groupId = "order-group")
    public void consume(String message) {
        // Process the incoming message
        System.out.println("Received message: " + message);
    }
}

