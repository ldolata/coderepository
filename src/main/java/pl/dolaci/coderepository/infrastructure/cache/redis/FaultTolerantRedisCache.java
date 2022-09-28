package pl.dolaci.coderepository.infrastructure.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import pl.dolaci.coderepository.util.ReactiveCache;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
/**
 * Fault tolerant redis cache implementation. The cache will try to
 * put/get data to/from redis cache. If connection to redis fails the service will
 * logged errors although getting date will work.
 */
public class FaultTolerantRedisCache<K,T> implements ReactiveCache<K,T> {
    private final ReactiveRedisOperations<K,T> cacheOperation;
    private final Duration cacheEvictionTime;
    private final Duration serverTimeout;

    public FaultTolerantRedisCache(ReactiveRedisOperations<K,T> cacheOperation, Duration cacheEvictionTime, Duration serverTimeout) {
        this.cacheOperation = cacheOperation;
        this.cacheEvictionTime = cacheEvictionTime;
        this.serverTimeout = serverTimeout;
    }

    public Mono <T> get (K key) {
        return cacheOperation.opsForValue().get(key).
                timeout(serverTimeout)
                .onErrorResume(e->{
                    log.error("Unable to get value from cache");
                    return Mono.empty();
                });
    }

    private T put (K key, T object) {
        cacheOperation.opsForValue().set(key, object, cacheEvictionTime).
                timeout(serverTimeout).
                subscribe();
        return object;
    }

    public Mono<T> getAndCache(K key, Mono<T> function){
        return get(key).
                switchIfEmpty(function.map(object-> put(key, object)));
    }

}
