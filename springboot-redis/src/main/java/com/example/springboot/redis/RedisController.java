package com.example.springboot.redis;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RedisController {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;


    public void demo() {
//        redisTemplate.opsForValue(); //操作字符串
//        redisTemplate.opsForHash(); //操作hash
//        redisTemplate.opsForList(); //操作list
//        redisTemplate.opsForSet(); //操作set
//        redisTemplate.opsForZSet(); //操作有序zset
    }

    @GetMapping("/has")
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据key删除reids中缓存数据
     */
    @GetMapping("/del")
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }



    /**
     * 指定key的失效时间
     */
    @GetMapping("/expire")
    public boolean expire(String key, long time) {
        //参数一：key
        //参数二：睡眠时间
        //参数三：睡眠时间单位 TimeUnit.DAYS 天 TimeUnit.HOURS 小时 。。。
        return redisTemplate.expire(key, time, TimeUnit.MINUTES);
    }

    /**
     * 保存和读取String
     */
    @GetMapping("/getString")
    public String getString(String key) {
        //读取redis数据
        return redisTemplate.opsForValue().get(key).toString();
    }

    @GetMapping("/addString")
    public boolean addString(String key, String value) {
        //保存数据
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    @GetMapping("/addString2")
    public boolean addString2(String key, String value) {
        //保存数据
        redisUtil.set(key, value);
        return true;
    }

    @GetMapping("/increment")
    public boolean increment(String key, long delta) {
        return redisUtil.incr(key, delta) > 0;
    }

    @GetMapping("/hset")
    public boolean hset(String key, String item, String value) {
        return redisUtil.hset(key, item, value);
    }

    @GetMapping("/hget")
    public Object hget(String key, String item) {
        return redisUtil.hget(key, item);
    }

    @GetMapping("/lset")
    public boolean lset(String key, String value) {
        return redisUtil.lSet(key, value);
    }

    @GetMapping("/lget")
    public Object lget(String key) {
        return redisUtil.lGet(key,0,-1);
    }
}
