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

package com.exorath.servercommunication.impl.minecraft.players;

import com.exorath.servercommunication.api.minecraft.players.SerializedPlayer;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * JSON implementation of SerializedPlayer
 * Created by Toon Sevrin on 5/17/2016.
 */
public class SerializedPlayerImpl implements SerializedPlayer {
    @SerializedName("name")
    private String name;
    @SerializedName("uuid")
    private UUID uniqueId;

    public SerializedPlayerImpl(String name, UUID uniqueId) {
        this.name = name;
        this.uniqueId = uniqueId;
    }

    /**
     * Deserializes a JsonObject to an instance of this class
     *
     * @param obj object to deserialize
     * @return instance of this class build from the specified object
     */
    public static SerializedPlayer deserialize(JsonObject obj) {
        return new GsonBuilder().create().fromJson(obj, SerializedPlayerImpl.class);
    }


    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }
}
