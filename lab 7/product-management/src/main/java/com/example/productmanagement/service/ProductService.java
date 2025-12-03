package com.example.productmanagement.service;

import com.example.productmanagement.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {
    
    Page<Product> getAllProducts(Pageable pageable);

    List<Product> getAllProducts(Sort sort);

    List<Product> getAllProducts();
    
    Optional<Product> getProductById(Long id);
    
    Product saveProduct(Product product);
    
    void deleteProduct(Long id);
    
    Page<Product> searchProducts(String keyword, Pageable pageable);
    
    Page<Product> getProductsByCategory(String category, Pageable pageable);

    Page<Product> advancedSearch(String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    boolean isProductCodeTaken(String productCode, Long id);

    List<String> getAllCategories();

    Page<Product> getProducts(String category, String keyword, Pageable pageable);

    long countProducts();

    BigDecimal calculateTotalValue();

    BigDecimal calculateAveragePrice();

    List<Product> findLowStockProducts(int threshold);

    List<Product> findRecentProducts();

    Map<String, Long> countProductsByCategory();
}
