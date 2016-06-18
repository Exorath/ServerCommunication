package com.exorath.servercommunication.communication;

import com.exorath.servercommunication.impl.communication.RedisPubSub;
import com.fiftyonred.mock_jedis.MockJedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Toon Sevrin on 6/18/2016.
 */
public class MockRedisPubSub extends RedisPubSub {
    public MockRedisPubSub(){
        super(new MockJedisPool(new JedisPoolConfig(), "someunknownhost"));
    }

}
