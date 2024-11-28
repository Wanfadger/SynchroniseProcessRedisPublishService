package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.beanConfig;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Duration;

@Configuration
public class SynchroniseCacheConfig {

    @Value("${spring.data.redis.host}")
    String host;

    @Value("${spring.data.redis.port}")
    int port;


    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }





    @Bean
    public RedisTemplate< String , Object> redisTemplate(JedisConnectionFactory redisConnectionFactory){
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new  Jackson2JsonRedisSerializer<>(Object.class);
        RedisTemplate< String , Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setEnableTransactionSupport(true);

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }


    @Bean
    RedisSerializationContext.SerializationPair<String> cacheManagerKeySerializer(){
        return RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
    }

    @Bean
    RedisSerializationContext.SerializationPair<Object> cacheManagerValueSerializer(){
    return RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    }


    @Bean
    @Primary
    public RedisCacheManager cacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(cacheManagerKeySerializer())
                .serializeValuesWith(cacheManagerValueSerializer())
                .disableCachingNullValues();
        return RedisCacheManager.builder(jedisConnectionFactory()).cacheDefaults(config).build();
    }

    @Bean("halfHourCacheManager")
    public RedisCacheManager halfHourCacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(cacheManagerKeySerializer())
                .serializeValuesWith(cacheManagerValueSerializer())
                .entryTtl(Duration.ofMinutes(30)).disableCachingNullValues();
        return RedisCacheManager.builder(jedisConnectionFactory()).cacheDefaults(config).build();
    }

    @Bean("hourCacheManager")
    public RedisCacheManager hourCacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1))
                 .serializeKeysWith(cacheManagerKeySerializer())
                .serializeValuesWith(cacheManagerValueSerializer())
                .disableCachingNullValues();
        return RedisCacheManager.builder(jedisConnectionFactory()).cacheDefaults(config).build();
    }

    @Bean("_24HourCacheManager")
    public RedisCacheManager _24HourCacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24))
                 .serializeKeysWith(cacheManagerKeySerializer())
                .serializeValuesWith(cacheManagerValueSerializer())
                .disableCachingNullValues();
        return RedisCacheManager.builder(jedisConnectionFactory()).cacheDefaults(config).build();
    }

    @Bean("weekCacheManager")
    public RedisCacheManager weekCacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(7))
                 .serializeKeysWith(cacheManagerKeySerializer())
                .serializeValuesWith(cacheManagerValueSerializer())
                .disableCachingNullValues();
        return RedisCacheManager.builder(jedisConnectionFactory()).cacheDefaults(config).build();
    }


    @Bean("monthCacheManager")
    public RedisCacheManager monthCacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(30))
                 .serializeKeysWith(cacheManagerKeySerializer())
                .serializeValuesWith(cacheManagerValueSerializer())
                .disableCachingNullValues();
        return RedisCacheManager.builder(jedisConnectionFactory()).cacheDefaults(config).build();
    }
}
