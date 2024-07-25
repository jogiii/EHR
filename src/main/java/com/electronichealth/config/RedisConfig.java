package com.electronichealth.config;

import org.springframework.context.annotation.Bean;
   import org.springframework.data.redis.cache.RedisCacheManager;
   import org.springframework.data.redis.connection.RedisConnectionFactory;
   import org.springframework.cache.annotation.EnableCaching;
   import org.springframework.cache.CacheManager;

   @EnableCaching
   public class RedisConfig {
       @Bean
       public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
           return RedisCacheManager.builder(connectionFactory).build();
       }
   }
