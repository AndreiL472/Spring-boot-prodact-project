# Set `CATEGORY_ID = NULL` when deleting a Category (H2 / Spring Data JPA)

This guide shows how to configure your database so that **when a `Category` is deleted, all related `Product` rows keep their data and have `CATEGORY_ID` set to `NULL`** (instead of being deleted).

Applies to **H2** (and similar SQL dialects) and integrates cleanly with Spring Data JPA.

---

## Why?

* You want to **keep products** even if the category is removed.
* Avoid `ConstraintViolationException` / FK errors on delete.
* Avoid writing custom application code to `SET category_id = NULL` before deleting the category.

---

## Prerequisites

* Table `PRODUCT` has a foreign key column `CATEGORY_ID` referencing `CATEGORY(ID)`.
* The FK column must be **nullable**.

```sql
-- Make sure the FK column allows NULLs
ALTER TABLE PRODUCT ALTER COLUMN CATEGORY_ID DROP NOT NULL;  -- H2
```

> If your H2 version prefers `SET NULL` syntax, use:
>
> ```sql
> ALTER TABLE PRODUCT ALTER COLUMN CATEGORY_ID SET NULL;
> ```

---

## Step 1 — Drop the existing FK constraint

Hibernate often generates random FK constraint names. Find it, then drop it.

**Find the FK name:**

```sql
SELECT CONSTRAINT_NAME, TABLE_NAME, COLUMN_NAME
FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE
WHERE TABLE_NAME = 'PRODUCT' AND COLUMN_NAME = 'CATEGORY_ID';
```

**Drop the FK:**

```sql
ALTER TABLE PRODUCT DROP CONSTRAINT <YOUR_FK_NAME>;
-- Example: ALTER TABLE PRODUCT DROP CONSTRAINT FK1MTSBUR82FRN64DE7BALYMQ9S;
```

---

## Step 2 — Create a new FK with `ON DELETE SET NULL`

```sql
ALTER TABLE PRODUCT
  ADD CONSTRAINT FK_PRODUCT_CATEGORY
  FOREIGN KEY (CATEGORY_ID)
  REFERENCES CATEGORY(ID)
  ON DELETE SET NULL;
```

This tells the database: *when a referenced `CATEGORY` row is deleted, set `PRODUCT.CATEGORY_ID` to `NULL` automatically*.

---

## Step 3 — Verify

```sql
-- Check referential constraints in H2
SELECT CONSTRAINT_NAME, DELETE_RULE
FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS
WHERE CONSTRAINT_NAME = 'FK_PRODUCT_CATEGORY';
-- Expected: DELETE_RULE = 'SET NULL'
```

---

## JPA/Hibernate mapping (important)

In your JPA entities make sure the FK is nullable and **do not** cascade deletes from Category to Product.

```java
// Product.java
@ManyToOne(optional = true)
@JoinColumn(name = "category_id", nullable = true)
private Category category;

// Category.java
@OneToMany(mappedBy = "category")
private List<Product> products; // No cascade = REMOVE, no orphanRemoval
```

> **Do NOT** use `cascade = CascadeType.REMOVE` or `orphanRemoval = true` on the `@OneToMany` side, as those would delete the products when the category is deleted.

---

## Spring Boot tips

* If you use `spring.jpa.hibernate.ddl-auto=update`, manual DDL changes persist on **file-based** H2. For **in-memory** H2, place these statements in `schema.sql` (or manage via Flyway/Liquibase).
* Example `application.properties` for H2 (file):

  ```properties
  spring.datasource.url=jdbc:h2:file:./data/testdb
  spring.datasource.driver-class-name=org.h2.Driver
  spring.datasource.username=sa
  spring.datasource.password=
  spring.jpa.hibernate.ddl-auto=update
  spring.h2.console.enabled=true
  spring.jpa.show-sql=true
  ```

---

## Test it

1. Insert sample data:

```sql
INSERT INTO CATEGORY (ID, NAME) VALUES (1, 'Electronics');
INSERT INTO PRODUCT (ID, NAME, PRICE, CATEGORY_ID) VALUES (10, 'TV', 1000, 1);
INSERT INTO PRODUCT (ID, NAME, PRICE, CATEGORY_ID) VALUES (11, 'Laptop', 2000, 1);
```

2. Delete the category:

```sql
DELETE FROM CATEGORY WHERE ID = 1;
```

3. Check products:

```sql
SELECT ID, NAME, CATEGORY_ID FROM PRODUCT WHERE ID IN (10,11);
-- CATEGORY_ID should now be NULL
```

---

## Alternative (application-only approach)

If you cannot change the FK rule in DB, you can do a bulk update before delete:

```java
public interface ProductRepository extends JpaRepository<Product, Integer> {
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Product p set p.category = null where p.category.id = :catId")
  int clearCategoryByCategoryId(@Param("catId") Integer catId);
}

@Service
@Transactional
public class CategoryService {
  public void deleteCategoryKeepProducts(Integer catId) {
    productRepository.clearCategoryByCategoryId(catId);
    categoryRepository.deleteById(catId);
  }
}
```

---

## Rollback / Change behavior

* To **delete products automatically** when a category is deleted, use:

  ```sql
  ALTER TABLE PRODUCT
    ADD CONSTRAINT FK_PRODUCT_CATEGORY
    FOREIGN KEY (CATEGORY_ID)
    REFERENCES CATEGORY(ID)
    ON DELETE CASCADE;
  ```
* To go back, drop and recreate the FK with `SET NULL` again.

---

## Troubleshooting

* **Cannot drop constraint** → double-check the exact name from `INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE`.
* **Column not nullable** → run the `ALTER TABLE ... DROP NOT NULL` first and ensure JPA mapping has `nullable = true`.
* **Hibernate re-creates FK differently** → manage schema with Flyway/Liquibase so your DDL is authoritative.

That’s it — now deleting a `Category` will automatically set `PRODUCT.CATEGORY_ID` to `NULL` and keep your products intact.
