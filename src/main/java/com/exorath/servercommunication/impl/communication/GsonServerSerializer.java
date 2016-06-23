package com.exorath.servercommunication.impl.communication;

import com.exorath.servercommunication.api.communication.ServerSerializer;
import com.exorath.servercommunication.api.servers.Server;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

/**
 * Created by Toon Sevrin on 6/23/2016.
 */
public class GsonServerSerializer implements ServerSerializer {

    @Override
    public String serialize(Server server) {
        return new Gson().toJson(server);
    }

    @Override
    public Server deserialize(String message) {
        return new Gson().fromJson(new JsonParser().parse(message), Server.class);
    }
}
