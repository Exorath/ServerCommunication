package com.exorath.servercommunication.impl.minecraft;

import com.exorath.servercommunication.api.minecraft.players.PlayerConnectService;
import com.exorath.servercommunication.api.minecraft.players.SerializedPlayer;
import com.exorath.servercommunication.api.minecraft.MCServer;
import com.exorath.servercommunication.impl.minecraft.players.SerializedPlayerImpl;
import com.exorath.servercommunication.impl.server.ServerImpl;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Toon Sevrin on 6/11/2016.
 */
public class MCServerImpl extends ServerImpl implements MCServer{
    @SerializedName("bungee")
    private String bungeeName;
    @SerializedName("joinable")
    private boolean joinable = false;
    @SerializedName("players")
    private Set<SerializedPlayer> players = new HashSet<>();
    @SerializedName("maxPlayers")
    private int maxPlayerAmount = 0;
    @SerializedName("extra")
    private JsonObject extraData = new JsonObject();

    private transient PlayerConnectService connectService;

    public MCServerImpl(PlayerConnectService connectService, String bungeeName) {
        this.bungeeName = bungeeName;
        this.connectService = connectService;
    }

    public MCServerImpl(PlayerConnectService connectService, String bungeeName, boolean joinable, int maxPlayerAmount) {
        this(connectService, bungeeName);
        this.joinable = joinable;
        this.maxPlayerAmount = maxPlayerAmount;
    }

    /**
     * Deserializes a JsonObject to an instance of this class
     *
     * @param obj object to deserialize
     * @return instance of this class build from the specified object
     */
    public static MCServerImpl deserialize(JsonObject obj) {
        return new GsonBuilder().create().fromJson(obj, MCServerImpl.class);
    }

    @Override
    public void add(Player player) {
        connectService.connect(player);
        players.add(new SerializedPlayerImpl(player.getName(), player.getUniqueId()));
        updateJoinable();
    }

    public void updateJoinable() {
        if (getPlayers().size() >= getMaxPlayerAmount())
            setJoinable(false);
    }

    @Override
    public String getBungeeName() {
        return bungeeName;
    }

    public void setJoinable(boolean joinable) {
        this.joinable = joinable;
    }

    @Override
    public boolean isJoinable() {
        return joinable;
    }

    public void setMaxPlayerAmount(int maxPlayerAmount) {
        this.maxPlayerAmount = maxPlayerAmount;
    }

    @Override
    public int getMaxPlayerAmount() {
        return maxPlayerAmount;
    }

    @Override
    public int getPlayerAmount() {
        return players.size();
    }

    public void setPlayers(Set<SerializedPlayer> players) {
        this.players = players;
    }

    @Override
    public Collection<SerializedPlayer> getPlayers() {
        return players;
    }

    @Override
    public JsonObject getExtraData() {
        return extraData;
    }
}
