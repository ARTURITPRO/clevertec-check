package edu.clevertec.check.aspect;

import edu.clevertec.check.cache.Cache;
import edu.clevertec.check.entity.Entity;
import edu.clevertec.check.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

/**
 * <p>An aspect class that adds functionality for caching entities when receiving,
 * deleting, creating and updating in the dao.</p>
 *
 * @author Artur Malashkov
 * @see edu.clevertec.check.annotation.Caches
 * @see Cache
 * @see CacheService
 * @since 1.8
 */

@Slf4j
@Aspect
public class CachAspect {

    private final Cache cache = CacheService.getInstance().getCache();

    @Pointcut("@annotation(edu.clevertec.check.annotation.Caches)")
    public void joinToCache() {
    }

    @SuppressWarnings("unchecked")
    @Around("joinToCache() && execution( public * edu.clevertec.check.service.impl.ProductServiceImpl.findById(..))")
    public Object searchInTheCacheAndRepo(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("findInTheCacheAndDatabase");
        Class<?> clazz = joinPoint.getSignature().getDeclaringType();
        Class<?> genericClass = (Class<?>) ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[1];
        Object[] arguments = joinPoint.getArgs();

        Optional<Entity> maybeEntity = cache.find((Integer) arguments[1], genericClass);

        if (maybeEntity.isPresent()) {
            log.info("findInTheCacheAndDatabase");
            return maybeEntity;
        } else {
            log.info("findInTheCacheAndDatabase");
            Optional<Entity> entity = (Optional<Entity>) joinPoint.proceed(arguments);
            log.info("findInTheCacheAndDatabase");
            entity.ifPresent(cache::save);
            return entity;
        }
    }

    @Around("joinToCache() && execution(public * save*(..))")
    public Object putToCacheAndDatabase(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("saveInTheCacheAndDatabase");
        Object[] arguments = joinPoint.getArgs();

        Entity entity = (Entity) joinPoint.proceed(arguments);

        cache.save(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Around("joinToCache() && execution(public * update*(..))")
    public Object refreshedInRepoAndCache(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("updateInTheCacheAndDatabase");
        Object[] arguments = joinPoint.getArgs();

        Optional<Entity> entity = (Optional<Entity>) joinPoint.proceed(arguments);

        entity.ifPresent(cache::update);
        return entity;
    }

    @Around("joinToCache() && execution(public * delete*(..))")
    public Object disposalInCacheAndRepo(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("deleteInTheCacheAndDatabase");
        Class<?> clazz = joinPoint.getSignature().getDeclaringType();
        Class<?> genericClass = (Class<?>) ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[1];
        Object[] arguments = joinPoint.getArgs();

        boolean result = (boolean) joinPoint.proceed(arguments);

        cache.delete((Integer) arguments[1], genericClass);
        return result;
    }
}
