package com.exorath.servercommunication.impl;

import com.exorath.servercommunication.api.CommunicationManager;
import com.exorath.servercommunication.api.communication.PubSub;
import com.exorath.servercommunication.api.servers.ServerProvider;

/**
 * Created by Toon Sevrin on 6/16/2016.
 */
public class CommunicationManagerImpl implements CommunicationManager {

    private PubSub pubSub;
    private ServerProvider serverProvider;

    public CommunicationManagerImpl(PubSub pubSub, ServerProvider serverProvider) {
        this.pubSub = pubSub;
        this.serverProvider = serverProvider;
    }
    @Override
    public PubSub getPubSub() {
        return pubSub;
    }

    @Override
    public ServerProvider getPublishChannel() {
        return serverProvider;
    }

    @Override
    public void publishSelf() {
        pubSub.publish(serverProvider.getPublishingChannel(), serverProvider.getServer().toString());//TODO: server.toString
        //TODO Generate method stub
    }

    @Override
    public void subscribe(String... channels) {
        pubSub.subscribe(channels);
    }
}
