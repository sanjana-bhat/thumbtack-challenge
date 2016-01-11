package simpledb.cmd;

import simpledb.KeyValStore;
import simpledb.model.Redis;
import simpledb.redis.RedisStore;
import simpledb.redis.RedisTransaction;

public class GetCommand implements Command {

    private String key;
    private Redis redis;

    public GetCommand(final String key, Redis redis) {
        this.key = key;
        this.redis = redis;
    }

    @Override
    public void execute() {
        RedisStore redisStore = redis.getRedisStore();
        String value = redisStore.get(key);
        if (value == null && !redisStore.isKeyMarkedUnset(key)) {
            RedisTransaction transaction = redis.getTransaction();
            for (KeyValStore store : transaction) {
                value = store.get(key);
                if (value != null) {
                    break;
                }
            }
        }
        System.out.println(value);
    }
}
