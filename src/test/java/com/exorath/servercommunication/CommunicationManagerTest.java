package com.exorath.servercommunication;

import com.exorath.servercommunication.api.CommunicationManager;
import com.exorath.servercommunication.api.communication.PubSub;
import com.exorath.servercommunication.api.communication.SubscriptionConsumer;
import com.exorath.servercommunication.impl.CommunicationManagerImpl;
import com.exorath.servercommunication.impl.minecraft.MCServerImpl;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import static org.junit.Assert.assertTrue;


/**
 * Created by Toon Sevrin on 6/18/2016.
 */
public class CommunicationManagerTest {

    @Test(expected = JedisException.class, timeout=5000)
    public void testInvalidJedis() {
        CommunicationManagerImpl manager = CommunicationManager.create(new JedisPool("localhost"), "publish");
    }

    private static final String PUBLISH_CHANNEL = "publish";

    @Test
    public void testPublishSelf() throws InterruptedException {
        final MutableBoolean connectMessageReceived = new MutableBoolean(false);
        CommunicationManager manager = CommunicationManager.create(new PubSub() {
            @Override
            public void publish(String channel, String message) {
                if (channel == PUBLISH_CHANNEL)
                    connectMessageReceived.setValue(true);
            }
            @Override
            public void subscribe(String... channels) {}
            @Override
            public void addSubscriptionConsumer(SubscriptionConsumer consumer) {}
            @Override
            public void close() {}
        }, PUBLISH_CHANNEL);

        manager.publishSelf(new MCServerImpl(null, "bungeeid"));
        assertTrue(connectMessageReceived.booleanValue());
    }

    private static final String CONNECT_CHANNEL = "connect";
}
