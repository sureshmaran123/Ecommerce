package com.ecommerce.order_service.Service;
import com.ecommerce.order_service.Repository.OrderRepository;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderItem;
import com.ecommerce.order_service.model.OrderSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    private Queue<Order> pendingOrders = new ConcurrentLinkedQueue<>();

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "saveOrderFallback")
    @Retry(name = "inventoryService")
    public Order saveOrder(Order order) {
        order.setStatus("PENDING");

        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
            for (OrderSize size : item.getSizes()) {
                size.setOrderItem(item);
            }
        }

        Order savedOrder = orderRepository.save(order);

        boolean inventoryAvailable = checkInventory(savedOrder);
        if (inventoryAvailable) {
            savedOrder.setStatus("COMPLETED");
        } else {
            savedOrder.setStatus("OUT_OF_STOCK");
        }

        return orderRepository.save(savedOrder);
    }

    public Order saveOrderFallback(Order order, Exception e) {
        order.setStatus("PENDING");
        Order savedOrder = orderRepository.save(order);
        pendingOrders.offer(savedOrder);
        return savedOrder;
    }

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "checkInventoryFallback")
    @Retry(name = "inventoryService")
    private boolean checkInventory(Order order) {
        String inventoryUrl = "http://localhost:8080/inventory/check-stock";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(inventoryUrl, order, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    private boolean checkInventoryFallback(Order order, Exception e) {
        return false;
    }

    @Scheduled(fixedDelay = 30000) // Run every 30 seconds
    public void processPendingOrders() {
        Queue<Order> failedOrders = new ConcurrentLinkedQueue<>();

        while (!pendingOrders.isEmpty()) {
            Order order = pendingOrders.poll();
            try {
                boolean inventoryAvailable = checkInventory(order);
                if (inventoryAvailable) {
                    order.setStatus("COMPLETED");
                } else {
                    order.setStatus("OUT_OF_STOCK");
                }
                orderRepository.save(order);
            } catch (Exception e) {
                failedOrders.offer(order);
            }
        }

        pendingOrders.addAll(failedOrders);
    }

    @KafkaListener(topics = "inventory-topic", groupId = "order-group")
    public void consume(String message) {
        // Process the incoming message
        System.out.println("Received message from inventory-topic: " + message);
        // Add your business logic here to handle the inventory update notification
    }
}
