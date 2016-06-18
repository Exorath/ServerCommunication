package com.exorath.servercommunication.servers;

import com.exorath.servercommunication.api.minecraft.players.PlayerConnectService;
import com.exorath.servercommunication.impl.minecraft.MCServerImpl;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.entity.Player;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;


/**
 * Created by Toon Sevrin on 6/18/2016.
 */
public class TestMCServer {
    @Test
    public void testJoin(){
        final MutableBoolean connected = new MutableBoolean(false);
        MCServerImpl server = new MCServerImpl((player) -> connected.setValue(true),
                "bungeeName");
        server.add(mock(Player.class));
        assertTrue(connected.booleanValue());
    }
}
