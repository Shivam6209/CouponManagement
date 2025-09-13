# 🎯 Coupon Management System

A robust RESTful API for managing discount coupons in e-commerce platforms. Supports cart-wise, product-wise, and BxGy (Buy X Get Y) coupon types with comprehensive validation and error handling.

## ✨ Features

### 🎫 Coupon Types Supported
- **Cart-wise**: Percentage discount on entire cart when total exceeds threshold
- **Product-wise**: Discount on specific products
- **BxGy (Buy X Get Y)**: Complex deals with repetition limits
  - Buy specified quantities from buy_products array
  - Get free items from get_products array
  - Configurable repetition limits
  - User choice for which products get discounted

### 🔧 Core Functionality
- Complete CRUD operations for coupons
- Coupon validation and business rules
- Soft delete (deactivation) instead of hard delete
- Comprehensive error handling
- Input validation with Jakarta Validation
- Transaction management

### 🛡️ Quality Assurance
- Pure JUnit 5 unit tests
- Comprehensive test coverage
- Code quality with proper exception handling

## 🛠 Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.5.5** - Framework
- **Spring Data JPA** - ORM and data access
- **MySQL 8.0.33** - Primary database
- **H2 Database** - Testing database
- **Jakarta Validation** - Input validation
- **Jackson** - JSON processing

### Testing
- **JUnit 5** - Unit testing framework
- **Spring Boot Test** - Integration testing
- **Pure Java assertions** - Standard JUnit assertions

### Build & DevOps
- **Maven** - Dependency management and build tool
- **Spring Boot DevTools** - Development tools

## 🏗 Architecture

### Layered Architecture Pattern
```
┌─────────────────┐
│   Controller    │  ← REST API endpoints
├─────────────────┤
│    Service      │  ← Business logic
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
├── service/        # Business logic
├── repository/     # Data access layer
├── entity/         # JPA entities
├── dto/           # Data transfer objects
├── exception/     # Custom exceptions
└── Application.java
```

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Coupon Management Endpoints

#### 1. Create Coupon
```http
POST /api/coupons
```

**Request Body:**
```json
{
  "type": "cart_wise",
  "details": {
    "threshold": 100.0,
    "discount": 10.0
  }
}
```

**Response:**
```json
{
  "id": "CPN1699123456789",
  "type": "CART_WISE",
  "details": {
    "threshold": 100.0,
    "discount": 10.0
  },
  "active": true,
  "createdAt": "2025-09-12T01:57:36",
  "updatedAt": "2025-09-12T01:57:36"
}
```

#### 2. Get All Coupons
```http
GET /api/coupons
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
PUT /api/coupons/{id}
```

#### 6. Delete Coupon (Soft Delete)
```http
DELETE /api/coupons/{id}
```

#### 7. Get Applicable Coupons
```http
POST /api/coupons/applicable-coupons
```

**Request Body:**
```json
{
  "items": [
    {
      "productId": "PROD001",
      "quantity": 2,
      "price": 100.0
    }
  ]
}
```

**Response:**
```json
{
  "applicableCoupons": [
    {
      "couponId": "CPN001",
      "type": "CART_WISE",
      "discount": 20.0,
      "description": "10% off on orders over ₹200"
    }
  ]
}
```

#### 8. Apply Coupon
```http
POST /api/coupons/apply-coupon/{id}
```

**Request Body:**
```json
{
  "items": [
    {
      "productId": "PROD001",
      "quantity": 2,
      "price": 100.0
    }
  ]
}
```

**Response:**
```json
{
  "items": [
    {
      "productId": "PROD001",
      "quantity": 2,
      "price": 100.0,
      "totalDiscount": 20.0
    }
  ],
  "totalPrice": 200.0,
  "totalDiscount": 20.0,
  "finalPrice": 180.0
}
```

## 🗄 Database Setup

### MySQL Configuration
```sql
-- Create database
CREATE DATABASE coupon_management;

-- Use the database
USE coupon_management;

-- The application will automatically create tables using JPA
-- Table: coupons
-- Columns: id, coupon_code, coupon_type, details, is_active, created_at, updated_at
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
```

### Configuration Strategy

#### **Simple Configuration Approach:**
- **`application.properties`
- **`src/test/resources/application.properties`

#### **Database Setup:**
1. **Install MySQL** and create database:
   ```sql
   CREATE DATABASE coupon_management;
   ```

2. **Update credentials** in `application.properties` 

3. **For testing:** H2 database is automatically used

## 🚀 Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

### Clone Repository
```bash
git clone <repository-url>
cd couponmanagement
```

### Build Project
```bash
mvn clean compile
```

### Database Setup
1. **Install MySQL** (if not already installed)
2. **Create database:**
   ```sql
   CREATE DATABASE coupon_management;
   ```
3. **Update credentials** in `application.properties` (default: root/password)

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
curl http://localhost:8080/api/coupons

# Should return empty array: []
```

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=CouponServiceTest
```

### Test Coverage
```bash
mvn test jacoco:report
```

### Test Results
The project includes comprehensive tests:
- **CouponServiceTest**: Business logic testing
- **JsonUtilTest**: Utility class testing
- **CouponControllerTest**: REST endpoint testing

## 📝 Usage Examples

### 1. Create Cart-wise Coupon
```bash
curl -X POST http://localhost:8080/api/coupons \
  -H "Content-Type: application/json" \
  -d '{
    "type": "cart_wise",
    "details": {
      "threshold": 500.0,
      "discount": 10.0
    }
  }'
```

### 2. Create BxGy Coupon
```bash
curl -X POST http://localhost:8080/api/coupons \
  -H "Content-Type: application/json" \
  -d '{
    "type": "bxgy",
    "details": {
      "buy_products": [
        {"product_id": 1, "quantity": 3},
        {"product_id": 2, "quantity": 3}
      ],
      "get_products": [
        {"product_id": 3, "quantity": 1}
      ],
      "repition_limit": 2
    }
  }'
```

### 3. Get Applicable Coupons
```bash
curl -X POST http://localhost:8080/api/coupons/applicable-coupons \
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
  "errorCode": "COUPON_NOT_FOUND",
  "message": "Coupon not found with ID: CPN001",
  "timestamp": "2025-09-12T01:57:36"
}
```

### Custom Exceptions
- `CouponNotFoundException` - When coupon doesn't exist
- `CouponAlreadyExistsException` - When creating duplicate coupon
- `InvalidCouponException` - When coupon data is invalid

## 🔮 Future Enhancements

- Usage limits per user
- User-specific coupons
- Redis caching for performance
- Rate limiting
- API documentation with Swagger
- Containerization with Docker

## 👥 Authors

- **Developer**: Shivam Jha
- **Project**: Coupon Management

## **🛠 ECLIPSE IDE SETUP & USAGE**

### **Import Project into Eclipse**

#### **Method 1: Import as Maven Project**
1. **Open Eclipse IDE**
2. **File → Import → Maven → Existing Maven Projects**
3. **Browse to your project folder:** `D:\Self\CouponManagement`
4. **Select the `pom.xml` file**
5. **Click Finish**

### **Run Application in Eclipse**
1. **Right-click on project → Run As → Maven build...**
2. **Goals:** `spring-boot:run`
3. **Click Run**

### **Run Tests in Eclipse**
1. **Right-click on project → Run As → Maven test**
2. **Or right-click on specific test class → Run As → JUnit Test**

### **Debug Application in Eclipse**
1. **Right-click on `Application.java` → Debug As → Java Application**
2. **Set breakpoints in your code**
3. **Use Debug perspective for debugging**

---

**Set base URL:** `http://localhost:8080/api`

### **API Endpoints to Import**

#### **1. Create Coupon**
```
Method: POST
URL: http://localhost:8080/api/coupons
Headers: Content-Type: application/json
Body (raw JSON):
{
  "type": "cart_wise",
  "details": {
    "threshold": 500.0,
    "discount": 10.0
  }
}
```

#### **2. Get All Coupons**
```
Method: GET
URL: http://localhost:8080/api/coupons
```

#### **3. Get Active Coupons**
```
Method: GET
URL: http://localhost:8080/api/coupons/active
```

#### **4. Get Coupon by ID**
```
Method: GET
URL: http://localhost:8080/api/coupons/CPN001
```

#### **5. Update Coupon**
```
Method: PUT
URL: http://localhost:8080/api/coupons/CPN001
Headers: Content-Type: application/json
Body (raw JSON):
{
  "type": "cart_wise",
  "details": {
    "threshold": 600.0,
    "discount": 15.0
  }
}
```

#### **6. Delete Coupon**
```
Method: DELETE
URL: http://localhost:8080/api/coupons/CPN001
```

#### **7. Get Applicable Coupons**
```
Method: POST
URL: http://localhost:8080/api/coupons/applicable-coupons
Headers: Content-Type: application/json
Body (raw JSON):
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

#### **8. Apply Coupon**
```
Method: POST
URL: http://localhost:8080/api/coupons/apply-coupon/CPN001
Headers: Content-Type: application/json
Body (raw JSON):
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
    }
  ]
}
```