package com.ecommerce.order_service.model;
public class OrderResponse {
    private Long orderId;
    private boolean available;

    // Default constructor
    public OrderResponse() {}

    // Parameterized constructor
    public OrderResponse(Long orderId, boolean available) {
        this.orderId = orderId;
        this.available = available;
    }

    // Getters and setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
