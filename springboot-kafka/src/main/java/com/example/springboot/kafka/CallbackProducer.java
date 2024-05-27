package com.example.springboot.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class CallbackProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/kafka/callback/{message}")
    public void sendMessageWithCallback(@PathVariable("message") String message) {
        kafkaTemplate.send("sb_topic", message, (SuccessCallback<SendResult<String, Object>>) result -> {
            // 消息发送到的topic
            String topic = result.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = result.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = result.getRecordMetadata().offset();
            System.out.println("发送消息成功1:" + topic + "-" + partition + "-" + offset);
        });

    }
}
