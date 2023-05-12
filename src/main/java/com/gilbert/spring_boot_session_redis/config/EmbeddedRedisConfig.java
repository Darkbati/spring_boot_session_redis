package com.gilbert.spring_boot_session_redis.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

// 특정 환경에서만 Embedded Redis 사용할 경우는 아래 @Profile 주석 해제하면 됨.
//@Profile("local")
@RequiredArgsConstructor
@Configuration
public class EmbeddedRedisConfig {
    private final RedisProperties redisProperties;

    protected RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
        this.redisServer = RedisServer.builder().port(redisProperties.getPort()).build();
        this.redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        this.redisServer.stop();
    }
}
