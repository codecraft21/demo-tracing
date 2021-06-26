package ch.codecraft.demo.tracing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@SpringBootApplication
public class DemoTracingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoTracingApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@RestController
@Slf4j
@RequiredArgsConstructor
class DemoController {
    private final RestTemplate restTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/")
    public String home() {
        log.info("Handling home");
        final var uuid = restTemplate.getForObject("http://localhost:8080/uuid", String.class);
        kafkaTemplate.send("test", "some data " + uuid);
        log.info("produced kafka {}", uuid);
        return "Hello World";
    }

    @GetMapping("/uuid")
    public String uuid() {
        final var uuid = UUID.randomUUID();
        log.info("generated uuid {}", uuid);
        return uuid.toString();
    }
}

@Service
@Slf4j
class AsyncService {
    @KafkaListener(id = "test-container", topics = "test", groupId = "test")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("received '{}' with headers: {}", record.value(), record.headers());
    }
}
