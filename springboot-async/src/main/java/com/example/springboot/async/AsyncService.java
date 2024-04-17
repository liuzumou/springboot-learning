package com.example.springboot.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncService {

    @Async
    public void doSomethingAsync(){
        log.info("异步方法-" + Thread.currentThread().getName() + ",start：" + System.currentTimeMillis());

        try {
            Thread.sleep(300L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("异步方法-" + Thread.currentThread().getName() + ",end：" + System.currentTimeMillis());
    }
}
