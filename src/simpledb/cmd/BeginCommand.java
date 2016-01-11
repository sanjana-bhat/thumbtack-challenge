package simpledb.cmd;

import simpledb.model.Redis;
import simpledb.redis.RedisStore;

public class BeginCommand implements Command {

    private Redis redis;

    public BeginCommand(Redis redis) {
        this.redis = redis;
    }

    @Override
    public void execute() {
        redis.getTransaction().begin(redis.getRedisStore());
        redis.setRedisStore(new RedisStore());
    }
}
