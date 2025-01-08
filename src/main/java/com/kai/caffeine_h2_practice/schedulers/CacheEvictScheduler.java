package com.kai.caffeine_h2_practice.schedulers;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheEvictScheduler {

    // 每 10 秒清除一次緩存
    @Scheduled(fixedRate = 10000)
    @CacheEvict(value = "users", allEntries = true)
    public void clearUserCache() {
        System.out.println("Cache cleared");
    }
}
