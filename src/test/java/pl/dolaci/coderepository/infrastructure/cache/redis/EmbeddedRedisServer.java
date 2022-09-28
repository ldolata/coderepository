package pl.dolaci.coderepository.infrastructure.cache.redis;


import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@Slf4j
public class EmbeddedRedisServer {
    private final RedisServer redisServer;

    public EmbeddedRedisServer(Integer port) {
        this.redisServer = new RedisServer(port);
    }

    public void start() throws Exception {
        log.info("Starting redis server");
        redisServer.start();
    }

    public void destroy() throws Exception {
        log.info("Stopping redis server");
        redisServer.stop();
    }

    public static void main(String[] args) throws Exception {
        new EmbeddedRedisServer(6379).start();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        redisServer.stop();
    }
}