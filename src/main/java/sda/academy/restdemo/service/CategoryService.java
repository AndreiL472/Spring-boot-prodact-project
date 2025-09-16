package sda.academy.restdemo.service;

import sda.academy.restdemo.exception.CategoryNotFoundException;
import sda.academy.restdemo.model.Category;
import sda.academy.restdemo.repository.CategoryRepository;
import sda.academy.restdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new
                        CategoryNotFoundException("Category with id " + id + " not found" ));
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(int id, Category updatedCategory) {


        Category existingCategory =  categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category with id " + id + " not found" ));

            existingCategory.setName(updatedCategory.getName());
            return categoryRepository.save(existingCategory);
    }

    public Boolean deleteCategory(int id) {
        if(categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteCategoryAndKeepProducts(Integer catId){
        int n = productRepository.clearCategoryByCategoryID(catId); // seteaza category_id = NULL
        categoryRepository.deleteById(catId);
    }
}
