package com.exorath.servercommunication.impl;

import com.exorath.servercommunication.api.CommunicationManager;
import com.exorath.servercommunication.api.communication.PubSub;
import com.exorath.servercommunication.api.communication.ServerSerializer;
import com.exorath.servercommunication.api.communication.SubscriptionConsumer;
import com.exorath.servercommunication.api.events.ServerEvent;
import com.exorath.servercommunication.api.servers.Server;
import com.exorath.servercommunication.api.servers.ServerProvider;
import com.exorath.servercommunication.impl.communication.GsonServerSerializer;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Toon Sevrin on 6/16/2016.
 */
public class CommunicationManagerImpl implements CommunicationManager {
    private PubSub pubSub;
    private ServerProvider serverProvider;
    private ServerSerializer serverSerializer;


    private Subject<ServerEvent, ServerEvent> onPublish = new SerializedSubject(PublishSubject.create());
    private Subject<ServerEvent, ServerEvent> onServerUpdate = new SerializedSubject(PublishSubject.create());
    private Subject<ServerEvent, ServerEvent> onServerRemoved = new SerializedSubject(PublishSubject.create());

    private ServerManager serverManager;

    public CommunicationManagerImpl(PubSub pubSub, ServerProvider serverProvider) {
        this.pubSub = pubSub;
        setServerProvider(serverProvider);

        setServerSerializer(new GsonServerSerializer());

        this.serverManager = new ServerManager(this);//requires serverSerializer, is this bad coding practice?
        pubSub.addSubscriptionConsumer(new SubConsumer());//requires serverManager, is this bad coding practice?
    }

    @Override
    public void setServerProvider(ServerProvider serverProvider) {
        this.serverProvider = serverProvider;
    }

    @Override
    public Collection<Server> getServers() {
        return serverManager.getServersById().values();
    }

    @Override
    public Map<String, Server> getServersById() {
        return serverManager.getServersById();
    }

    @Override
    public Map<String, HashSet<String>> getServersByChannel() {
        return serverManager.getServersByChannel();
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
    public ServerSerializer getServerSerializer() {
        return serverSerializer;
    }

    @Override
    public void setServerSerializer(ServerSerializer serverSerializer) {
        this.serverSerializer = serverSerializer;
    }

    @Override
    public Subject<ServerEvent, ServerEvent> getOnPublish() {
        return onPublish;
    }

    @Override
    public Subject<ServerEvent, ServerEvent> getOnServerUpdate() {
        return onServerUpdate;
    }

    @Override
    public Subject<ServerEvent, ServerEvent> getOnServerRemoved() {
        return onServerRemoved;
    }

    @Override
    public void publishSelf() {
        Server server = serverProvider.getServer();
        pubSub.publish(serverProvider.getPublishingChannel(), serverSerializer.serialize(server));
        onPublish.onNext(new ServerEvent(server));
    }

    @Override
    public void subscribe(String... channels) {
        pubSub.subscribe(channels);
    }


    private class SubConsumer implements SubscriptionConsumer {
        @Override
        public void onPublish(String channel, String message) {
            if (serverManager != null)
                serverManager.onMessage(channel, message);
        }

    }
}
