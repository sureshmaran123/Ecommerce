package com.ecommerce.product_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    private String id; // Change type to String for alphanumeric ID
    private String title;
    private String description;
    private String type;
    private String brand;

    @ElementCollection
    @CollectionTable(name = "product_collections", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "collection")
    private List<String> collection;
    private String category;
    private double price;
    private boolean sale;
    private String discount;
    private boolean isNew;

    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private List<String> tags;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductVariant> variants;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductImage> images;

    @PrePersist
    private void generateId() {
        this.id = UUID.randomUUID().toString(); // Generate a random UUID
    }
}
