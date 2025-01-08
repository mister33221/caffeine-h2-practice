package com.kai.caffeine_h2_practice.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.kai.caffeine_h2_practice.models.User;
import com.kai.caffeine_h2_practice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private CacheManager cacheManager;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
    }

//    value 為緩存名稱，key 為緩存的 key，cacheManager 為緩存管理器
//    我們可以在不同德地方藉由使用 緩存名稱 來指定使用哪個緩存，例如在 CacheEvictScheduler.java 中就指定時間到了要將緩存名稱為 users 的緩存清除
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        // 模擬延遲以驗證緩存效果
        // 如果沒有使用緩存，每次請求都會花費 3 秒
        // 如果使用緩存，則這個方法內的所有邏輯都不會執行，直接返回緩存中的結果
        // 藉此驗證緩存是否生效
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void printCacheStats() {
        CacheStats stats = getCacheStatistics();
        logger.info("Cache Hit Count: " + stats.hitCount());
        logger.info("Cache Miss Count: " + stats.missCount());
        logger.info("Cache Load Success Count: " + stats.loadSuccessCount());
        logger.info("Cache Load Failure Count: " + stats.loadFailureCount());
        logger.info("Cache Hit Rate: " + stats.hitRate());
        logger.info("Cache Eviction Count: " + stats.evictionCount());
    }

    public CacheStats getCacheStatistics() {
        // 取得緩存區 products
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache("users");
        if (cache != null) {
            Cache<Object, Object> nativeCache = cache.getNativeCache();
            return nativeCache.stats();  // 返回 CacheStats
        }
        throw new IllegalStateException("Cache 'products' not found!");
    }
}
