package simpledb.cmd;

import simpledb.model.Redis;
import simpledb.redis.RedisStore;

public class RollbackCommand implements Command {

    private Redis redis;

    public RollbackCommand(Redis redis) {
        this.redis = redis;
    }

    @Override
    public void execute() {
        RedisStore redisStore = (RedisStore) redis.getTransaction().rollback();
        if (redisStore == null) {
            System.out.println("ROLLBACK not allowed");
            return;
        }
        redis.setRedisStore(redisStore);
    }
}
