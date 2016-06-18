package com.exorath.servercommunication.servers;

import com.exorath.servercommunication.api.servers.Server;
import com.exorath.servercommunication.api.servers.ServerProvider;
import com.exorath.servercommunication.impl.minecraft.MCServerImpl;

/**
 * Created by Toon Sevrin on 6/18/2016.
 */
public class MockMCServerProvider implements ServerProvider{
    private String publishingChannel;
    public MockMCServerProvider(){
        this("somePublishingChannel");
    }
    public MockMCServerProvider(String publishingChannel){
        this.publishingChannel = publishingChannel;
    }
    @Override
    public Server getServer() {
        return new MCServerImpl((player) -> {}, "bungeeid");
    }

    @Override
    public String getPublishingChannel() {
        return publishingChannel;
    }
}
