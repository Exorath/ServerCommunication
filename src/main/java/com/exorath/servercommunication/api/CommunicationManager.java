package com.exorath.servercommunication.api;

import com.exorath.servercommunication.api.communication.PubSub;
import com.exorath.servercommunication.api.minecraft.MCServer;
import com.exorath.servercommunication.api.servers.Server;
import com.exorath.servercommunication.impl.CommunicationManagerImpl;
import com.exorath.servercommunication.impl.communication.RedisPubSub;
import redis.clients.jedis.JedisPool;

/**
 * Created by Toon Sevrin on 6/16/2016.
 */
public interface CommunicationManager {

    /**
     * Gets the PubSub associated with this manager instance.
     *
     * @return the PubSub associated with this manager instance
     */
    PubSub getPubSub();

    /**
     * Gets the channel this server should be broadcasted on.
     *
     * @return the channel this server should be broadcasted on
     */
    String getPublishChannel();

    /**
     *
     * @param server
     */
    void publishSelf(Server server);

    /**
     *
     * @param channels
     */
    void subscribe(String... channels);
    /**
     * Tries to start the CommunicationManager with an {@link redis.clients.jedis.JedisPubSub}
     *
     * @param jedisPool      Pool to fish redis resources out of.
     * @param publishChannel channel this server should be published on
     * @return
     */
    static CommunicationManagerImpl create(JedisPool jedisPool, String publishChannel) {
        return create(new RedisPubSub(jedisPool), publishChannel);
    }

    /**
     * Tries to start the CommunicationManager.
     *
     * @param publishChannel channel this server should broadcast it's status onto
     * @return The manager if it successfully started, null if an exception was thrown (it will be printed to the console as well).
     */
    static CommunicationManagerImpl create(PubSub pubSub, String publishChannel) {
        return new CommunicationManagerImpl(pubSub, publishChannel);
    }
}
