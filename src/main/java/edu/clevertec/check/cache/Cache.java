package edu.clevertec.check.cache;


import edu.clevertec.check.aspect.CachAspect;
import edu.clevertec.check.cache.impl.LFUCache;
import edu.clevertec.check.cache.impl.LRUCache;
import edu.clevertec.check.entity.Entity;

import java.util.Optional;
/**
 * <p>A common interface for implementing various caching algorithms.</p>
 * <p>Works with entities implementing the {@link Entity} interface</p>
 *
 * @author Artur Malashkov
 * @see CachAspect
 * @see LFUCache
 * @see LRUCache
 * @since 1.8
 */

public interface Cache {

    /**
     * Default cache size.
     */
    int DEFAULT_CACHE_SIZE = 10;

    /**
     * Sets the maximum number of entities in the cache.
     *
     * @param cacheSize cache size
     */
    void setCacheSize(int cacheSize);

    /**
     * Saves the entity to the cache.
     *
     * @param entity entity
     * @return saved entity
     */
    Entity save(Entity entity);

    /**
     * Searches for an entity in the cache by ID and type. If the entity is found in the cache,
     * the returned container will contain it, but if the entity is not found, the container will be empty.
     *
     * @param id    ID of the entity in the repository
     * @param clazz type of entity
     * @return container for the entity
     */
    Optional<Entity> find(Integer id, Class<?> clazz);

    /**
     * Updates the entity in the cache. The cache is searched by ID and type.
     *
     * @param entity entity to update
     * @return updated entity
     */
    Entity update(Entity entity);

    /**
     * Deletes an entity from the cache by ID and type.
     *
     * @param id    ID of the entity in the repository
     * @param clazz type of entity
     * @return true if the entity was found and deleted
     */
    boolean delete(Integer id, Class<?> clazz);

    void clear();

    boolean isEmpty();
}
