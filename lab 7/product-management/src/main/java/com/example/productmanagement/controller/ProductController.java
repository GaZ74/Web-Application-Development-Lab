package com.example.productmanagement.controller;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        long totalProducts = productService.countProducts();
        BigDecimal totalValue = productService.calculateTotalValue();
        BigDecimal averagePrice = productService.calculateAveragePrice();
        List<Product> lowStock = productService.findLowStockProducts(10);
        List<Product> recent = productService.findRecentProducts();
        Map<String, Long> categoryCounts = productService.countProductsByCategory();

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("averagePrice", averagePrice);
        model.addAttribute("lowStockProducts", lowStock);
        model.addAttribute("recentProducts", recent);
        model.addAttribute("categoryCounts", categoryCounts);
        return "dashboard";
    }
    
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // List all products
    @GetMapping
    public String listProducts(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Model model) {

            if (sortBy == null || sortBy.isBlank()) {
                sortBy = "id";
            }

            Sort sort = sortDir.equalsIgnoreCase("asc") ?
            Sort.by(sortBy).ascending() :
            Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Product> productPage;

            if (category == null || category.isBlank() || category.equalsIgnoreCase("All")) {
                productPage = productService.getAllProducts(pageable);
            }
            else {
                productPage = productService.getProductsByCategory(category, pageable);
            }

            model.addAttribute("products", productPage.getContent());
            model.addAttribute("currentPage", productPage.getNumber());
            model.addAttribute("totalPages", productPage.getTotalPages());
            model.addAttribute("size", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("category", category);
            model.addAttribute("pageUrl", "/products");
            // Dynamic categories
            model.addAttribute("categories", productService.getAllCategories());
            return "product-list";  // Returns product-list.html
    }
    
    // Show form for new product
    @GetMapping("/new")
    public String showNewForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "product-form";
    }
    
    // Show form for editing product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Product not found");
                    return "redirect:/products";
                });
    }
    
    // Save product (create or update)
    @PostMapping("/save")
    public String saveProduct(
        @Valid @ModelAttribute("product") Product product, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
            if (productService.isProductCodeTaken(product.getProductCode(), product.getId())) {
                result.rejectValue("productCode", "error.product", "Product code is already in use");
            }
            if (result.hasErrors()) {
                return "product-form";
            }
            try {
                productService.saveProduct(product);
                redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            }
        return "redirect:/products";
    }
    
    // Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/products";
    }
    
    // Search products
    @GetMapping("/search")
    public String searchProducts(
        @RequestParam("keyword") String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Model model) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage = productService.searchProducts(keyword, pageable);
            model.addAttribute("products", productPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());
            model.addAttribute("size", size);
            model.addAttribute("keyword", keyword);
            return "product-list";
    }
    @GetMapping("/advanced-search")
public String advancedSearch(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) String category,
    @RequestParam(required = false) BigDecimal minPrice,
    @RequestParam(required = false) BigDecimal maxPrice,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    Model model) {
    // Implementation
    if (name != null && name.isBlank()) name = null;
    if (category != null && category.isBlank()) category = null;
    Pageable pageable = PageRequest.of(page, size);
    Page<Product> products = productService.advancedSearch(name, category, minPrice, maxPrice, pageable);
    model.addAttribute("products", products.getContent());
    model.addAttribute("currentPage", products.getNumber());
    model.addAttribute("totalPages", products.getTotalPages());
    model.addAttribute("size", size);
    model.addAttribute("name", name);
    model.addAttribute("category", category);
    model.addAttribute("minPrice", minPrice);
    model.addAttribute("maxPrice", maxPrice);
    model.addAttribute("pageUrl", "/products/advanced-search");
    return "product-list";
    }
}
