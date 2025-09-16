package sda.academy.restdemo.controller;

import sda.academy.restdemo.model.Category;
import sda.academy.restdemo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/view/categories")
public class CategoryViewController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model){
        model.addAttribute("categories",categoryService.getAllCategories());
        return "categories";
    }


    //  /view/categories/add  pe acelasi url  conform MVC/REST
    // 2 metode
    // 1. GET /add - afiseaza formularul
    // introduc in Model un new Category()
    // 2. POST /add - proceseaza trimiterea formularului
    // primeste datele in  @ModelAttribute Category category
    // salvez datele apoi pot face un redirect:/view/categories

    // GET - arata formularul , POST - proceseaza si salveze


    @GetMapping("/add")
    public String getCategory(Model model){
        model.addAttribute("category", new Category()); // new Category() - obiect gol pentru formular
        return "add-category";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category){
        categoryService.addCategory(category); // salvez in baza de date
        return "redirect:/view/categories"; // redirectare catre lista de categorii

    }

    @GetMapping("/edit/{id}")
    public String showEditCategoryForm(@PathVariable int id, Model model){

        // incarc categoria din baza de date , o pun in Model , si in Thymeleaf o sa apara automat id si name
        // la acea categorie cu id-ul mentionat in path
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "edit-category";
        //view/categories/edit/3   category: id:3 name:phone
    }


    @PostMapping("/edit")
    public String editCategory(@ModelAttribute Category category){
        categoryService.updateCategory(category.getId(), category);
        return "redirect:/view/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable int id){
        categoryService.deleteCategory(id);
        return "redirect:/view/categories";
    }


}
