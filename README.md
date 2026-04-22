# StoreShop

StoreShop is a Spring Boot e-commerce web application built with server-side rendering using Thymeleaf. It provides a public storefront for browsing products, user registration and ordering, plus an administrative back office for managing products, categories, users, and order status.

## Highlights

- Public catalog with search, category filtering, pagination, and product detail pages
- Customer account registration and authenticated shopping flow
- Session-based cart management
- Checkout and order history pages
- Admin dashboard with protected management routes
- Product CRUD with image upload support
- Category management
- User management with role-based access
- Order administration and status updates
- Spring Security login flow with role-based redirection
- Unit and integration test coverage with JUnit, Mockito, MockMvc, H2, and JaCoCo

## Tech Stack

- Java 17
- Spring Boot 3.2.5
- Spring MVC
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL for the main runtime database
- H2 for automated tests
- Maven
- Lombok
- Bootstrap via WebJars

## Architecture Overview

The application follows a conventional layered Spring MVC structure:

- `controllers`: request handling for public, cart, order, and admin areas
- `services`: business logic for accounts, products, categories, carts, and orders
- `repositories`: JPA persistence layer
- `entities`: domain model persisted with Hibernate
- `templates`: Thymeleaf server-rendered views
- `static`: CSS and uploaded product images
- `security`: Spring Security configuration and user details integration

## Core Functional Areas

### Public storefront

- Browse products from `/`, `/home`, or `/produits`
- Search products by keyword
- Filter products by category
- View product details
- Register a new customer account

### Cart and ordering

- Add products to cart
- Update or remove cart items
- Clear the cart
- Checkout an order
- View order history

### Administration

- Access admin back office through `/admin/**`
- Manage products and uploaded images
- Manage categories
- Manage users and roles
- Review and update order status

## Security Model

Spring Security is configured with a custom login page and role-based authorization.

- Publicly accessible:
  - `/`
  - `/home`
  - `/produits/**`
  - `/register`
  - `/login`
  - static assets and uploaded images
- Admin-only:
  - `/admin/**`
- Authenticated users:
  - remaining protected routes such as cart and orders

After login:

- `ADMIN` users are redirected to `/admin/dashboard`
- non-admin users are redirected to `/`

The application also ensures a default admin account exists at startup through `StoreShopApplication`.

Default fallback admin values in code:

- username: `admin`
- password: `admin123`
- email: `admin@storeshop.local`

These can be overridden with:

- `app.admin.username`
- `app.admin.password`
- `app.admin.email`

## Project Structure

```text
src/main/java/com/storeshop
├── config
├── controllers
├── entities
├── models
├── repositories
├── security
├── services
│   └── impl
└── StoreShopApplication.java

src/main/resources
├── application.properties
├── static
│   ├── css
│   └── uploads
└── templates
    ├── admin
    ├── categorie
    ├── login
    ├── produit
    └── public
```

## Prerequisites

- Java 17+
- Maven 3.9+ or the included Maven Wrapper
- MySQL running locally

Recommended local database:

- database: `storeshop`
- username: `root`
- password: `mysql`

These values match the current `src/main/resources/application.properties`.

## Configuration

Runtime configuration is currently defined in [application.properties](/home/ayyoub/working-area/StoreShop/src/main/resources/application.properties).

Important defaults:

- server port: `8081`
- datasource: `jdbc:mysql://localhost:3306/storeshop`
- schema strategy: `spring.jpa.hibernate.ddl-auto=update`
- multipart uploads enabled up to `10MB`

### Example local setup

Create the MySQL database if it does not already exist:

```sql
CREATE DATABASE storeshop;
```

If needed, adjust these properties:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/storeshop
spring.datasource.username=root
spring.datasource.password=mysql
server.port=8081
```

## Running the Application

### With the Maven Wrapper

```bash
./mvnw spring-boot:run
```

### Build the project

```bash
./mvnw clean package
```

After startup, open:

```text
http://localhost:8081
```

## Running Tests

Run the full automated test suite:

```bash
./mvnw test
```

Test setup uses:

- H2 in-memory database for Spring Boot context tests
- Mockito for unit testing
- MockMvc for controller tests
- JaCoCo for coverage reporting

After test execution, JaCoCo output is generated under:

```text
target/site/jacoco/
```

## Image Uploads

Product images are uploaded through the admin product forms.

- Physical storage path: `src/main/resources/static/uploads/`
- Public path: `/uploads/**`

The upload directory is exposed through Spring MVC resource handling in `WebConfig`.

## Main Routes

### Public routes

- `GET /`, `GET /home`, `GET /produits`
- `GET /produits/detail`
- `GET /register`
- `POST /register`
- `GET /login`

### Cart routes

- `GET /panier`
- `POST /panier/add`
- `POST /panier/update`
- `POST /panier/remove`
- `POST /panier/clear`

### Order routes

- `GET /commandes`
- `POST /commandes/checkout`

### Admin routes

- `GET /admin/dashboard`
- `GET /admin/users`
- `GET|POST /admin/users/add`
- `GET|POST /admin/users/edit`
- `GET /admin/users/delete`
- `GET|POST /admin/produits/add`
- `GET|POST /admin/produits/edit`
- `GET /admin/produits/delete`
- `GET|POST /admin/categories/...`
- `GET /admin/commandes`
- `POST /admin/commandes/status`

## Quality Tooling

The Maven build includes:

- `jacoco-maven-plugin` for test coverage
- `sonar-maven-plugin` for SonarQube analysis
- `fmt-maven-plugin` for formatting support

Example commands:

```bash
./mvnw test
./mvnw sonar:sonar
```

## Current Notes

- The application is MySQL-oriented in runtime configuration.
- Tests are isolated from MySQL and use H2.
- Uploaded files are stored inside the project tree, which is convenient for local development but should usually be externalized for production deployments.
- A `DockerFile` exists in the repository root but is currently empty.

## Future Improvements

- Add a real production profile with environment-based configuration
- Move uploaded assets to external object storage or a mounted volume
- Add database migrations with Flyway or Liquibase
- Strengthen validation and error handling around forms and uploads
- Add API documentation or architecture diagrams if the project evolves beyond MVC pages

## License

No project license is currently defined in `pom.xml`. Add one before public distribution.
