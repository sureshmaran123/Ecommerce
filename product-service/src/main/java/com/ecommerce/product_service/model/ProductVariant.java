package com.ecommerce.product_service.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long variantId;
    private String sku;
    @ElementCollection
    private List<ProductSize> sizes;
    private String color;

    @ManyToOne
    @JoinColumn(name = "image_id")
    @JsonSerialize(using = ProductImageSerializer.class) // Use custom serializer
    private ProductImage image_id; // Reference to ProductImage
}
