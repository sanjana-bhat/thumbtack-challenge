package simpledb.cmd;

import simpledb.model.Redis;
import simpledb.redis.RedisStore;

public class CommitCommand implements Command {

    private Redis redis;

    public CommitCommand(Redis redis) {
        this.redis = redis;
    }

    @Override
    public void execute() {
        RedisStore redisStore = (RedisStore) redis.getTransaction().commit(redis.getRedisStore());
        if (redisStore == null) {
            System.out.println("Nothing to COMMIT");
            return;
        }
        redis.setRedisStore(redisStore);
    }

}
