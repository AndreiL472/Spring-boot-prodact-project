package sda.academy.restdemo.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sda.academy.restdemo.model.Product;
import sda.academy.restdemo.service.CategoryService;
import sda.academy.restdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/products")
public class ProductViewController {

    @Autowired
    ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String viewProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.createProduct(product);
        return "redirect:/view/products";
    }



}