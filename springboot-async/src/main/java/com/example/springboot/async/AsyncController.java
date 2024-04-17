package com.example.springboot.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AsyncController {

    @Autowired
    AsyncService asyncService;

    @GetMapping("/async")
    public String testAsync(){
        log.info("主方法-" + Thread.currentThread().getName() + ",start：" + System.currentTimeMillis());
        // async method
        asyncService.doSomethingAsync();
        log.info("主方法-" + Thread.currentThread().getName() + ",end：" + System.currentTimeMillis());
        return "ok";
    }
}
