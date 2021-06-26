package ch.codecraft.demo.tracing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoTracingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoTracingApplication.class, args);
    }

}

@RestController
@Slf4j
class DemoController {

    @RequestMapping("/")
    public String home() {
        log.info("Handling home");
        return "Hello World";
    }
}
