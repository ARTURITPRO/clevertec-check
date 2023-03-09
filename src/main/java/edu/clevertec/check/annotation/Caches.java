package edu.clevertec.check.annotation;

import edu.clevertec.check.aspect.CachAspect;
import edu.clevertec.check.cache.Cache;
import edu.clevertec.check.service.CacheService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * <p>This annotation is put over the methods that the cache
 * should be used when working with.</p>
 *
 * @author Artur Malashkov
 * @see CachAspect
 * @see Cache
 * @see CacheService
 * @since 1.8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Caches {
}
