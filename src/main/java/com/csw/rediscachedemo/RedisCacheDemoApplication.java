package com.csw.rediscachedemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("com.csw.rediscachedemo.dao")
public class RedisCacheDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisCacheDemoApplication.class, args);
    }

}
