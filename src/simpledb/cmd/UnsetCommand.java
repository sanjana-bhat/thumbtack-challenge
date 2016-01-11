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

        if (redisStore.isKeyMarkedUnset(key)) {
            return;
        }
        String value = redisStore.get(key);
        int count = 0;
        //The current redis state doesn't have the key and hence needs to be searched in the saved states
        if (value == null) {
            for (KeyValStore store : transaction) {
                value = store.get(key);
                //Once the key is found it's value count is decremented indicating an unset
                if (value != null) {
                    count = store.numequalto(value);
                    break;
                }
            }
        }
        //Update the value count from previous state
        if (value != null && (redisStore.numequalto(value) == null || redisStore.numequalto(value) == 0)) {
            redisStore.increaseValueCountBy(value, count);
        }
        if (value != null) {
            redisStore.decreaseValueCountBy(value, 1);
        }
        redisStore.unset(key);
    }
}
