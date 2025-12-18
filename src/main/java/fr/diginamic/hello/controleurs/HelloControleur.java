package fr.diginamic.hello.controleurs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloControleur {

    @GetMapping
    public String direHello() {
        return helloService.salutations();
    }

    @Autowired
    public  HelloService helloService;


}
