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
        int count = redisStore.numequalto(value);
        if (count == 0) {
            RedisTransaction transaction = redis.getTransaction();
            for (KeyValStore store : transaction) {
                count = store.numequalto(value);
                if (count != 0) {
                    break;
                }
            }
        }
        System.out.println(count);
    }

}