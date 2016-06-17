package com.exorath.servercommunication.api.minecraft;

import com.exorath.servercommunication.api.servers.Server;
import com.exorath.servercommunication.api.minecraft.players.SerializedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Toon Sevrin on 6/16/2016.
 */
public interface MCServer extends Server {

    /**
     * Tries to send the player to this server. Will increment the servers player count regardless of the result.
     * @param player player to send to this server
     */
    void add(Player player);
    /**
     * True if players should be able to join this server (Can be as spectator or as player)
     *
     * @return true if players should be able to join this server
     */
    boolean isJoinable();

    /**
     * Gets the bungeecord id of this game server (The server name used to connect players to this game server)
     *
     * @return the bungeecord id of this game server
     */
    String getBungeeName();


    /**
     * Gets the maximum amount of players allowed on this hub server
     *
     * @return the maximum amount of players allowed on this hub server
     */
    default int getMaxPlayerAmount(){
        return Bukkit.getMaxPlayers();
    }

    /**
     * Gets the amount of players that are on this hub server
     *
     * @return the amount of players that are on this hub server
     */
    int getPlayerAmount();

    /**
     * Gets a list of all players on this server
     *
     * @return a list of all players on this server
     */
    Collection<SerializedPlayer> getPlayers();
}
