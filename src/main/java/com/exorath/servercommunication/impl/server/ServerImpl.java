package com.exorath.servercommunication.impl.server;

import com.exorath.servercommunication.api.servers.Server;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.concurrent.TimeUnit;

/**
 * Created by Toon Sevrin on 6/16/2016.
 */
public class ServerImpl implements Server {
    @SerializedName("expirationdelay")
    private int expirationDelay = Server.DEFAULT_EXPIRATION_DELAY;
    @SerializedName("expirationtimeunit")
    private TimeUnit expirationTimeUnit = Server.DEFAULT_EXPIRATION_TIME_UNIT;

    @SerializedName("id")
    private String id;

    public ServerImpl(String id){
        this.id = id;
    }

    public ServerImpl(String id, int expirationDelay, TimeUnit expirationTimeUnit){
        this.id = id;
        this.expirationDelay = expirationDelay;
        this.expirationTimeUnit = expirationTimeUnit;
    }
    @Override
    public String getId() {
        return id;
    }

    @SerializedName("data")
    private JsonObject data = new JsonObject();

    @Override
    public JsonObject getData() {
        return data;
    }

    @Override
    public int getExpirationDelay() {
        return expirationDelay;
    }

    @Override
    public TimeUnit getExpirationUnit() {
        return expirationTimeUnit;
    }
}
