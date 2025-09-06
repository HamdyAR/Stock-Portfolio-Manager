# Stock-Portfolio-Manager
## Overview
A Java Spring Boot REST API for managing stock portfolios. Features include stock catalog management, order tracking (buy/sell), and real-time portfolio computation with MySQL database integration.

## Setup Instructions

### 1. Clone Repository

Clone the repository into your computer using the following command: 
```
git clone https://github.com/your-username/Stock-Portfolio-Manager.git
```
### 2. Configure MySQL
Create a file named local.properties in main/src/resources and add the following:
```
spring.datasource.url=jdbc:mysql://localhost:3306/stock_portfolio

# Replace "root" with your database user, if applicable
spring.datasource.username=root

# Specify your database user's password, if applicable. If your database user doesn't have a password set, delete the line below
spring.datasource.password=YOUR_MYSQL_PASSWORD
```
### 3. Database Initialization
The sample data is located in:
```
src/main/resources/data.sql
```
Note: Spring Boot will automatically execute this file at startup to insert sample records provided in the file into the tables.

## Design Considerations
### 1. Data Modelling
During the design phase, I evaluated two approaches for modelling the data:

**Three-table approach:**
- Orders table: stores all buy/sell transactions
- Portfolio table: stores the current holdings for the user
- Stocks table: stores data for each stock

**Two-table approach:**
- Orders table: stores all buy/sell transactions
- Stocks table: stores data for each stock
- Portfolio is derived dynamically by aggregating order data

**✅ Why I chose the two-table approach:**
- Avoids data redundancy
- Reduces risk of inconsistency
- Follows data normalisation principles
- Simplifies maintenance

While the three-table approach could improve performance for very large datasets, it adds complexity and potential synchronization issues. For the nature and scope of this project, the two-table approach balances simplicity, readability, and maintainability.

### 2. System Scope
**👤 Single-User System**
I decided to build the simplest version of this project as version 1, which features only a single user. This approach keeps the project manageable within the submission timeline and allows focus on core portfolio functionality rather than user management complexity. Additionally, user authentication and authorization concepts will be covered later in the course curriculum.

**Note:** Version 2 will include multi-user support, real-time stock prices, and external API integration.

### 3. Primary Key Strategy
During the design phase, I evaluated different approaches for primary keys for the Stock module:

**Options considered:**

**Stock Symbol as Primary Key:**
```java
@Id
private String stockSymbol; // "AAPL", "GOOGL"
``` 
I considered stockSymbol since it is unique fulfilling the requirement of a primary key being unique. However, this approach would tie business logic directly to the database structure.

**UUID as Primary Key: (Chosen)**
```java
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;
```

**Why I chose UUID:**

**Immutable identity**: Primary keys never change regardless of business rule changes.
**Global uniqueness**: UUIDs are unique across systems and databases.
**Separation of concerns**: Business identifiers (stock symbols) remain separate from technical identifiers.
**Performance**: Efficient for database indexing and joining operations.
**Future-proof**: Accommodates potential business logic changes without affecting data integrity.

While stock symbols are human-readable, using them as primary keys would create dependencies between business logic and database structure, making the system less maintainable.

### 4. Entity Field Decisions
During the entity design phase, I carefully selected fields that balance simplicity with functionality:

**Stock Entity Fields:**
```java
- UUID id (Primary Key)
- String symbol (Business Identifier, Unique)
- String companyName
- String exchange
- String industry
```

**Order Entity Fields:**
```java
- UUID id (Primary Key)
- UUID stock_id (Foreign Key to Stock entity)
- OrderSide side (Enum: BUY/SELL)
- Integer volume
- Double price
- LocalDateTime timestamp
```

**Rationale for field choices:**

- **Industry and exchange fields inclusion**: Enables portfolio diversification analysis and filtering capabilities (e.g., GET /stocks?industry=Technology).
- **Enum for order side**: Prevents invalid values and provides type safety compared to string-based approaches.
- **Timestamp importance**: Essential for order history tracking and potential future time-based portfolio calculations.
- **Price storage**: Fixed price approach suitable for project scope, avoiding complexity of real-time market data.


### ERD
The entity relationship diagram for my project indicating the database design is illustrated below:

![ERD](docs/erd.png)

### UML Class Diagram







