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
import com.exorath.servercommunication.api.servers.Server;
import com.exorath.servercommunication.api.servers.ServerProvider;
import com.exorath.servercommunication.impl.CommunicationManagerImpl;
import com.exorath.servercommunication.impl.communication.RedisPubSub;
import redis.clients.jedis.JedisPool;

/**
 * Created by Toon Sevrin on 6/16/2016.
 */
public interface CommunicationManager {

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
     *
     */
    void publishSelf();

    /**
     *
     * @param channels
     */
    void subscribe(String... channels);
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
     * @param serverProvider  provider to receive the servers status of, used for publishing
     * @return The manager if it successfully started, null if an exception was thrown (it will be printed to the console as well).
     */
    static CommunicationManagerImpl create(PubSub pubSub, ServerProvider serverProvider) {
        return new CommunicationManagerImpl(pubSub, serverProvider);
    }
}
