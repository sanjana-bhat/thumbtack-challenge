package simpledb.cmd;

import simpledb.KeyValStore;
import simpledb.model.Redis;
import simpledb.redis.RedisStore;
import simpledb.redis.RedisTransaction;

public class NumequaltoCommand implements Command {

    private String value;
    private Redis redis;

    public NumequaltoCommand(final String value, Redis redis) {
        this.value = value;
        this.redis = redis;
    }

    @Override
    public void execute() {
        RedisStore redisStore = redis.getRedisStore();
        Integer count = redisStore.numequalto(value);
        //If the value is not present, get it from the previous states
        if (count == null) {
            RedisTransaction transaction = redis.getTransaction();
            for (KeyValStore store : transaction) {
                count = store.numequalto(value);
                if (count != null) {
                    break;
                }
            }
        }
        if (count == null) {
            count = 0;
        }
        System.out.println(count);
    }

}
