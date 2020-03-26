package me.i509.fabric.trampoline;

import com.google.gson.Gson;
import me.i509.fabric.trampoline.accessors.BungeeConnectionModifier;
import me.i509.fabric.trampoline.accessors.HandshakeC2SPacketModifier;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginDisconnectS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class MixinHelpers {

    private MixinHelpers() {
    }

    private static Gson gson = new Gson();

    public static void handshake(HandshakeC2SPacket handshakePacket, ClientConnection client) {
        BungeeConnectionModifier bungeeClientConnection = (BungeeConnectionModifier) client;

        HandshakeC2SPacketModifier modPacket = (HandshakeC2SPacketModifier) handshakePacket;

        String[] split = modPacket.getAddress().split("\00");

        if (split.length == 3 || split.length == 4) {
            modPacket.setAddress(split[0]);
            bungeeClientConnection.setSocketAddress(new java.net.InetSocketAddress(split[1], ((java.net.InetSocketAddress) client.getAddress()).getPort()));
            bungeeClientConnection.setSpoofedUUID(com.mojang.util.UUIDTypeAdapter.fromString(split[2]));
        } else {
            Text text = new LiteralText("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!");
            client.send(new LoginDisconnectS2CPacket(text));
            client.disconnect(text);
            return;
        }

        if (split.length == 4) {
            bungeeClientConnection.setSpoofedProfile(gson.fromJson(split[3], com.mojang.authlib.properties.Property[].class));
        }
    }
}
