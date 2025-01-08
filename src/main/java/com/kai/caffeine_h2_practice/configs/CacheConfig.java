package com.kai.caffeine_h2_practice.configs;


import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;

import com.kai.caffeine_h2_practice.models.User;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching  // 啟用緩存功能
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        // 設定最大緩存容量為 100，緩存時間為 10 秒
        return Caffeine.newBuilder()
                .initialCapacity(100) // initial capacity for the cache, the unit is the number of entries
                .maximumSize(500) // maximum size for the cache, the unit is the number of entries
                .expireAfterWrite(10, TimeUnit.SECONDS) // After 10 seconds the entries have been written, they will be expired.
                .expireAfterAccess(10, TimeUnit.SECONDS) // After 10 seconds the entries have been accessed, they will be expired.
//                .refreshAfterWrite(10, TimeUnit.SECONDS) // the cache will be refreshed after 10 seconds without any api request.(TODO: need to be verified)
//                the refreshAfterWite need to work with cacheManager to define the cacheLoader to achieve the refresh function.
                .weakKeys() // Use weak references for keys. The values can be garbage collected if there are no other string references to the keys.
                .weakValues() // Uses weak references for keys. The keys can be garbage collected if there are no strong references to them elsewhere in the application.
//                .softValues() // When the JVM is running low on memory, the garbage collector will evict the entries from the cache based on the LRU algorithm.
                .recordStats(); // record the cache statistics
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        return cacheManager;

//        If we want to use the refreshAfterWrite function, we need to define the cacheLoader in the cacheManager.
//        CaffeineCacheManager cacheManager = new CaffeineCacheManager("users");
//        cacheManager.setCacheLoader(new CacheLoader<Object, Object>() {
//            @Override
//            public Object load(Object key) throws Exception {
//                // 在這裡定義如何從數據來源讀取資料
//                return fetchFromDatabase((Long) key);
//            }
//        });
    }

//    private User fetchFromDatabase(Long id) {
//        // 模擬從資料庫獲取資料的邏輯
//        System.out.println("Fetching data from the database...");
//        return productRepository.findById(id).orElse(null);
//    }
}
