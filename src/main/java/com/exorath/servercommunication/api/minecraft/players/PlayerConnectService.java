package com.exorath.servercommunication.api.minecraft.players;

import org.bukkit.entity.Player;

/**
 * This service is responsible for connecting bukkit players to the appropriate servers.
 * Created by Toon Sevrin on 6/16/2016.
 */
public interface PlayerConnectService {
    /**
     * Tries to connect the provided player to the provided bungee server.
     * @param player player to connect to server
     */
    void connect(Player player);
}
