# Inventory Management — Improvement Notes

## Code Improvements

| Check | Priority | File | Issue | Suggestion |
|---|---|---|---|---|
| [x] | **Critical** | `ProductServiceImpl.java:46` | **Logic bug in `updateProduct`**: the null check runs on `prod.getPrice()` — the record fetched from the DB, which is always valid. The incoming `product.getPrice()` is never checked. | Move the null check to `product.getPrice()` (the request body). |
| [x] | **Critical** | `ProductServiceImpl.java:27` | **`createProduct` sets ID from request body**: `prod.setId(product.getId())` lets a caller supply any arbitrary ID, which can silently overwrite an existing record by colliding with its primary key. | Remove `prod.setId(product.getId())` and let the DB auto-generate the ID. |
| [ ] | **High** | Missing file | **No global exception handler**: `ProductNotFoundException` and `EmployeeValidationException` are thrown but nothing maps them to HTTP responses, so every caller gets a raw 500 error instead of a meaningful 404 or 400. | Add a `@RestControllerAdvice` class that maps `ProductNotFoundException` → 404 and `EmployeeValidationException` → 400 with a structured error body. |
| [x] | **High** | `Product.java:28` | **`Float` used for price**: floating-point is imprecise for monetary values (`0.1 + 0.2 != 0.3`). | Change `price` to `BigDecimal` and update `@Min` to `@DecimalMin`. |
| [x] | **High** | `EmployeeValidationException.java` | **Misnamed exception**: the class is called `EmployeeValidationException` inside a product-only service. | Rename to `ProductValidationException`. |
| [x] | **High** | `Product.java:28` | **`@Min(value = 0)` contradicts the error message and the schema**: the message says "more than 0" but `@Min(0)` allows exactly 0; `schema.sql` enforces `price > 0`. | Change to `@DecimalMin(value = "0.01")` to be consistent with the DB constraint and the message intent. |
| [x] | **High** | `Product.java:28` | **No `@NotNull` on `price`**: `@Min` does not imply non-null, so a null price silently bypasses Bean Validation and falls through to a fragile manual null check in the service. | Add `@NotNull` to the `price` field. |
| [x] | **Medium** | `ProductController.java:8-9` | **Unused imports**: `org.apache.coyote.Response` (a Tomcat internal) and `org.springframework.http.HttpStatusCode` are imported but never referenced. | Remove both. |
| [x] | **Medium** | `ProductServiceImpl.java` | **Unused import**: `org.springframework.beans.factory.annotation.Autowired` is imported but not used — constructor injection is already handled by `@RequiredArgsConstructor`. | Remove the import. |
| [x] | **Medium** | `ProductServiceImpl.java:8` | **Wrong `@Transactional` import**: uses `jakarta.transaction.Transactional` instead of Spring's own `org.springframework.transaction.annotation.Transactional`. The Spring version supports rollback rules, propagation configuration, and integrates correctly with Spring's proxy infrastructure. | Switch to `org.springframework.transaction.annotation.Transactional`. |
| [x] | **Medium** | `ProductController.java:56` | **Inconsistent validation annotations**: `create` uses `@Valid` and `updateProduct` uses `@Validated` without a validation group — they behave identically here but signal different intentions to future readers. | Use `@Valid` consistently on all request bodies. |
| [x] | **Medium** | `ProductController.java:46-66` | **Incomplete logging**: `POST /product` and `GET /product` have log statements, but `getById`, `updateProduct`, and `deleteProduct` have none. | Add `log.info(...)` at the start of every endpoint method. |
| [x] | **Medium** | `InventoryManagementApplication.java:22` | **Debug print statement**: `System.out.println("Third commit")` is a development leftover in the main method. | Remove it. |
| **NOT NEEDED** | **Medium** | `application.yaml:7-8` | **Hardcoded credentials**: `username: admin` and `password: password` are committed in plain text. | Replace with environment variable references: `${DB_USERNAME}` / `${DB_PASSWORD}`. |
| **NOT NEEDED** | **Medium** | `application.yaml:14-16` | **H2 console enabled unconditionally**: exposes a raw database interface to anyone who can reach the server. | Gate it behind a `dev` Spring profile or disable it entirely. |
| **NOT NEEDED** | **Medium** | `application.yaml:11` | **`ddl-auto: update` is production-unsafe**: Hibernate silently alters the live schema on startup, which can corrupt data or drop columns. | Use `validate` or `none` for anything beyond local development; use `create-drop` in test profiles. |
| [ ] | **Low** | `Product.java` | **No `quantity` field**: an inventory management system tracks stock levels, but the `Product` entity has no quantity field. | Add an `@Min(0)` `Integer quantity` field (and the corresponding column in `schema.sql`). |
| [x] | **Low** | `schema.sql` + `application.yaml` | **Schema management conflict**: `schema.sql` creates the table while `ddl-auto: update` tells Hibernate to also manage the schema — two systems in charge of the same DDL. | Pick one strategy: pure SQL scripts with `ddl-auto: none`, or Hibernate DDL only with no `schema.sql`. |
| [x] | **Low** | `schema.sql:2` | **`BIGSERIAL` is PostgreSQL syntax**: H2 doesn't support PostgreSQL sequences by default; this works only in PostgreSQL-compatibility mode. | Use `BIGINT AUTO_INCREMENT` or eliminate the SQL file and let Hibernate generate the DDL. |
| [x] | **Low** | `data.sql:1` | **Trailing space in seed data**: `'Laptop '` — the integration test was written to match this typo (`assertEquals("Laptop ", name)`), making both the data and the test wrong. | Remove the trailing space from the seed data and fix the assertion. |
| **NOT NEEDED** | **Low** | `ProductController.java:20` | **Non-RESTful URL**: `/product` is singular; REST convention uses plural nouns for collection resources. | Rename to `/products`. |
| **NOT NEEDED** | **Low** | `service/Impl/` package | **`impl` sub-package anti-pattern**: placing `ProductServiceImpl` in an `impl` child package provides no organizational value and pollutes import paths. | Move `ProductServiceImpl` directly into the `service` package. |
| [x] | **Low** | `Product.java:19` | **Commented-out `@Schema` annotation**: `// @Schema(description = "Product ID", example = "1")` is left as dead code. | Either restore it or delete it. |

---

## Additional Test Cases

### Unit Tests (`InventoryManagementApplicationTests.java`)

| Check | Priority | Test Name | Description |
| --- |---|---|---|
| [ ] | **Critical** | `createProduct_shouldSaveAndReturnProduct` | Mock `repository.save(any())` to return a product with ID=1. Call `service.createProduct(prod)`. Assert the returned product's name, price, and that `repository.save()` was invoked exactly once via `Mockito.verify`. |
| [ ] | **Critical** | `createProduct_shouldThrowWhenPriceIsNull` | Set `prod.setPrice(null)`. Use `assertThrows(EmployeeValidationException.class, () -> service.createProduct(prod))` to confirm the guard fires and the exception message matches. |
| [ ] | **Critical** | `getProductById_shouldThrowProductNotFoundException` | Mock `repository.findById(99L)` to return `Optional.empty()`. Use `assertThrows(ProductNotFoundException.class, () -> service.getProductById(99L))` and verify the exception message contains the missing ID. |
| [ ] | **Critical** | `updateProduct_shouldUpdateFieldsAndReturnProduct` | Mock `repository.findById(1L)` to return the existing product. Mock `repository.save(any())` to return an updated product. Call `service.updateProduct(1L, updatedProd)`. Assert the returned product reflects the new name and price. |
| [ ] | **Critical** | `updateProduct_shouldThrowWhenProductNotFound` | Mock `repository.findById(99L)` to return `Optional.empty()`. Use `assertThrows(ProductNotFoundException.class, () -> service.updateProduct(99L, prod))`. |
| [ ] | **High** | `deleteProduct_shouldCallRepositoryDelete` | Mock `repository.findById(1L)` to return an existing product. Call `service.deleteProduct(1L)`. Use `Mockito.verify(repository).delete(prod)` to confirm the delete call was made. |
| [ ] | **High** | `deleteProduct_shouldThrowWhenProductNotFound` | Mock `repository.findById(99L)` to return `Optional.empty()`. Use `assertThrows(ProductNotFoundException.class, () -> service.deleteProduct(99L))`. |
| [ ] | **High** | `getAllProducts_shouldReturnListOfProducts` | Mock `repository.findAll()` to return a list of two products. Call `service.getAllProducts()`. Assert the list size is 2 and the names match the mocked data. |
| [ ] | **High** | `getAllProducts_shouldReturnEmptyListWhenNoProductsExist` | Mock `repository.findAll()` to return `Collections.emptyList()`. Call `service.getAllProducts()`. Assert the returned list is not null and has size 0. |
| [ ] | **Medium** | `updateProduct_shouldThrowWhenIncomingPriceIsNull` | Expose the current logic bug: mock `findById` to return a valid product, then create an incoming update object with `price = null`. Assert that `EmployeeValidationException` is thrown. (This test will currently *fail* because the null check is on the wrong variable — making the bug visible.) |
| [ ] | **Medium** | `createProduct_shouldNotSetIdFromRequestBody` | Set `prod.setId(99L)` before calling `createProduct`. Capture the argument passed to `repository.save()` using `Mockito.ArgumentCaptor`. Assert that the ID of the saved entity is `null`, confirming the service does not pass the caller-supplied ID to the DB. |

### Integration Tests (`InventoryManagementApplicationIntegrationTests.java`)

| Check | Priority | Test Name | Description |
| --- |---|---|---|
| [ ] | **Critical** | `createProduct_shouldPersistAndBeRetrievableById` | Construct a new `Product` with a unique name and valid price. Call `service.createProduct(product)` and capture the returned object. Assert the returned ID is non-null. Then call `service.getProductById(returnedId)` and assert the name and price match — confirming the record is actually in the database. |
| [ ] | **Critical** | `getProductById_shouldThrowProductNotFoundException` | Call `service.getProductById(9999L)` and use `assertThrows(ProductNotFoundException.class, ...)` to verify the exception propagates correctly when the DB has no matching record. |
| [ ] | **Critical** | `updateProduct_shouldPersistChangesToDatabase` | Create a product via `createProduct`. Update it with new name/price via `updateProduct`. Then fetch it again by ID. Assert the fetched values match the updated values, not the originals — confirming the `UPDATE` was committed to the DB. |
| [ ] | **Critical** | `deleteProduct_shouldRemoveRecordFromDatabase` | Create a product via `createProduct`. Delete it by ID via `deleteProduct`. Then attempt `getProductById(deletedId)` and assert `ProductNotFoundException` is thrown — confirming the row is gone. |
| [ ] | **High** | `createProduct_shouldThrowWhenNameIsBlank` | Call `service.createProduct` with a product whose name is `""` or `" "`. Assert that a validation or constraint exception is thrown, testing the `@NotBlank` annotation end-to-end through the service layer. |
| [ ] | **High** | `createProduct_shouldThrowWhenPriceIsZeroOrNegative` | Call `service.createProduct` with `price = 0f` and again with `price = -5f`. Assert that the appropriate exception is thrown in both cases, testing the `@Min`/`@DecimalMin` constraint and/or the database `CHECK` constraint. |
| [ ] | **High** | `getAllProducts_shouldReturnAllSeededProducts` | Call `service.getAllProducts()` and assert the returned list contains at least the product seeded by `data.sql`. Assert by name rather than just list size to make the assertion meaningful. |
| [ ] | **Medium** | `updateProduct_shouldThrowWhenProductNotFound` | Call `service.updateProduct(9999L, someProduct)` and use `assertThrows(ProductNotFoundException.class, ...)` to confirm error handling works during the update path with a real DB. |
| [ ] | **Medium** | `deleteProduct_shouldThrowWhenProductNotFound` | Call `service.deleteProduct(9999L)` and use `assertThrows(ProductNotFoundException.class, ...)` to confirm error handling works during the delete path with a real DB. |
| [ ] | **Low** | Add `@Transactional` to the integration test class | Each test that creates/updates/deletes data currently leaves side effects in the H2 file DB, causing test-order dependencies. Annotating the class (or individual tests) with `@Transactional` makes Spring roll back each test's changes automatically, giving true test isolation. |