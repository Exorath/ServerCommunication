package com.exorath.servercommunication.api;

import com.exorath.servercommunication.api.communication.PubSub;
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
     * @return the channel this server should be broadcasted on
     */
    String getPublishChannel();


    /**
     * Tries to start the CommunicationManager with an {@link redis.clients.jedis.JedisPubSub}
     * @param jedisPool Pool to fish redis resources out of.
     * @param publishChannel channel this server should be published on
     * @return
     */
    static CommunicationManagerImpl create(JedisPool jedisPool, String publishChannel) {
        try {
            return create(new RedisPubSub(jedisPool), publishChannel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tries to start the CommunicationManager.
     *
     * @param publishChannel channel this server should broadcast it's status onto
     * @return The manager if it successfully started, null if an exception was thrown (it will be printed to the console as well).
     */
    static CommunicationManagerImpl create(PubSub pubSub, String publishChannel) {
        try {
            return new CommunicationManagerImpl(pubSub, publishChannel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
