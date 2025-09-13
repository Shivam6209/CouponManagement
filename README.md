# 🎯 Coupon Management System

A robust, production-ready RESTful API for managing discount coupons in e-commerce platforms. Supports cart-wise, product-wise, and BxGy (Buy X Get Y) coupon types with comprehensive validation, error handling, and testing.

## ✨ Features

### 🎫 Coupon Types Supported
- **Cart-wise**: Percentage discount on entire cart when total exceeds threshold
- **Product-wise**: Fixed discount on specific products
- **BxGy (Buy X Get Y)**: Complex deals with repetition limits
  - Buy specified quantities from multiple products
  - Get free items from get_products array
  - Configurable repetition limits
  - User choice for which products get discounted

### 🔧 Core Functionality
- Complete CRUD operations for coupons
- Coupon validation and business rules enforcement
- Soft delete (deactivation) instead of hard delete
- Comprehensive error handling with custom exceptions
- Input validation with Jakarta Validation
- Transaction management with Spring Data JPA

### 🛡️ Quality Assurance
- Comprehensive JUnit 5 unit tests
- Test coverage for all business logic
- H2 in-memory database for testing
- Proper exception handling and validation

## 🛠 Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.5.5** - Framework
- **Spring Data JPA** - ORM and data access
- **MySQL 8.0.33** - Primary database
- **H2 Database** - Testing database
- **Jakarta Validation** - Input validation
- **Jackson** - JSON processing
- **Maven** - Dependency management

### Testing
- **JUnit 5** - Unit testing framework
- **Spring Boot Test** - Integration testing
- **MockMvc** - Web layer testing
- **H2 Database** - In-memory testing database

## 🏗 Architecture

### Layered Architecture Pattern
```
┌─────────────────┐
│   Controller    │  ← REST API endpoints
├─────────────────┤
│    Service      │  ← Business logic & validation
├─────────────────┤
│   Repository    │  ← Data access layer
├─────────────────┤
│    Entity       │  ← Database entities
├─────────────────┤
│      DTO        │  ← Data transfer objects
└─────────────────┘
```

### Package Structure
```
src/main/java/com/couponManagement/
├── controller/     # REST controllers
├── service/        # Business logic & interfaces
├── repository/     # Data access layer
├── entity/         # JPA entities
├── dto/           # Data transfer objects
├── exception/     # Custom exceptions
└── Application.java
```

### Entity Structure
- **Coupon**: Main entity with Long primary key
- **CartWiseCoupon**: Cart-wise discount details
- **ProductWiseCoupon**: Product-specific discount details
- **BxGyCoupon**: Buy X Get Y discount details
- **Product**: Product entity for BxGy validation

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Coupon Management Endpoints (camelCase)

#### 1. Create Coupon
```http
POST /api/createCoupon
```

**Request Body:**
```json
{
  "type": "CART_WISE",
  "cartWiseDetails": {
    "threshold": 100.0,
    "discount": 10.0
  }
}
```

**Response:**
```json
{
  "code": 0,
  "message": "Coupon created successfully",
  "result": {
    "id": 1,
    "couponCode": "CPN1757788284014272",
    "type": "CART_WISE",
    "cartWiseDetails": {
      "threshold": 100.0,
      "discount": 10.0
    },
    "active": true,
    "createdAt": "2025-09-14T00:01:20.895",
    "updatedAt": "2025-09-14T00:01:20.895"
  }
}
```

#### 2. Get All Coupons
```http
GET /api/getAllCoupons
```

#### 3. Get Active Coupons
```http
GET /api/coupons/active
```

#### 4. Get Coupon by ID
```http
GET /api/coupons/{id}
```

#### 5. Update Coupon
```http
PUT /api/updateCoupon/{id}
```

#### 6. Delete Coupon (Soft Delete)
```http
DELETE /api/deleteCoupon/{id}
```

#### 7. Get Applicable Coupons
```http
POST /api/applicableCoupons
```

**Request Body:**
```json
{
  "items": [
    {
      "productId": "PROD001",
      "quantity": 6,
      "price": 50.0
    },
    {
      "productId": "PROD002", 
      "quantity": 3,
      "price": 30.0
    },
    {
      "productId": "PROD003",
      "quantity": 2,
      "price": 25.0
    }
  ]
}
```

**Response:**
```json
{
  "code": 0,
  "message": "Applicable coupons retrieved successfully",
  "result": {
    "applicableCoupons": [
      {
        "couponId": 1,
        "type": "CART_WISE",
        "discount": 40.0,
        "description": "10% off on orders over ₹100"
      },
      {
        "couponId": 3,
        "type": "BXGY",
        "discount": 50.0,
        "description": "Buy 6 of Product X or Y, Get 2 of Product Z Free"
      }
    ]
  }
}
```

#### 8. Apply Coupon
```http
POST /api/applyCoupon/{id}
```

**Request Body:**
```json
{
  "items": [
    {
      "productId": "PROD001",
      "quantity": 6,
      "price": 50.0
    },
    {
      "productId": "PROD002",
      "quantity": 3,
      "price": 30.0
    },
    {
      "productId": "PROD003",
      "quantity": 2,
      "price": 25.0
    }
  ]
}
```

**Response:**
```json
{
  "code": 0,
  "message": "Coupon applied successfully",
  "result": {
    "items": [
      {
        "productId": "PROD001",
        "quantity": 6,
        "price": 50.0,
        "totalDiscount": 0.0,
        "totalPrice": 300.0
      },
      {
        "productId": "PROD002",
        "quantity": 3,
        "price": 30.0,
        "totalDiscount": 0.0,
        "totalPrice": 90.0
      },
      {
        "productId": "PROD003",
        "quantity": 4,
        "price": 25.0,
        "totalDiscount": 50.0,
        "totalPrice": 50.0
      }
    ],
    "totalPrice": 490.0,
    "totalDiscount": 50.0,
    "finalPrice": 440.0
  }
}
```

## 🗄 Database Setup

### MySQL Configuration
```sql
-- Create database
CREATE DATABASE coupon_management;

-- Use the database
USE coupon_management;

-- Tables will be automatically created by JPA
-- Main tables: coupons, cart_wise_coupons, product_wise_coupons, bxgy_coupons, products
```

### Application Properties
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/coupon_management
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Test Configuration (H2)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.properties.hibernate.hbm2ddl.auto=create-drop
```

## 🚀 Installation & Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

### Clone Repository
```bash
git clone <repository-url>
cd CouponManagement
```

### Database Setup
1. **Install MySQL** (if not already installed)
2. **Create database:**
   ```sql
   CREATE DATABASE coupon_management;
   ```
3. **Update credentials** in `application.properties` (default: root/password)

### Build Project
```bash
mvn clean compile
```

## ▶️ Running the Application

### Development Mode (with MySQL)
```bash
mvn spring-boot:run
```

### Testing Mode (with H2)
```bash
mvn test
# Tests automatically use H2 database
```

### Production Mode
```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Verify Application
```bash
# Check if application started
curl http://localhost:8080/api/getAllCoupons

# Should return: {"code":0,"message":"Coupons retrieved successfully","result":[]}
```

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=CouponControllerTest
mvn test -Dtest=CouponServiceTest
```

### Test Coverage
The project includes comprehensive tests:
## 📝 Usage Examples

### 1. Create Cart-wise Coupon
```bash
curl -X POST http://localhost:8080/api/createCoupon \
  -H "Content-Type: application/json" \
  -d '{
    "type": "CART_WISE",
    "cartWiseDetails": {
      "threshold": 500.0,
      "discount": 10.0
    }
  }'
```

### 2. Create Product-wise Coupon
```bash
curl -X POST http://localhost:8080/api/createCoupon \
  -H "Content-Type: application/json" \
  -d '{
    "type": "PRODUCT_WISE",
    "productWiseDetails": {
      "productId": 1,
      "discount": 20.0
    }
  }'
```

### 3. Create BxGy Coupon
```bash
curl -X POST http://localhost:8080/api/createCoupon \
  -H "Content-Type: application/json" \
  -d '{
    "type": "BXGY",
    "bxGyDetails": {
      "buyProducts": [
        {"productId": 1, "quantity": 3},
        {"productId": 2, "quantity": 3}
      ],
      "getProducts": [
        {"productId": 3, "quantity": 1}
      ],
      "repetitionLimit": 2
    }
  }'
```

### 4. Get Applicable Coupons
```bash
curl -X POST http://localhost:8080/api/applicableCoupons \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {"productId": "PROD001", "quantity": 6, "price": 50.0},
      {"productId": "PROD002", "quantity": 3, "price": 30.0},
      {"productId": "PROD003", "quantity": 2, "price": 25.0}
    ]
  }'
```

### 5. Apply Coupon
```bash
curl -X POST http://localhost:8080/api/applyCoupon/1 \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {"productId": "PROD001", "quantity": 6, "price": 50.0},
      {"productId": "PROD002", "quantity": 3, "price": 30.0},
      {"productId": "PROD003", "quantity": 2, "price": 25.0}
    ]
  }'
```

## 🚨 Error Handling

### HTTP Status Codes
- `200` - Success
- `201` - Created
- `400` - Bad Request (validation errors)
- `404` - Not Found (coupon not found)
- `409` - Conflict (coupon already exists)
- `500` - Internal Server Error

### Error Response Format
```json
{
  "code": -1,
  "message": "Coupon not found with ID: 999",
  "result": null
}
```

### Custom Exceptions
- `CouponNotFoundException` - When coupon doesn't exist
- `CouponAlreadyExistsException` - When creating duplicate coupon
- `InvalidCouponException` - When coupon data is invalid

## 🔧 Business Logic Details

### Cart-wise Coupons
- Applied when cart total exceeds threshold
- Discount calculated as percentage of cart total
- Applied to entire cart

### Product-wise Coupons
- Applied to specific products only
- Fixed discount amount per product
- Only affects matching product IDs

### BxGy Coupons
- Complex "Buy X Get Y" logic
- Supports multiple products in buy_products (OR condition)
- User can choose which products get discounted
- Repetition limit controls how many times offer can be applied
- Calculates maximum possible discount based on cart contents

## 🔮 Future Enhancements

- Usage limits per user
- User-specific coupons
- Redis caching for performance
- Rate limiting
- API documentation with Swagger
- Containerization with Docker

## 👥 Authors

- **Developer**: Shivam Jha
- **Project**: Coupon Management System
- **Version**: 1.0.0