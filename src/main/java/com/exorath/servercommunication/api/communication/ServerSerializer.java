package com.exorath.servercommunication.api.communication;

import com.exorath.servercommunication.api.servers.Server;

/**
 * Created by Toon Sevrin on 6/23/2016.
 */
public interface ServerSerializer {
    /**
     * Serializes a server to a string which can be send over the network.
     * @param server Server to serialize
     * @return string containing the server data
     */
    String serialize(Server server);

    /**
     * Deserializes a server from a string.
     * @param message string containing the server data
     * @return server derived from the string message
     */
    Server deserialize(String message);
}
