package pl.paul.springbootjwtauth2b1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {

    @GetMapping("/hello")
    public String getHello(){
        return "hello";
    }

    @GetMapping("/hello-user")
    public String getHelloUser(){
        return "hello user";
    }
}
