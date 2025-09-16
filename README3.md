# Category MVC Views (Thymeleaf + Spring MVC)

This README explains how the **Category** HTML pages and the `CategoryViewController` work together to render pages and process form submits using **Spring MVC** and **Thymeleaf**.

---

## Overview

* Layer: **Web MVC** (HTML pages), not REST.
* Controller: `sda.academy.restdemo.controller.CategoryViewController` annotated with `@Controller` (returns view names, not JSON).
* Templates: Thymeleaf HTML files under `src/main/resources/templates`.
* Binding: `Model` → Thymeleaf via `th:object` + `th:field`.
* Pattern: **GET** shows the form; **POST** processes the form (**PRG**: Post/Redirect/Get).

---

## Routes

Base path: `/view/categories`

| HTTP | Path                         | Purpose                     | View name        |
| ---: | ---------------------------- | --------------------------- | ---------------- |
|  GET | `/view/categories`           | List all categories         | `categories`     |
|  GET | `/view/categories/add`       | Show **Add Category** form  | `add-category`   |
| POST | `/view/categories/add`       | Handle Add form submit      | redirect to list |
|  GET | `/view/categories/edit/{id}` | Show **Edit Category** form | `edit-category`  |
| POST | `/view/categories/edit`      | Handle Edit form submit     | redirect to list |

> The controller uses a `CategoryService` for CRUD; HTML forms bind to a `Category` object.

---

## Controller (key methods)

```java
@Controller
@RequestMapping("/view/categories")
public class CategoryViewController {

  @Autowired
  CategoryService categoryService;

  // 1) LIST
  @GetMapping
  public String listCategories(Model model){
    model.addAttribute("categories", categoryService.getAllCategories());
    return "categories"; // templates/categories.html
  }

  // 2) ADD (GET): show empty form
  @GetMapping("/add")
  public String getCategory(Model model){
    model.addAttribute("category", new Category());
    return "add-category"; // templates/add-category.html
  }

  // 3) ADD (POST): bind & save
  @PostMapping("/add")
  public String addCategory(@ModelAttribute Category category){
    categoryService.addCategory(category);
    return "redirect:/view/categories"; // PRG
  }

  // 4) EDIT (GET): load and show form
  @GetMapping("/edit/{id}")
  public String showEditCategoryForm(@PathVariable int id, Model model){
    model.addAttribute("category", categoryService.getCategoryById(id));
    return "edit-category"; // templates/edit-category.html
  }

  // 5) EDIT (POST): bind & update
  @PostMapping("/edit")
  public String editCategory(@ModelAttribute Category category){
    categoryService.updateCategory(category.getId(), category);
    return "redirect:/view/categories"; // PRG
  }
}
```

### Notes

* `@ModelAttribute Category category` binds form fields to a `Category` instance (requires no-args constructor + setters).
* Hidden input for `id` is used in edit forms to indicate which record to update.
* Use constructor injection instead of field injection in production (cleaner & testable).

---

## Templates

### 1) List (templates/categories.html)

```html
<table>
  <thead>
    <tr><th>ID</th><th>Name</th><th>Actions</th></tr>
  </thead>
  <tbody>
    <tr th:each="cat : ${categories}">
      <td th:text="${cat.id}"></td>
      <td th:text="${cat.name}"></td>
      <td>
        <!-- URL expression: path variable mapping -->
        <a th:href="@{/view/categories/edit/{id}(id=${cat.id})}">Edit</a>
      </td>
    </tr>
  </tbody>
</table>
```

### 2) Add (templates/add-category.html)

```html
<form th:action="@{/view/categories/add}"
      th:object="${category}"
      method="post">
  <div>
    <label>Category name:</label>
    <input type="text" th:field="*{name}" placeholder="Enter category name"/>
  </div>
  <button type="submit">Save</button>
</form>
```

* `th:object="${category}"` sets the backing bean.
* `th:field="*{name}"` binds the input to `category.name` (auto-generates `name="name"` and the current `value`).

### 3) Edit (templates/edit-category.html)

```html
<form th:action="@{/view/categories/edit}"
      th:object="${category}"
      method="post">
  <input type="hidden" th:field="*{id}"/>
  <div>
    <label>Category name:</label>
    <input type="text" th:field="*{name}" placeholder="Enter category name"/>
  </div>
  <button type="submit">Update Category</button>
</form>
```

* Hidden `id` ensures the server knows which record to update.

---

## Binding & Flow (Add / Edit)

### Add

```
GET  /view/categories/add
  → Model["category"] = new Category()
  → Renders form (empty)
POST /view/categories/add  (form-urlencoded: name=...)
  → @ModelAttribute Category category (binding)
  → service.addCategory(category)
  → redirect:/view/categories
```

### Edit

```
GET  /view/categories/edit/{id}
  → service.getCategoryById(id) → Model["category"]
  → Renders form (prefilled id + name)
POST /view/categories/edit  (form-urlencoded: id=..., name=...)
  → @ModelAttribute Category category
  → service.updateCategory(category.getId(), category)
  → redirect:/view/categories
```

---

## Tips & Best Practices

* **Validation:** add Bean Validation and show messages in the form.

  ```java
  @PostMapping("/add")
  public String add(@Valid @ModelAttribute("category") Category category, BindingResult br) {
    if (br.hasErrors()) return "add-category"; // re-render with errors
    categoryService.addCategory(category);
    return "redirect:/view/categories";
  }
  ```

  ```html
  <input th:field="*{name}"/>
  <div class="text-danger" th:if="${#fields.hasErrors('name')}" th:errors="*{name}">Name error</div>
  ```
* **Security:** if Spring Security is enabled, include CSRF token in forms:

  ```html
  <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
  ```
* **URL building:** prefer `@{...}` with path variables: `@{/view/categories/edit/{id}(id=${cat.id})}`.
* **Styling:** add Bootstrap classes as needed.
* **Safer edit POST:** alternatively post to `/edit/{id}` and prefer the path variable over hidden field.

---

## Troubleshooting

* **Form not binding values:** ensure the model contains `category`, and `Category` has getters/setters and a no-args constructor.
* **Template not found:** view name must match file in `templates/` without extension.
* **404 on edit:** the id doesn’t exist; handle it in the service (throw and map to 404 page).
* **CSRF errors:** include the CSRF token if Security is on.

---

## Quick Start Checklist

1. Add dependency: `spring-boot-starter-thymeleaf`.
2. Create controller as above (`@Controller`, not `@RestController`).
3. Place HTML templates in `src/main/resources/templates`.
4. Start the app and visit `/view/categories`.
