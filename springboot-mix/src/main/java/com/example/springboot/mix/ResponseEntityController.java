package com.example.springboot.mix;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对比ResponseEntity 和 ResponseBody 区别
 */
@RestController
public class ResponseEntityController {

    /**
     * ResponseEntity 允许我们添加标头Head和状态码
     *
     * @return
     */
    @GetMapping("/1")
    public ResponseEntity<User> resp1() {
        return ResponseEntity.badRequest().body(new User("wang", "g@q.com"));
//        return ResponseEntity.ok(new User("wang", "g@q.com"));
    }

    @GetMapping("/2")
    @ResponseBody
    public User resp2() {
        return new User("wang", "g@q.com");
    }

    /**
     * 必须配合RestController使用，否则报错
     *
     * @return
     */
    @GetMapping("/3")
    public User resp3() {
        return new User("wang", "g@q.com");
    }

    /**
     * 手工处理response
     *
     * @param response
     * @return
     */
    @GetMapping("/4")
    public void resp4(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setContentType("application/json;charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(new User("wang", "g@q.com"));
        response.getWriter().write(jsonString);
    }
}
