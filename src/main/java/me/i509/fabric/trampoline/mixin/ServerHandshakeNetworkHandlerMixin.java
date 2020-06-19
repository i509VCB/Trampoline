package me.i509.fabric.trampoline.mixin;

import me.i509.fabric.trampoline.mixin.accessors.HandshakeC2SPacketAccessor;
import me.i509.fabric.trampoline.impl.ClientConnectionExtensions;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginDisconnectS2CPacket;
import net.minecraft.server.network.ServerHandshakeNetworkHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import com.google.gson.Gson;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerHandshakeNetworkHandler.class)
public abstract class ServerHandshakeNetworkHandlerMixin {
    @Shadow @Final private ClientConnection connection;

    @Unique private static final Gson GSON = new Gson();

    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/network/ClientConnection.setPacketListener(Lnet/minecraft/network/listener/PacketListener;)V", ordinal = 0, shift = Shift.AFTER), method = "onHandshake(Lnet/minecraft/network/packet/c2s/handshake/HandshakeC2SPacket;)V")
    public void onHandshake(HandshakeC2SPacket handshakePacket, CallbackInfo ci) {
        ClientConnectionExtensions connectionExtensions = (ClientConnectionExtensions) connection;

        HandshakeC2SPacketAccessor packetAccessor = (HandshakeC2SPacketAccessor) handshakePacket;

        String[] split = packetAccessor.getAddress().split("\00");

        if (split.length == 3 || split.length == 4) {
            packetAccessor.setAddress(split[0]);
            connectionExtensions.setRealAddress(new java.net.InetSocketAddress(split[1], ((java.net.InetSocketAddress) connection.getAddress()).getPort()));
            connectionExtensions.setSpoofedUUID(com.mojang.util.UUIDTypeAdapter.fromString(split[2]));
        } else {
            Text text = new LiteralText("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!");
            connection.send(new LoginDisconnectS2CPacket(text));
            connection.disconnect(text);
            return;
        }

        if (split.length == 4) {
            connectionExtensions.setSpoofedProfile(GSON.fromJson(split[3], com.mojang.authlib.properties.Property[].class));
        }
    }
}
