package simpledb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import simpledb.model.Redis;

public class RedisCliTest {

    RedisCli redisCli;

    @Before
    public void setUp() throws Exception {
        redisCli = new RedisCli();
    }

    @Test
    public void executeCommandTest() {
        redisCli.executeCmdsFromFile("test/resources/sample_input.txt");
        Redis redis = redisCli.getRedisInstance();
        Assert.assertNull(redis.getRedisStore().get("a"));
        Assert.assertEquals(redis.getRedisStore().get("b"), "40");
        Assert.assertEquals(redis.getRedisStore().get("c"), "60");
        Assert.assertEquals((int) redis.getRedisStore().numequalto("d"), 1);

    }
}
