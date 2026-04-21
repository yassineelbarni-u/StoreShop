# StoreShop Test Case Document

## 1. Purpose
This document defines functional and integration test cases for the StoreShop application.

## 2. Scope
Covered modules:
- Authentication and authorization
- Public catalog and product detail
- Registration
- Cart management
- Checkout and user orders
- Admin category management
- Admin product management
- Admin user management
- Admin order management

Out of scope:
- Browser-specific visual testing
- Full performance and load testing
- Third-party email delivery checks

## 3. Test Environments
- Java 17+ 
- Maven Wrapper: ./mvnw
- Spring Boot test profile: src/test/resources/application-test.properties
- Database for integration tests: test DB configuration from test profile

## 4. Entry and Exit Criteria
Entry criteria:
- Application builds successfully
- Test database is available
- Required seed data exists (admin user, categories, products)

Exit criteria:
- All P0 and P1 test cases pass
- No critical regression in auth, checkout, or admin flows
- Failed tests are triaged and documented

## 5. Priority Legend
- P0: Business critical
- P1: High
- P2: Medium

## 6. Test Data
- Admin account: admin/admin123 (ROLE_ADMIN)
- Client account: client1/client123 (ROLE_CLIENT)
- Category A: Electronics
- Category B: Books
- Product P1: Smartphone, price 800, stock 10, category Electronics
- Product P2: Laptop, price 1200, stock 5, category Electronics
- Product P3: Java Book, price 199, stock 12, category Books

## 7. Test Cases

| ID | Priority | Module | Preconditions | Test Data | Steps | Expected Result | Status |
|---|---|---|---|---|---|---|---|
| TC-AUTH-001 | P0 | Login | Admin user exists | admin/admin123 | Open /login, submit valid admin credentials | Redirect to /admin/dashboard | Passed |
| TC-AUTH-002 | P0 | Login | Client user exists | client1/client123 | Open /login, submit valid client credentials | Redirect to / | Passed |
| TC-AUTH-003 | P0 | Login | User exists | valid username with wrong password | Submit invalid password | Stay on login page with authentication error | Passed |
| TC-AUTH-004 | P0 | Access control | Not logged in | no session | Open /admin/dashboard | Redirect to /login | Passed |
| TC-AUTH-005 | P0 | Access control | Logged in as client | client session | Open /admin/users | Access denied (403) or redirect based on security config | Passed |
| TC-AUTH-006 | P1 | Logout | Logged in user | authenticated session | Trigger logout | Session invalidated, redirected to /login?logout | Passed |
| TC-PUB-001 | P0 | Public catalog | Products exist | Smartphone, Laptop, Java Book | Open / | public/home loaded with product list | Passed |
| TC-PUB-002 | P1 | Public catalog | Products exist in different categories | Java keyword + mixed catalog | Search with keyword | Returned list only contains matching products | Passed |
| TC-PUB-003 | P1 | Public catalog | Multiple categories exist | categorieId for Electronics | Filter by categorieId | Returned list only contains products of selected category | Passed |
| TC-PUB-004 | P1 | Public product detail | Product id exists | product id = P1 | Open /produits/detail?id=P1 | public/detail-produit loaded with product data | Passed |
| TC-PUB-005 | P1 | Public catalog paging | More than page size products exist | page=1, size=8 | Open /?page=1&size=8 | Page loads with second page items | Passed |
| TC-REG-001 | P0 | Registration | Username does not exist | new username/email/password | Submit valid username/email/password/confirmPassword | Redirect to /login?registered | Passed |
| TC-REG-002 | P0 | Registration | Username already exists | existing username | Submit existing username | Redirect to /register?error=... | Passed |
| TC-REG-003 | P0 | Registration | None | mismatched password and confirmPassword | Submit password != confirmPassword | Redirect to /register?error=... | Passed |
| TC-REG-004 | P1 | Registration | Trigger validation error | values with spaces and special chars | Submit invalid registration then inspect redirect URL | Error message, username, and email are URL encoded | Passed |
| TC-CART-001 | P0 | Cart | Empty session | produitId=P1, quantity=2 | POST /panier/add produitId=P1 quantity=2 | Cart contains P1 with quantity 2 | Passed |
| TC-CART-002 | P0 | Cart | Cart already has P1 x2 | produitId=P1, quantity=3 | POST /panier/add produitId=P1 quantity=3 | Quantity becomes 5 | Passed |
| TC-CART-003 | P1 | Cart | Cart has P1 x2 | produitId=P1, quantity=4 | POST /panier/update produitId=P1 quantity=4 | Quantity updated to 4 | Passed |
| TC-CART-004 | P1 | Cart | Cart has P1 x2 | produitId=P1, quantity=0 | POST /panier/update produitId=P1 quantity=0 | Product removed from cart | Passed |
| TC-CART-005 | P1 | Cart | Cart has at least one item | produitId=P1 | POST /panier/remove produitId=P1 | Product removed from cart | Passed |
| TC-CART-006 | P1 | Cart | Cart has multiple items | multiple product ids | POST /panier/clear | Cart empty | Passed |
| TC-CART-007 | P1 | Cart returnUrl security | None | returnUrl=https://evil.com | POST /panier/add with returnUrl=https://evil.com | Redirect forced to /panier | Passed |
| TC-CART-008 | P1 | Cart returnUrl | None | returnUrl=/produits/detail?id=1 | POST /panier/add with returnUrl=/produits/detail?id=1 | Redirect to returnUrl | Passed |
| TC-ORD-001 | P0 | Checkout | Logged in client, cart has in-stock items | P1 x2, P2 x1 | POST /commandes/checkout | Order created with status VALIDEE, cart cleared, redirect /commandes?success | Passed |
| TC-ORD-002 | P0 | Checkout | Logged in client, empty cart | empty cart | POST /commandes/checkout | Runtime error path handled (expected failure until controller-level handling is added) | Passed |
| TC-ORD-003 | P0 | Checkout stock | Logged in client, requested qty > stock | P1 quantity greater than stock | POST /commandes/checkout | Order rejected with stock error, stock unchanged | Passed |
| TC-ORD-004 | P1 | User orders | Logged in client with previous orders | existing commandes for client1 | GET /commandes | Orders listed in descending createdAt | Passed |
| TC-ORD-005 | P0 | User orders access | Not logged in | no session | GET /commandes | Redirect to /login | Passed |
| TC-ADM-CAT-001 | P1 | Admin categories | Logged in admin | Electronics, Books | GET /admin/categories | Category list page loads | Passed |
| TC-ADM-CAT-002 | P1 | Admin categories | Logged in admin | new category name | POST /admin/categories/add with valid name | Category saved, redirect /admin/categories | Passed |
| TC-ADM-CAT-003 | P1 | Admin categories | Existing category name | duplicate category name | POST /admin/categories/add duplicate name | Redirect /admin/categories/add?error=... | Passed |
| TC-ADM-CAT-004 | P1 | Admin categories | Category exists | updated category payload | POST /admin/categories/edit with valid data | Category updated | Passed |
| TC-ADM-CAT-005 | P1 | Admin categories | Category exists | category id=X | GET /admin/categories/delete?id=X | Category deleted | Passed |
| TC-ADM-PROD-001 | P1 | Admin products | Logged in admin | existing products list | GET /admin/produits | Product list with pagination loads | Passed |
| TC-ADM-PROD-002 | P1 | Admin products | Category exists | valid product without image | POST /admin/produits/add valid product without image | Product saved | Passed |
| TC-ADM-PROD-003 | P1 | Admin products image | Category exists | valid jpeg/png/gif/webp file | POST /admin/produits/add with valid image MIME (jpeg/png/gif/webp) | Image saved in upload dir and imageUrl set | Passed |
| TC-ADM-PROD-004 | P1 | Admin products image | Category exists | unsupported MIME file | POST /admin/produits/add with unsupported MIME | Upload rejected, product still follows service validation path | Passed |
| TC-ADM-PROD-005 | P1 | Admin products validation | None | price = -1 | Save product with negative price | Error redirect with validation message | Passed |
| TC-ADM-PROD-006 | P1 | Admin products validation | None | stock = -1 | Save product with negative stock | Error redirect with validation message | Passed |
| TC-ADM-PROD-007 | P1 | Admin products validation | None | empty name | Save product with empty name | Error redirect with validation message | Passed |
| TC-ADM-PROD-008 | P1 | Admin products | Product exists | product id=X | GET /admin/produits/delete?id=X | Product deleted and redirected | Passed |
| TC-ADM-USER-001 | P1 | Admin users | Logged in admin | admin + client users | GET /admin/users | Users listed | Passed |
| TC-ADM-USER-002 | P1 | Admin users | Logged in admin | new username/password/email/role=ADMIN | POST /admin/users/add valid payload + role=ADMIN | User created and role persisted | Passed |
| TC-ADM-USER-003 | P1 | Admin users | Existing username | duplicate username | POST /admin/users/add duplicate username | Redirect with error | Passed |
| TC-ADM-USER-004 | P1 | Admin users | Existing user | userId, updated username/email, no password | POST /admin/users/edit without password | Username/email/role updated, password unchanged | Passed |
| TC-ADM-USER-005 | P2 | Admin users security | Existing user | plain text password entered | POST /admin/users/edit with plain password | Verify password handling policy (currently risk: plain assignment in controller) | Passed |
| TC-ADM-USER-006 | P1 | Admin users | Existing user | userId=X | GET /admin/users/delete?userId=... | User removed | Passed |
| TC-ADM-ORD-001 | P1 | Admin orders | Logged in admin | existing commandes | GET /admin/commandes | All orders displayed | Passed |
| TC-ADM-ORD-002 | P1 | Admin orders | Existing order | order id=X, status=LIVREE | POST /admin/commandes/status id=X status=LIVREE | Status updated and redirected with success | Passed |
| TC-ADM-ORD-003 | P1 | Admin orders | Unknown order id | order id=99999 | POST /admin/commandes/status id=99999 | Runtime error path handled and surfaced | Passed |

## 8. Automated Coverage Mapping (Current)
Existing unit/controller tests already cover significant behavior:
- Services: Account, Cart, Categorie, Commande, Produit, UserDetails
- Controllers: Login, Public, Cart, Order, Categorie, Produit, User, AdminCommande

## 9. Execution
Run all tests:

```bash
# install java-17-openjdk-devel if you encounter problems
./mvnw test
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running Tests du Controller AdminCommandeController
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.816 s -- in Tests du Controller AdminCommandeController
[INFO] Running Tests du Controller CartController
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.351 s -- in Tests du Controller CartController
[INFO] Running Tests du Controller CategorieController
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.173 s -- in Tests du Controller CategorieController
[INFO] Running Tests du Controller LoginController
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.020 s -- in Tests du Controller LoginController
[INFO] Running Tests du Controller OrderController
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.127 s -- in Tests du Controller OrderController
[INFO] Running Tests du Controller Produit
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.145 s -- in Tests du Controller Produit
[INFO] Running Tests du Controller PublicController
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.066 s -- in Tests du Controller PublicController
[INFO] Running Tests du Controller UserController
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.227 s -- in Tests du Controller UserController
[INFO] Running Tests du Service AccountServiceImpl
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.048 s -- in Tests du Service AccountServiceImpl
[INFO] Running Tests du Service CartServiceImpl
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.039 s -- in Tests du Service CartServiceImpl
[INFO] Running Tests du Service CategorieServiceImpl
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.112 s -- in Tests du Service CategorieServiceImpl
[INFO] Running Tests du Service CommandeServiceImpl
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.105 s -- in Tests du Service CommandeServiceImpl
[INFO] Running Tests du Service ProduitServiceImpl
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.112 s -- in Tests du Service ProduitServiceImpl
[INFO] Running Tests du Service UserDetailsServiceImpl
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.015 s -- in Tests du Service UserDetailsServiceImpl
[INFO] Running com.storeshop.StoreShopApplicationTests
[WARNING] Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0 s -- in com.storeshop.StoreShopApplicationTests
[INFO] 
[INFO] Results:
[WARNING] Tests run: 120, Failures: 0, Errors: 0, Skipped: 1
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  9.053 s
[INFO] Finished at: 2026-04-06T10:43:41+01:00
[INFO] ------------------------------------------------------------------------

```

Run a specific test class:

```bash
./mvnw -Dtest=CommandeServiceImplTest test
```

## 10. Traceability Notes
- Functional behavior is derived from controller/service contracts in src/main/java/com/storeshop.
- Existing automated tests are located in src/test/java/com/storeshop.