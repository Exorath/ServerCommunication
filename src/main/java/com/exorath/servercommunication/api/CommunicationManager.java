/*   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   */
package com.exorath.servercommunication.api;

import com.exorath.servercommunication.api.communication.PubSub;
import com.exorath.servercommunication.api.communication.ServerSerializer;
import com.exorath.servercommunication.api.events.ServerEvent;
import com.exorath.servercommunication.api.servers.Server;
import com.exorath.servercommunication.api.servers.ServerProvider;
import com.exorath.servercommunication.impl.CommunicationManagerImpl;
import com.exorath.servercommunication.impl.communication.RedisPubSub;
import redis.clients.jedis.JedisPool;
import rx.Observable;
import rx.subjects.Subject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Toon Sevrin on 6/16/2016.
 */
public interface CommunicationManager {
    int DEFAULT_HEARTBEAT = 1;
    TimeUnit DEFAULT_HEARTBEAT_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * Gets the PubSub associated with this manager instance.
     *
     * @return the PubSub associated with this manager instance
     */
    PubSub getPubSub();

    /**
     * Gets the ServerProvider of this manager.
     *
     * @return the ServerProvider of this manager
     */
    ServerProvider getPublishChannel();

    /**
     * Sets the interval this server should automatically publish itself over.
     * Note that if publishSelf is called between two heartbeats, the heartbeat time is reset and an other heartbeat will only be send after this interval.
     *
     * @param heartbeat interval this server should publish itself over
     */
    void setHeartbeat(int heartbeat);

    /**
     * Gets the interval this server should automatically publish itself over.
     *
     * @return The interval this server should automatically publish itself over
     */
    int getHeartbeat();

    /**
     * Sets the TimeUnit of the heartbeat interval.
     *
     * @param heartbeatTimeUnit timeunit to assign to the heartbeat interval
     */
    void setHeartbeatTimeUnit(TimeUnit heartbeatTimeUnit);

    /**
     * Gets the TimeUnit of the heartbeat interval.
     *
     * @return the TimeUnit of the heartbeat interval
     */
    TimeUnit getHeartbeatTimeUnit();

    /**
     * Gets the server serializer, it is responsible for serializing and deserializing servers from and to strings. Default serializer is the GsonServerSerializer.
     * @return the server serializer
     */
    ServerSerializer getServerSerializer();

    /**
     * Sets the server serializer for custom server serialization. It is responsible for serializing and deserializing servers from and to strings.
     * @param serverSerializer the new serializer to assign
     */
    void setServerSerializer(ServerSerializer serverSerializer);
    /**
     * Publishes this server on the publishing channel. Both the server and channel are provided by the ServerProvider
     */
    void publishSelf();

    /**
     * Subscribes to provided server channels. This means any servers published on these channels will be registered in this manager
     *
     * @param channels channels to subscribe to
     */
    void subscribe(String... channels);

    Subject<ServerEvent,ServerEvent> getOnPublish();

    Subject<ServerEvent,ServerEvent> getOnServerUpdate();

    Subject<ServerEvent,ServerEvent> getOnServerRemoved();

    Collection<Server> getServers();
    Map<String, Server> getServersById();
    Map<String, HashSet<Server>> getServersByChannel();
    /**
     * Tries to start the CommunicationManager with an {@link redis.clients.jedis.JedisPubSub}
     *
     * @param jedisPool      Pool to fish redis resources out of.
     * @param serverProvider provider to receive the servers status of, used for publishing
     * @return
     */
    static CommunicationManagerImpl create(JedisPool jedisPool, ServerProvider serverProvider) {
        return create(new RedisPubSub(jedisPool), serverProvider);
    }

    /**
     * Tries to start the CommunicationManager.
     *
     * @param pubSub
     * @param serverProvider provider to receive the servers status of, used for publishing
     * @return The manager if it successfully started, null if an exception was thrown (it will be printed to the console as well).
     */
    static CommunicationManagerImpl create(PubSub pubSub, ServerProvider serverProvider) {
        return new CommunicationManagerImpl(pubSub, serverProvider, CommunicationManager.DEFAULT_HEARTBEAT, DEFAULT_HEARTBEAT_TIME_UNIT);
    }
}
