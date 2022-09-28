package pl.dolaci.coderepository.util;

import reactor.core.publisher.Mono;

public interface ReactiveCache<K, T> {
    Mono<T> getAndCache(K key, Mono<T> cachedAction);
    Mono<T> get(K key);
}
