package com.exorath.servercommunication.impl;

import com.exorath.servercommunication.api.CommunicationManager;
import com.exorath.servercommunication.api.servers.Server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by Toon Sevrin on 6/23/2016.
 */
public class ServerManager {
    private ScheduledExecutorService executorService =  Executors.newSingleThreadScheduledExecutor();
    private CommunicationManager communicationManager;
    //Will packaging this in a wrapper object impact performance?
    private HashMap<String, Server> serversById = new HashMap<>();
    private HashMap<String, HashSet<String>> serversByChannel = new HashMap<>();
    private HashMap<String, ScheduledFuture> futuresByServerId = new HashMap<>();

    public ServerManager(CommunicationManager communicationManager){
        this.communicationManager = communicationManager;
    }
    public void onMessage(String channel, String message){
        if(communicationManager.getServerSerializer() == null)
            return;

        Server server = communicationManager.getServerSerializer().deserialize(message);

        cancelFuture(server.getId());
        putServer(channel, server);
        scheduleRemoval(channel, server);
    }
    private void cancelFuture(String id){
        if(futuresByServerId.containsKey(id))
            futuresByServerId.get(id).cancel(false);
    }
    private void putServer(String channel, Server server){
        serversById.put(server.getId(), server);
        serversByChannel.putIfAbsent(channel, new HashSet<>());
        serversByChannel.get(channel).add(server.getId());

    }

    private void scheduleRemoval(String channel, Server server){
        futuresByServerId.put(server.getId(), executorService.schedule(() -> {
            remove(channel, server);
        }, server.getExpirationDelay(), server.getExpirationUnit()));
    }

    private void remove(String channel, Server server){
        futuresByServerId.remove(server.getId());
        serversById.remove(server.getId());
        serversByChannel.get(channel).remove(server.getId());
        if(serversByChannel.get(channel).size() == 0)
            serversByChannel.remove(channel);
    }

    public HashMap<String, Server> getServersById() {
        return serversById;
    }

    public HashMap<String, HashSet<String>> getServersByChannel() {
        return serversByChannel;
    }
}
