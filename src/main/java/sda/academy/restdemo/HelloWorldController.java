package sda.academy.restdemo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class HelloWorldController {
    @RequestMapping(method = RequestMethod.GET, path="/api/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello from the other side");
    }
}
