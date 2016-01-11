package simpledb.model;

import simpledb.redis.RedisStore;
import simpledb.redis.RedisTransaction;

public class Redis {

    private RedisStore redisStore;
    private RedisTransaction transaction;

    public Redis() {
        redisStore = new RedisStore();
        transaction = new RedisTransaction();
    }

    public void setRedisStore(final RedisStore redisStore) {
        this.redisStore = redisStore;
    }

    public RedisStore getRedisStore() {
        return redisStore;
    }

    public void setTransaction(final RedisTransaction transaction) {
        this.transaction = transaction;
    }

    public RedisTransaction getTransaction() {
        return transaction;
    }
}
