package com.exorath.servercommunication;

import com.exorath.servercommunication.api.CommunicationManager;
import com.exorath.servercommunication.api.communication.PubSub;
import com.exorath.servercommunication.api.communication.SubscriptionConsumer;
import com.exorath.servercommunication.api.servers.Server;
import com.exorath.servercommunication.api.servers.ServerProvider;
import com.exorath.servercommunication.communication.MockPubSub;
import com.exorath.servercommunication.impl.CommunicationManagerImpl;
import com.exorath.servercommunication.impl.communication.RedisPubSub;
import com.exorath.servercommunication.impl.minecraft.MCServerImpl;
import com.exorath.servercommunication.impl.minecraft.players.PlayerConnectByRedis;
import com.exorath.servercommunication.servers.MockMCServerProvider;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import static org.junit.Assert.assertTrue;


/**
 * Created by Toon Sevrin on 6/18/2016.
 */
public class CommunicationManagerTest {

    /**
     * Tests if a JedisException is thrown when a CommunicationManager is created with a wrong jedis host.
     */
    @Test(expected = JedisException.class, timeout=5000)
    public void testInvalidJedis() {
        CommunicationManagerImpl manager = CommunicationManager.create(new JedisPool("someUnknownHost"), new MockMCServerProvider());
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

            public void subscribe(String... channels) {}
            public void addSubscriptionConsumer(SubscriptionConsumer consumer) {}
            public void close() {}
        }, new MockMCServerProvider(PUBLISH_CHANNEL));

        manager.publishSelf();

        assertTrue(connectMessageReceived.booleanValue());
    }

    /**
     * Tests if the registered {@link PubSub} gets called when {@Link CommunicationManager#subscribe} is called.
     */
    @Test
    public void testSubscribe() {
        String[] channels = {"samplechannel1","samplechannel1","samplechannel1"};
        final MutableBoolean connectMessageReceived = new MutableBoolean(false);

        CommunicationManager manager = CommunicationManager.create(new PubSub() {

            @Override
            public void subscribe(String... channels) {
                if (channels.equals(channels))
                    connectMessageReceived.setValue(true);
            }
            public void addSubscriptionConsumer(SubscriptionConsumer consumer) {}
            public void close() {}
            public void publish(String channel, String message) {}
        },new MockMCServerProvider());

        manager.subscribe(channels);

        assertTrue(connectMessageReceived.booleanValue());
    }
    private static final String CONNECT_CHANNEL = "connect";

    private ServerProvider getMockMCServerProvider(){
        return new ServerProvider() {
            @Override
            public Server getServer() {
                return  new MCServerImpl((player) -> {}, "bungeeid");
            }

            @Override
            public String getPublishingChannel() {
                return PUBLISH_CHANNEL;
            }
        };
    }
}
