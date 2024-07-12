package com.ecommerce.product_service.service;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.model.ProductImage;
import com.ecommerce.product_service.model.ProductVariant;
import com.ecommerce.product_service.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "product-topic";

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.path2}")
    private String uploadPath2;

    private int imageCounter = 0;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public Product createProduct(Product product, List<MultipartFile> productImages) {
        List<ProductImage> images = new ArrayList<>();

        for (MultipartFile file : productImages) {
            try {
                String uniqueName = generateUniqueName();
                String newFileName = uniqueName + getFileExtension(file.getOriginalFilename());

                Path path1 = Paths.get(uploadPath + newFileName);
                Path path2 = Paths.get(uploadPath2 + newFileName);

                Files.copy(file.getInputStream(), path1, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(file.getInputStream(), path2, StandardCopyOption.REPLACE_EXISTING);

                ProductImage productImage = new ProductImage();
                productImage.setSrc("/assets/images/" + newFileName);
                productImage.setAlt(newFileName);

                images.add(productImage);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception properly
            }
        }

        product.setImages(images);

        List<ProductImage> imagess = product.getImages();
        List<ProductVariant> variants = product.getVariants();
        for (ProductVariant variant : variants) {
            if (variant.getImage_id() == null) {
                int index = variants.indexOf(variant);
                if (index < images.size()) {
                    variant.setImage_id(images.get(index));
                } else {
                    variant.setImage_id(null);
                }
            }
        }

        Product savedProduct = productRepository.save(product);
        kafkaTemplate.send(TOPIC, "Created product with ID: " + savedProduct.getId());
        return savedProduct;
    }

    private String generateUniqueName() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String sequentialNumber = String.format("%03d", getNextImageCounter());
        return timestamp + sequentialNumber;
    }

    private synchronized int getNextImageCounter() {
        return ++imageCounter;
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex);
    }

    public Product getProduct(String id) {
        return productRepository.findById(id).orElse(null);
    }
}



