package com.example.productmanagement.service;

import java.math.BigDecimal;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public long countProducts() {
        return productRepository.count();
    }

    @Override
    public BigDecimal calculateTotalValue() {
        BigDecimal total = productRepository.calculateTotalValue();
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculateAveragePrice() {
        BigDecimal avg = productRepository.calculateAveragePrice();
        return avg != null ? avg : BigDecimal.ZERO;
    }
    
    @Override
    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findLowStockProducts(threshold);
    }

    @Override
    public List<Product> findRecentProducts() {
        Pageable limit = Pageable.ofSize(5);
        return productRepository.findRecentProductsLimited(limit);
    }

    @Override
    public Map<String, Long> countProductsByCategory() {
        List<Object[]> result = productRepository.countProductsByCategory();

        Map<String, Long> map = new HashMap<>();
        for (Object[] row : result) {
            String category = (String) row[0];
            Long count = (Long) row[1];
            map.put(category, count);
        }
        return map;
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    @Override
    public List<Product> getAllProducts(Sort sort) {
        return productRepository.findAll(sort);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }

    @Override
    public Page<Product> getProducts(String category, String keyword, Pageable pageable) {
        String filterCategory = (category == null || category.isBlank()) ? "" : category;
        String filterKeyword = (keyword == null || keyword.isBlank()) ? "" : keyword;
        return productRepository.findByCategoryContainingAndNameContainingAllIgnoreCase(filterCategory, filterKeyword, pageable);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    @Override
    public Product saveProduct(Product product) {
        // Validation logic can go here
        return productRepository.save(product);
    }
    
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContaining(keyword, pageable);
    }
    
    @Override
    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }
    @Override
    public Page<Product> advancedSearch(String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.advancedSearch(name, category, minPrice, maxPrice, pageable);
    }
    // Check if product code is already exist.
    @Override
    public boolean isProductCodeTaken(String productCode, Long id) {
        Optional<Product> existingProduct = productRepository.findByProductCode(productCode);
        if (existingProduct.isEmpty()) {
            return false; // Code not used
        }
        if (id == null) {
            return true; //create a new product
        }
        else {
            return !existingProduct.get().getId().equals(id);
        }
    }
}
