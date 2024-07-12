package com.ecommerce.product_service.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ProductImageSerializer extends JsonSerializer<ProductImage> {

    @Override
    public void serialize(ProductImage value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getImageId());
    }
}
