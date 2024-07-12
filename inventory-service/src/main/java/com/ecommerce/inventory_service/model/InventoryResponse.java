package com.ecommerce.inventory_service.model;

public class InventoryResponse {
    private boolean available;
    private String message;

    // Default constructor
    public InventoryResponse() {
    }

    // Constructor with parameters
    public InventoryResponse(boolean available, String message) {
        this.available = available;
        this.message = message;
    }

    // Getters and setters
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
