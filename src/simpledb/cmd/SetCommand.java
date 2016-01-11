package simpledb.cmd;

import simpledb.KeyValStore;
import simpledb.model.Redis;
import simpledb.redis.RedisStore;
import simpledb.redis.RedisTransaction;

public class SetCommand implements Command {

    private String key;
    private String value;
    private Redis redis;

    public SetCommand(final String key, final String value, Redis redis) {
        this.key = key;
        this.value = value;
        this.redis = redis;
    }

    @Override
    public void execute() {
        RedisStore redisStore = redis.getRedisStore();
        RedisTransaction transaction = redis.getTransaction();
        int count = 0;
        String oldVal;
        for (KeyValStore store : transaction) {
            RedisStore tempStore = (RedisStore) store;
            oldVal = tempStore.get(key);
            if (oldVal != null && !oldVal.equals(value)) {
                tempStore.decreaseValueCountBy(oldVal, 1);
            }
            if (tempStore.numequalto(value) != null) {
                count += tempStore.numequalto(value);
            }
        }

        if (redisStore.numequalto(value) != null && redisStore.numequalto(value) == 0) {
            redisStore.increaseValueCountBy(value, count);
        }
        redisStore.set(key, value);

    }
}
