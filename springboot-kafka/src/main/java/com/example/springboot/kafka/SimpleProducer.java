package com.example.springboot.kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class SimpleProducer {

    //https://blog.csdn.net/qq_20865839/article/details/133948989

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/kafka/simple/{message}")
    public void sendNormalMessage(@PathVariable("message") String message) {
        CompletableFuture<SendResult<String, Object>> result = kafkaTemplate.send("sb_topic", message);
    }
}
