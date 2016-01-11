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
        int newValCount = 0, oldValCount = 0;
        String oldVal = redisStore.get(key);
        //Find the current value for the key in saved state stack and get it's count
        if (oldVal == null) {
            for (KeyValStore store : transaction) {
                oldVal = store.get(key);
                if (oldVal != null) {
                    oldValCount = store.numequalto(oldVal);
                    break;
                }
            }
        }
        for (KeyValStore store : transaction) {
            //Find the new value for the key in saved state stack and get it's count
            if (store.numequalto(value) != null) {
                newValCount = store.numequalto(value);
                break;
            }
        }

        //For the first set operation in this transaction, add the previous count
        if (redisStore.numequalto(oldVal) == null || redisStore.numequalto(oldVal) == 0) {
            redisStore.increaseValueCountBy(oldVal, oldValCount);
        }
        if (redisStore.numequalto(value) == null || redisStore.numequalto(value) == 0) {
            redisStore.increaseValueCountBy(value, newValCount);
        }

        //Decrement the count by 1 for the cuurent value
        if (oldVal != null && !oldVal.equals(value)) {
            redisStore.decreaseValueCountBy(oldVal, 1);
        }

        redisStore.set(key, value);

    }
}
