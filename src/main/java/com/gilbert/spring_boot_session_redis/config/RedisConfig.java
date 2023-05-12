package com.gilbert.spring_boot_session_redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.FlushMode;
import org.springframework.session.SaveMode;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.util.StringUtils;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {
    @Value("${spring.session.timeout}")
    private int maxInactiveIntervalInSeconds;

    @Value("${spring.session.redis.namespace}")
    private String redisNamespace;

    private final RedisProperties redisProperties;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getUrl());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        if (StringUtils.hasText(redisProperties.getPassword())) {
            redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        }
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        poolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        poolConfig.setMaxWait(redisProperties.getLettuce().getPool().getMaxWait());
        poolConfig.setTimeBetweenEvictionRuns(redisProperties.getLettuce().getPool().getTimeBetweenEvictionRuns());
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .build();
        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
    }

    @Bean
    public RedisOperations<String, Object> sessionRedisOperations(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    @Primary
    @Bean
    public RedisSessionRepository redisSessionRepository(RedisOperations<String, Object> sessionRedisOperations) {
        RedisSessionRepository redisSessionRepository = new RedisSessionRepository(sessionRedisOperations);
        redisSessionRepository.setDefaultMaxInactiveInterval(Duration.ofSeconds(maxInactiveIntervalInSeconds));
        redisSessionRepository.setRedisKeyNamespace(redisNamespace);
        redisSessionRepository.setSaveMode(SaveMode.ON_SET_ATTRIBUTE);
        redisSessionRepository.setFlushMode(FlushMode.ON_SAVE);
        return redisSessionRepository;
    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}
