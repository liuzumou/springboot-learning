package com.example.springboot.kafka;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "services_rest_log")
@RestController
public class Log2Kafka {

    private static final org.slf4j.Logger log1 = LoggerFactory.getLogger("services_rest_log");

    @RequestMapping("/kafka/log/{message}")
    public String log2Kafka(@PathVariable String message) {
        log1.info("log2Kafka---: {}", message);
        log.info("log2Kafka: {}", message);
        return message;
    }
}
