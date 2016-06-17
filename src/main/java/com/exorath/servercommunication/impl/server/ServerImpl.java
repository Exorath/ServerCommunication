package com.exorath.servercommunication.impl.server;

import com.exorath.servercommunication.api.servers.Server;
import com.google.gson.JsonObject;

/**
 * Created by Toon Sevrin on 6/16/2016.
 */
public class ServerImpl implements Server {
    private JsonObject extraData = new JsonObject();
    @Override
    public JsonObject getExtraData() {
        return extraData;
    }
}
