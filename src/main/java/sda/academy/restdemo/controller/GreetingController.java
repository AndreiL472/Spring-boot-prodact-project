package sda.academy.restdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
   public String greeting(Model model){
        // model este o mapa de atribute trimisa catre view ( template)
        // tot ce pun in model e vizibil in Thymeleaf

        // adaug date in Model
        // adica variabile in template
        model.addAttribute("name", "Alex");
        model.addAttribute("items", List.of("Coffee", "Tea","Water","Juice"));
        model.addAttribute("isLogged", false);
        model.addAttribute("role", "Editor");

        // returnez un nume de view "greeting" // src/main/resources/templates/greeting.html
        return "greeting";
    }
}
