package com.example.springboot.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleConsumer {
    @KafkaListener(topics = {"sb_topic"}, groupId = "sb_group")
    public void onNormalMessage(ConsumerRecord<String, Object> record) {
        System.out.println("简单消费：" + record.topic() + "-" + record.partition() + "=" + record.value());
    }
}
