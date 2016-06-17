package com.exorath.servercommunication;

import com.exorath.servercommunication.api.communication.PubSub;
import com.exorath.servercommunication.impl.communication.RedisPubSub;
import redis.clients.jedis.JedisPool;

/**
 * The CommunicationManager allows servers to share their status with each other.
 * Created by Toon Sevrin on 6/11/2016.
 */
public class CommunicationManager {
    
    private PubSub pubSub;
    private String publishChannel;

    private CommunicationManager(PubSub pubSub, String publishChannel) {
        this.pubSub = pubSub;
        this.publishChannel = publishChannel;
    }

    /**
     * Gets the PubSub associated with this manager instance.
     *
     * @return the PubSub associated with this manager instance
     */
    public PubSub getPubSub() {
        return pubSub;
    }

    /**
     * Gets the channel this server should be broadcasted on.
     * @return the channel this server should be broadcasted on
     */
    public String getPublishChannel() {
        return publishChannel;
    }
}
