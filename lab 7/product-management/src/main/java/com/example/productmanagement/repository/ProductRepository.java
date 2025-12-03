package com.example.productmanagement.repository;

import com.example.productmanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Spring Data JPA generates implementation automatically!
    
    // Custom query methods (derived from method names)
    Page<Product> findByCategory(String category, Pageable pageable);
    
    Page<Product> findByNameContaining(String keyword, Pageable pageable);
    
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<Product> findByCategoryOrderByPriceAsc(String category);
    
    boolean existsByProductCode(String productCode);

    Optional<Product> findByProductCode(String productCode);
    
    // All basic CRUD methods inherited from JpaRepository:
    // - findAll()
    // - findById(Long id)
    // - save(Product product)
    // - deleteById(Long id)
    // - count()
    // - existsById(Long id)
    @Query("SELECT p FROM Product p WHERE " +
    "(:name IS NULL OR p.name LIKE CONCAT('%', :name, '%')) AND " +
    "(:category IS NULL OR p.category = :category) AND " +
    "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
    "(:maxPrice IS NULL OR p.price <= :maxPrice)")
Page<Product> advancedSearch(@Param("name") String name,
                            @Param("category") String category,
                            @Param("minPrice") BigDecimal minPrice,
                            @Param("maxPrice") BigDecimal maxPrice,
                            Pageable pageable
                        );
                        
@Query("SELECT DISTINCT p.category FROM Product p")
List<String> findDistinctCategories();

Page<Product> findByCategoryContainingAndNameContainingAllIgnoreCase(String category, String name, Pageable pageable);

@Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category")
long countByCategory(@Param("category") String category);

@Query("SELECT SUM(p.price * p.quantity) FROM Product p")
BigDecimal calculateTotalValue();

@Query("SELECT AVG(p.price) FROM Product p")
BigDecimal calculateAveragePrice();

@Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
List<Product> findLowStockProducts(@Param("threshold") int threshold);

@Query("SELECT p FROM Product p ORDER BY p.id DESC")
List<Product> findRecentProductsLimited(Pageable pageable);

@Query("SELECT p.category AS category, COUNT(p) AS count FROM Product p GROUP BY p.category")
List<Object[]> countProductsByCategory();
}