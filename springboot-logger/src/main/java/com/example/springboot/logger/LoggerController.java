package com.example.springboot.logger;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoggerController {

    private static Logger logger = LoggerFactory.getLogger(LoggerController.class);

    @GetMapping("/logger")
    public String testLog() {
        logger.info("logger:LoggerFactory.getLogger");
        log.info("log:slf4j");
        return "ok";
    }
}
