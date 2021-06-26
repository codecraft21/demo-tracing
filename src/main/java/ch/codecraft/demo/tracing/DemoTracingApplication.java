package ch.codecraft.demo.tracing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@SpringBootApplication
public class DemoTracingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoTracingApplication.class, args);
    }
}

@RestController
@Slf4j
@RequiredArgsConstructor
class DemoController {
    private final KafkaTemplate<String, String> template;

    @RequestMapping("/")
    public String home() {
        log.info("Handling home");
        final var uuid = UUID.randomUUID();
        template.send("test", "some data " + uuid);
        log.info("produced data {}", uuid);
        return "Hello World";
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
