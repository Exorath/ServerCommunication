package com.exorath.servercommunication.impl.minecraft.players;

import com.exorath.servercommunication.api.minecraft.players.PlayerConnectService;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Temporary implementation to connect players by redis.
 * TODO: This redis connection system should be done with a seperate library:
 *      Library should handle callback.
 *      Library should handle message layout.
 *      Library should handle channel names
 * Created by Toon Sevrin on 6/16/2016.
 */
public class PlayerConnectByRedis implements PlayerConnectService {
    //TODO: These variables must be placed in a seperate library
    private static final String NAME_ID = "name";
    private static final String UUID_ID = "uuid";
    private static final String BUNGEEID_ID = "bungeeid";

    private JedisPool jedisPool;
    private String connectChannel;
    private String bungeeServerId;

    public PlayerConnectByRedis(String bungeeServerId, JedisPool jedisPool, String connectChannel) {
        this.bungeeServerId = bungeeServerId;
        this.jedisPool = jedisPool;
        this.connectChannel = connectChannel;
    }

    @Override
    public void connect(Player player) {
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(connectChannel, getMessage(player, bungeeServerId).toString());
            }
        }, "redisPublisherThread").start();
    }

    public JsonObject getMessage(Player player, String bungeeServerId) {
        JsonObject object = new JsonObject();
        object.add(NAME_ID, new JsonPrimitive(player.getName()));
        object.add(UUID_ID, new JsonPrimitive(player.getUniqueId().toString()));
        object.add(BUNGEEID_ID, new JsonPrimitive(bungeeServerId));
        return object;
    }
}
