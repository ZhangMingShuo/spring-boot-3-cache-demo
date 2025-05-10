package com.mingshuo.comment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.annotation.CachingConfigurerSupport#errorHandler()
     */
    @Override
    public CacheErrorHandler errorHandler() {

        return new CacheErrorHandler() {
            @Override
            public void handleCachePutError(final RuntimeException exception, final Cache cache, final Object key, final Object value) {
                log.error("Failed to put key '{}' into Redis Cache: {}.", key, cache.getName(), exception);
            }

            @Override
            public void handleCacheGetError(final RuntimeException exception, final Cache cache, final Object key) {
                log.error("Failed to get key '{}' from Redis Cache: {}.", key, cache.getName(), exception);
            }

            @Override
            public void handleCacheEvictError(final RuntimeException exception, final Cache cache, final Object key) {
                log.error("Failed to evict key '{}' from Redis Cache: {}.", key, cache.getName(), exception);
                throw exception;
            }

            @Override
            public void handleCacheClearError(final RuntimeException exception, final Cache cache) {
                log.error("Failed to clear Redis Cache: {}.", cache.getName(), exception);
            }
        };
    }
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {

        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        // 序列化值
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
//            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
            config = config.computePrefixWith(CacheKeyPrefix.prefixed(redisProperties.getKeyPrefix()));
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        return config;
    }
}