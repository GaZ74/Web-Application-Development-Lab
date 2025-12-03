# Product Management System

## Student Information
- **Name:** Dang Hoang Minh Long
- **Student ID:** ITCSIU24054
- **Class:** Web Application Development_S1_2025-26_G01_lab02

## Technologies Used
- Spring Boot 3.3.x
- Spring Data JPA
- MySQL 8.0
- Thymeleaf
- Maven

## Setup Instructions
1. Import project into VS Code
2. Create database: `product_management`
3. Update `application.properties` with your MySQL credentials
4. Run: `mvn spring-boot:run`
5. Open browser: http://localhost:8081/products

## Completed Features
- [x] CRUD operations
- [x] Search functionality
- [x] Advanced search with filters
- [x] Validation
- [x] Sorting
- [x] Pagination
- [ ] REST API (Bonus)

## Project Structure
product-management
-.mvn-wrapper-maven-wrapper.properties
-.vscode
-src
  |-main
  |   |-java-com-example-productmanagement
  |   |                          |-controller-ProductController.java
  |   |                          |-entity-Product.java
  |   |                          |-repository-ProductRepository.java
  |   |                          |-service
  |   |                          |    |-ProductService.jave
  |   |                          |    |-ProductServiceImpl.java
  |   |                          |ProductManagementApplication.java
  |   |-resources
  |   |     |-static-css
  |   |     |-templates
  |   |     |     |-dashboard.html
  |   |     |     |product-form.html
  |   |     |     |product-list.html
  |   |     |application.properties
  |-test-java-com-example-productmanagement-ProductManagementApplicationTests.java
-target
    |-classes
    |    |-com-example-productmanagement
    |    |                      |-controller-ProductController.class
    |    |                      |-entity-Product.class
    |    |                      |-repository-ProductRepository.class
    |    |                      |-service
    |    |                      |   |-ProductService.class
    |    |                      |   |-ProductServiceImpl.class
    |    |                      |ProductManagementApplication.class
    |    |-templates
    |    |     |-dashboard.html
    |    |     |product-form.html
    |    |     |product-list.html
    |    |application.properties
    |-generated-sources - annotations
    |-generated-test-sources - test-annotations
    |-maven-archiver - pom.properties
    |-maven-status - maven-compiler-plugin
    |                         |-compile - default-compile
    |                         |                  |-createdFiles.lst
    |                         |                  |-inputFiles.lst
    |                         |-testCompile - default-testCompile
    |                         |                      |-inputFiles.lst
    |                         |                      |-inputFiles.lst
    |-surefire-reports
    |         |com.example.productmanagement.ProductManagementApplicationTests.txt
    |         |TEST-com.example.productmanagement.ProductManagementApplicationTests.xml
    |-test-classes - com - example - productmanagement - ProductManagementApplicationTests.class
    |product-management-0.0.1-SNAPSHOT.jar
    |product-management-0.0.1-SNAPSHOT.jar.original
.gitattributes
.gitignore
HELP.md
mvnw
mvnw.cmd
pom.xml
README.txt
** What changed from original provided code:
- List<Product> searchProducts(@Param("name")...) changed to Page<Product> advancedSearch(@Param("name") String name,...) [Advanced search feature]
- List<Product> getAllProducts(); changed to Page<Product> getAllProducts(Pageable pageable); [Pagination feature]
- List<Product> searchProducts(String keyword); changed to Page<Product> searchProducts(String keyword, Pageable pageable); [Pagination feature]
- public List<Product> getAllProducts() changed to public Page<Product> getAllProducts(Pageable pageable) [Pagination feature]
- public List<Product> searchProducts(String keyword) changed to public Page<Product> searchProducts(String keyword, Pageable pageable) [Pagination feature]
** Known Issues:
- Missing spring-boot-starter-validation in pom.xml [FIXED]
- Duplicate product code validation (return referenced code errors) [FIXED]