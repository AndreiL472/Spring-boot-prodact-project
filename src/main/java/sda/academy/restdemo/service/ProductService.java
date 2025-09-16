package sda.academy.restdemo.service;

import sda.academy.restdemo.exception.CategoryNotFoundException;
import sda.academy.restdemo.exception.ProductNotFoundException;
import sda.academy.restdemo.model.Category;
import sda.academy.restdemo.model.Product;
import sda.academy.restdemo.repository.CategoryRepository;
import sda.academy.restdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    public Product createProduct(Product product) {
        requireProductBody(product); // validare: body-ul requiestului sa nu fie null
        if (product.getCategory() != null) { // daca s-a trimis categorie
            CategoryValidationForProduct(product.getCategory());
        }
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Integer id, Product product) {
        requireProductBody(product);
        if(productRepository.existsById(id)) {
            CategoryValidationForProduct(product.getCategory());
            product.setId(id);
            Product updatedProduct = productRepository.save(product);
            return Optional.of(updatedProduct);
        }
        return Optional.empty();
    }

/*    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
    }*/

    public Product deleteProductById(Integer id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.deleteById(product.getId());
                    return product;
                }).orElse(
                        null
                );
    }

    private void requireProductBody(Product product) {
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body 'product' is required");
        }
    }

    private void CategoryValidationForProduct(Category category) {
        // dacă permiți produs fără categorie, ieși pur și simplu
        if (category == null) return;

        // la tine id-ul e primitiv (int), deci verifică > 0
        if (category.getId() <= 0) {
            throw new CategoryNotFoundException("Category id must be > 0");
        }

        // verifică existența în DB; dacă nu există -> excepție
        categoryRepository.findById(category.getId())
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category with id " + category.getId() + " not found"));
    }
}
