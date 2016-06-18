package com.exorath.servercommunication.servers;

import com.exorath.servercommunication.api.servers.Server;
import com.exorath.servercommunication.api.servers.ServerProvider;
import com.exorath.servercommunication.impl.server.ServerImpl;

/**
 * Created by Toon Sevrin on 6/18/2016.
 */
public class MockServerProvider implements ServerProvider {
    private String publishingChannel;
    public MockServerProvider(){
        this("somePublishingChannel");
    }

    public MockServerProvider(String publishingChannel){
        this.publishingChannel = publishingChannel;
    }
    @Override
    public Server getServer() {
        return new ServerImpl();
    }

    @Override
    public String getPublishingChannel() {
        return publishingChannel;
    }
}
