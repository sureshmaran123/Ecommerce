package com.ecommerce.order_service.model;

import java.util.List;

public class OrderRequest {
    private String productId;
    private List<Item> items;

    public static class Item {
        private String color;
        private List<Size> sizes;

        public static class Size {
            private String size;
            private int quantity;

            // Getters and setters
            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }
        }

        // Getters and setters
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

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
