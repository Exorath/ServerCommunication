package com.exorath.servercommunication.api.servers;

/**
 * Created by Toon Sevrin on 6/18/2016.
 */
public interface ServerProvider {

    /**
     * Returns a fresh server object.
     * @return a server object
     */
    Server getServer();

    /**
     * Gets the channel this server should be published on.
     * @return the channel this server should be publsihed on
     */
    String getPublishingChannel();
}
