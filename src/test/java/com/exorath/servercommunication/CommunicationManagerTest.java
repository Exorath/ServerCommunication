package com.exorath.servercommunication;

import com.exorath.servercommunication.api.CommunicationManager;
import com.exorath.servercommunication.api.communication.PubSub;
import com.exorath.servercommunication.api.communication.SubscriptionConsumer;
import com.exorath.servercommunication.api.servers.Server;
import com.exorath.servercommunication.api.servers.ServerProvider;
import com.exorath.servercommunication.communication.MockPubSub;
import com.exorath.servercommunication.impl.CommunicationManagerImpl;
import com.exorath.servercommunication.impl.server.ServerImpl;
import com.exorath.servercommunication.servers.MockServerProvider;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by Toon Sevrin on 6/18/2016.
 */
public class CommunicationManagerTest {

    /**
     * Tests if a JedisException is thrown when a CommunicationManager is created with a wrong jedis host.
     */
    @Test(expected = JedisException.class, timeout = 5000)
    public void testInvalidJedis() {
        CommunicationManagerImpl manager = CommunicationManager.create(new JedisPool("someUnknownHost"), new MockServerProvider());
    }

    private static final String PUBLISH_CHANNEL = "publish";

    /**
     * Tests if the registered {@link PubSub} gets called when {@Link CommunicationManager#publishSelf} is called.
     */
    @Test
    public void testPublishSelf() {
        final MutableBoolean connectMessageReceived = new MutableBoolean(false);

        CommunicationManager manager = CommunicationManager.create(new PubSub() {
            @Override
            public void publish(String channel, String message) {
                if (channel == PUBLISH_CHANNEL)
                    connectMessageReceived.setValue(true);
            }

            public void subscribe(String... channels) {
            }

            public void addSubscriptionConsumer(SubscriptionConsumer consumer) {
            }

            public void close() {
            }
        }, new MockServerProvider(PUBLISH_CHANNEL));

        manager.publishSelf();

        assertTrue(connectMessageReceived.booleanValue());
    }

    @Test
    public void testExpiration() {
        CommunicationManager manager = CommunicationManager.create(new MockPubSub(), new ServerProvider() {
            @Override
            public Server getServer() {
                return Server.create("testid", 100, TimeUnit.MILLISECONDS);
            }

            @Override
            public String getPublishingChannel() {
                return PUBLISH_CHANNEL;
            }
        });
        manager.subscribe(PUBLISH_CHANNEL);
        manager.publishSelf();

        assertEquals(1, manager.getServers().size());
        assertEquals(1, manager.getServersByChannel().size());
        assertEquals(1, manager.getServersById().size());
        try {
            Thread.sleep(50);
            assertEquals(1, manager.getServers().size());
            assertEquals(1, manager.getServersByChannel().size());
            assertEquals(1, manager.getServersById().size());
            Thread.sleep(45);
            assertEquals(1, manager.getServers().size());
            assertEquals(1, manager.getServersByChannel().size());
            assertEquals(1, manager.getServersById().size());
            Thread.sleep(6);
            assertEquals(0, manager.getServers().size());
            assertEquals(0, manager.getServersByChannel().size());
            assertEquals(0, manager.getServersById().size());
            manager.publishSelf();
            assertEquals(1, manager.getServers().size());
            assertEquals(1, manager.getServersByChannel().size());
            assertEquals(1, manager.getServersById().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    /**
     * Tests if the registered {@link PubSub} gets called when {@Link CommunicationManager#subscribe} is called.
     */
    @Test
    public void testSubscribe() {
        String[] channels = {"samplechannel1", "samplechannel1", "samplechannel1"};
        final MutableBoolean connectMessageReceived = new MutableBoolean(false);

        CommunicationManager manager = CommunicationManager.create(new PubSub() {

            @Override
            public void subscribe(String... channels) {
                if (channels.equals(channels))
                    connectMessageReceived.setValue(true);
            }

            public void addSubscriptionConsumer(SubscriptionConsumer consumer) {
            }

            public void close() {
            }

            public void publish(String channel, String message) {
            }
        }, new MockServerProvider());

        manager.subscribe(channels);

        assertTrue(connectMessageReceived.booleanValue());
    }

    private static final String CONNECT_CHANNEL = "connect";

    private ServerProvider getMockMCServerProvider() {
        return new ServerProvider() {
            @Override
            public Server getServer() {
                return new ServerImpl("someid");
            }

            @Override
            public String getPublishingChannel() {
                return PUBLISH_CHANNEL;
            }
        };
    }
}
