package pl.dolaci.coderepository.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

public class SimpleReactiveCache <K, T> implements ReactiveCache<K, T> {

    final Duration cacheEviction;
    final Cache<K, T> caffeineCache;

    public SimpleReactiveCache(Duration cacheEviction) {
        this.cacheEviction = cacheEviction;
        this.caffeineCache = Caffeine.newBuilder()
                .expireAfterWrite(cacheEviction)
                .recordStats()
                .build();
    }

    @Override
    public Mono<T> getAndCache(K key, Mono<T> cachedAction) {
        Optional<T> cachedObject = Optional.ofNullable(caffeineCache.getIfPresent(key));
        return cachedObject
                .map(Mono::just)
                .orElseGet(() ->
                        cachedAction.doOnNext(object-> caffeineCache.put(key, object))
                );
    }

    @Override
    public Mono<T> get(K key) {
        Optional<T> cachedObject = Optional.ofNullable(caffeineCache.getIfPresent(key));
        return cachedObject
                .map(Mono::just).orElse(Mono.empty());
    }
}
