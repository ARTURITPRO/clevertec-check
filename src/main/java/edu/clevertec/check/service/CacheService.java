package edu.clevertec.check.service;

import edu.clevertec.check.cache.Cache;
import edu.clevertec.check.cache.impl.LFUCache;
import edu.clevertec.check.cache.impl.LRUCache;
import edu.clevertec.check.util.SettingsUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheService {

    private static final CacheService INSTANCE = new CacheService();

    private static final String CACHE_ALGORITHM_PROPERTY = "cache.algorithm";
    private static final String CACHE_SIZE_PROPERTY = "cache.size";
    private static final String LRU_CACHE = "LRU";
    private static final String LFU_CACHE = "LFU";
    @Getter
    private final Cache cache;

    private CacheService() {
        String propertiesCacheType = SettingsUtil.get(CACHE_ALGORITHM_PROPERTY);
        if (LRU_CACHE.equals(propertiesCacheType)) {
            cache = LRUCache.getInstance();
            log.info("LRU cache initialized: {}", cache);
        } else if (LFU_CACHE.equals(propertiesCacheType)) {
            cache = LFUCache.getInstance();
            log.info("LFU cache initialized: {}", cache);
        } else {
            cache = LRUCache.getInstance();
            log.warn("Default LRU cache initialized: {}", cache);
        }

        String propertiesCacheSize = SettingsUtil.get(CACHE_SIZE_PROPERTY);
        if (propertiesCacheSize != null) {
            cache.setCacheSize(Integer.parseInt(propertiesCacheSize));
            log.info("Set max size cache: {}", propertiesCacheSize);
        }
    }

    public static CacheService getInstance() {
        return INSTANCE;
    }
}
