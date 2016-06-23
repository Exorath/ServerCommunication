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

    private int heartbeat;
    private TimeUnit heartbeatTimeUnit;

    private Subject<ServerEvent, ServerEvent> onPublish;
    private Subject<ServerEvent, ServerEvent> onServerUpdate;
    private Subject<ServerEvent, ServerEvent> onServerRemoved;

    private ServerManager serverManager;
    public CommunicationManagerImpl(PubSub pubSub, ServerProvider serverProvider, int heartbeat, TimeUnit heartbeatTimeUnit) {
        this.pubSub = pubSub;
        this.serverProvider = serverProvider;

        this.heartbeat = heartbeat;
        this.heartbeatTimeUnit = heartbeatTimeUnit;

        this.serverSerializer = new GsonServerSerializer();

        this.serverManager = new ServerManager(this);
    }

    private void setupSubjects() {
        onPublish = getNewSubject();
        onServerUpdate = getNewSubject();
        onServerRemoved = getNewSubject();
    }

    private Subject<ServerEvent, ServerEvent> getNewSubject() {
        return new SerializedSubject(PublishSubject.create());
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
    public Map<String, HashSet<Server>> getServersByChannel() {
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
    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    @Override
    public int getHeartbeat() {
        return heartbeat;
    }

    @Override
    public void setHeartbeatTimeUnit(TimeUnit heartbeatTimeUnit) {
        this.heartbeatTimeUnit = heartbeatTimeUnit;
    }

    @Override
    public TimeUnit getHeartbeatTimeUnit() {
        return heartbeatTimeUnit;
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


    private class SubConsumer implements SubscriptionConsumer{
        @Override
        public void onPublish(String channel, String message) {

        }

    }
}
