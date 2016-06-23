package com.exorath.servercommunication.api.events;

import com.exorath.servercommunication.api.servers.Server;

/**
 * Created by Toon Sevrin on 6/23/2016.
 */
public class ServerEvent {
    private Server server;

    public ServerEvent(Server server){
        this.server = server;
    }

    public Server getServer() {
        return server;
    }
}
