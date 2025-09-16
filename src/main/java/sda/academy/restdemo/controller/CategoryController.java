package sda.academy.restdemo.controller;

import sda.academy.restdemo.model.Category;
import sda.academy.restdemo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();

        return ResponseEntity.ok().body(categories);
    }

/*    @GetMapping("/getGategory")
    public ResponseEntity<String> getCategoryById(@RequestParam Integer id) {
        return ResponseEntity.ok().body(categoryService.getCategoryById(id).getName());
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<String> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(categoryService.getCategoryById(id).getName());
    }

    @PostMapping
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);

        return ResponseEntity.status(HttpStatus.CREATED).body("Category " + category.getName() + " added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        categoryService.updateCategory(id, category);
        return ResponseEntity.ok().body("Category " + category.getName() + " updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id) {
        if(categoryService.deleteCategory(id)) {
            return ResponseEntity.ok().body("Category " + id + " deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category " + id + " not found");
        }
    }



}
