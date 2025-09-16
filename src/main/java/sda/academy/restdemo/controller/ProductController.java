package sda.academy.restdemo.controller;

import sda.academy.restdemo.model.Product;
import sda.academy.restdemo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {

       Product product =  productService.getProductById(id);
       return ResponseEntity.ok(product);

    }


    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @Valid Product product) {
        productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer id, @RequestBody @Valid Product product) {
        productService.updateProduct(id, product);
        return ResponseEntity.status(HttpStatus.OK).body("Product updated successfully");
    }

/*    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
    }



    // @ExceptionHandler in Controller - doar pentru un controller specific , ( local in cadrul unui controller)
}
