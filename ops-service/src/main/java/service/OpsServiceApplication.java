package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class OpsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpsServiceApplication.class, args);
    }

}

@Controller
@RequestMapping("/greet")
class GreetingsController {

    String template = "Hello, %s!";
    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("/")
    @ResponseBody
    public Greeting greet(@RequestParam (name = "name", required = false, defaultValue = "world") String name ) {
            return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

}


class Greeting {

    Integer id;
    String name;

    public Greeting(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Greeting() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
