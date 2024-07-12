package com.ecommerce.inventory_service.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    private String color;

    @ElementCollection
    private List<Size> sizes;

    @Embeddable
    public static class Size {
        private String size;
        private int stock;

        // Getters and setters
        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }
}
