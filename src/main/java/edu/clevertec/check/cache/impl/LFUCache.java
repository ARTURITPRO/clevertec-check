package edu.clevertec.check.cache.impl;

import edu.clevertec.check.cache.Cache;
import edu.clevertec.check.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class LFUCache implements Cache {

    /**
     * Cache Object.
     */
    private static final Cache INSTANCE = new LFUCache();

    /**
     * A thread-safe map for storing cache items.
     */
    private final Map<String, Element> mapCacheElements = new ConcurrentHashMap<>();

    /**
     * Cache size, default value is set to default.
     */
    @Setter
    private int cacheSize = DEFAULT_CACHE_SIZE;

    /**
     * Saves the entity to the cache.
     *
     * @param entity entity
     * @return saved entity
     */
    @Override
    public Entity save(Entity entity) {
        Optional<Element> maybeElement = findCacheElement(entity.getId(), entity.getClass());
        if (!maybeElement.isPresent()) {
            addCacheElement(entity);
        }
        return entity;
    }

    /**
     * Searches for an entity in the cache by ID and type. If the entity is found in the cache,
     * the returned container will contain it, but if the entity is not found, the container will be empty.
     *
     * @param id    ID of the entity in the repository
     * @param clazz type of entity
     * @return container for the entity
     */
    @Override
    public Optional<Entity> find(Integer id, Class<?> clazz) {
        return findCacheElement(id, clazz)
                .map(Element::getEntity);
    }

    /**
     * Updates the entity in the cache. The cache is searched by ID and type.
     *
     * @param entity entity to update
     * @return updated entity
     */
    @Override
    public Entity update(Entity entity) {
        Optional<Element> maybeElement = findCacheElement(entity.getId(), entity.getClass());
        if (maybeElement.isPresent()) {
            maybeElement.get().setEntity(entity);
        } else {
            addCacheElement(entity);
        }
        return entity;
    }

    /**
     * Deletes an entity from the cache by ID and type.
     *
     * @param id    ID of the entity in the repository
     * @param clazz type of entity
     * @return true if the entity was found and deleted
     */
    @Override
    public boolean delete(Integer id, Class<?> clazz) {
        Optional<Element> maybeElement = findCacheElement(id, clazz);
        if (maybeElement.isPresent()) {
            mapCacheElements.remove(generatedKeyForCache(maybeElement.get().getEntity()));
            log.info("The entity has been removed from the cache: {} [{}]",
                    maybeElement.get().getEntity(),
                    maybeElement.get().getCountCalls()
            );
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears the cache of all elements.
     */
    @Override
    public void clear() {
        mapCacheElements.clear();
    }

    /**
     * Returns true if this cache contains no elements.
     *
     * @return true if this cache contains no elements
     */
    @Override
    public boolean isEmpty() {
        return mapCacheElements.isEmpty();
    }

    /**
     * Adds a new element to the cache.
     *
     * @param entity the entity to add to the element
     */
    private void addCacheElement(Entity entity) {
        while (mapCacheElements.size() >= cacheSize) {
            removeOldCacheElement();
        }
        mapCacheElements.put(generatedKeyForCache(entity), new Element(entity, 1));
        log.info("Entity is saved in the cache: {}", entity);
    }

    /**
     * Generates a key by entity class and id, for storing and searching in the cache.
     *
     * @param entity entity
     * @return key for storing and searching in the cache
     */
    private String generatedKeyForCache(Entity entity) {
        return entity.getClass().getName() + "@" + entity.getId();
    }

    /**
     * Generates a key by entity class and id, for storing and searching in the cache.
     *
     * @param id    ID of the entity in the repository
     * @param clazz type of entity
     * @return key for storing and searching in the cache
     */
    private String generatedKeyForCache(Integer id, Class<?> clazz) {
        return clazz.getName() + "@" + id;
    }

    /**
     * Searches for an item in the cache by ID and the type of entity in it.
     * If such an entity is found, then the returned container will contain an element with it,
     * if there is no element with such an entity, then the container will be empty.
     *
     * @param id    entity ID
     * @param clazz type of entity
     * @return container for the element
     */
    private Optional<Element> findCacheElement(Integer id, Class<?> clazz) {
        Optional<Element> maybeElement = Optional.ofNullable(mapCacheElements.getOrDefault(generatedKeyForCache(id, clazz), null));
        if (maybeElement.isPresent()) {
            maybeElement.get().setCountCalls(maybeElement.get().getCountCalls() + 1);
            log.info("Entity found in cache: {} [{}]",
                    maybeElement.get().getEntity(),
                    maybeElement.get().getCountCalls()
            );
        }
        return maybeElement;
    }

    /**
     * Removes the least used item from the cache
     */
    private void removeOldCacheElement() {
        Optional<Element> maybeElement = mapCacheElements.values().stream()
                .min(Comparator.comparing(Element::getCountCalls));
        if (maybeElement.isPresent()) {
            mapCacheElements.remove(generatedKeyForCache(maybeElement.get().getEntity()));
            log.info("The entity has been removed from the cache: {} [{}]",
                    maybeElement.get().getEntity(),
                    maybeElement.get().getCountCalls()
            );
        }
    }

    /**
     * Static method for getting the cache object.
     *
     * @return cache object
     */
    public static Cache getInstance() {
        return INSTANCE;
    }

    /**
     * The cache element contains the entity and the count of the number of accesses to it.
     */
    @AllArgsConstructor
    @Data
    private static class Element {

        Entity entity;
        Integer countCalls;
    }
}
