package com.exorath.servercommunication.impl;

import com.exorath.servercommunication.api.CommunicationManager;
import com.exorath.servercommunication.api.communication.PubSub;

/**
 * Created by Toon Sevrin on 6/16/2016.
 */
public class CommunicationManagerImpl implements CommunicationManager {

    private PubSub pubSub;
    private String publishChannel;

    public CommunicationManagerImpl(PubSub pubSub, String publishChannel) {
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
