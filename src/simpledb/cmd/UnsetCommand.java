package simpledb.cmd;

import simpledb.KeyValStore;
import simpledb.model.Redis;
import simpledb.redis.RedisStore;
import simpledb.redis.RedisTransaction;

public class UnsetCommand implements Command {

    private String key;
    private Redis redis;

    public UnsetCommand(final String key, Redis redis) {
        this.key = key;
        this.redis = redis;
    }

    @Override
    public void execute() {
        RedisStore redisStore = redis.getRedisStore();
        RedisTransaction transaction = redis.getTransaction();
        String value = redisStore.get(key);
        if (value == null) {
            for (KeyValStore store : transaction) {
                value = store.get(key);
                if (value != null) {
                    break;
                }
            }
        }
        if (redisStore.numequalto(value) == 0) {
            int count = 0;
            for (KeyValStore store : transaction) {
                count += store.numequalto(value);

            }
            redisStore.increaseValueCountBy(value, count);
        }
        redisStore.unset(key);
    }
}
