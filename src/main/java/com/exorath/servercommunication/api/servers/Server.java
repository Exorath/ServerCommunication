/*
 *    Copyright 2016 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.servercommunication.api.servers;

import com.exorath.servercommunication.impl.server.ServerImpl;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

/**
 * Created by Toon Sevrin on 6/4/2016.
 */
public interface Server {
    int DEFAULT_EXPIRATION_DELAY = 3;
    TimeUnit DEFAULT_EXPIRATION_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * Gets the id of this server.
     * @return the id of this server
     */
    String getId();

    /**
     * Gets a {@link JsonObject} of ExtraData send by this server.
     * @return a {@link JsonObject} of ExtraData send by this server
     */
    JsonObject getData();

    /**
     * gets after how long this server will expire when registered to a server list.
     * @return after how long this server will expire when registered to a server list
     */
    int getExpirationDelay();
    /**
     * Gets the TimeUnit of the expiration delay
     * @return the TimeUnit of the expiration delay
     */
    TimeUnit getExpirationUnit();

    static Server create(String id){
        return new ServerImpl(id);
    }
    static Server create(String id, int expirationDelay, TimeUnit expirationTimeUnit){
        return new ServerImpl(id, expirationDelay, expirationTimeUnit);
    }
}
