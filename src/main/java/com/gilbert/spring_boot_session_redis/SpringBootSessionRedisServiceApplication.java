package com.gilbert.spring_boot_session_redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@SpringBootApplication
public class SpringBootSessionRedisServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSessionRedisServiceApplication.class, args);
    }

}
