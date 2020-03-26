package me.i509.fabric.trampoline.mixin;

import me.i509.fabric.trampoline.MixinHelpers;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.server.network.ServerHandshakeNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerHandshakeNetworkHandler.class)
public class MixinServerHandshakeNetworkHandler {

    @Shadow @Final private ClientConnection connection;

    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/network/ClientConnection.setPacketListener(Lnet/minecraft/network/listener/PacketListener;)V", ordinal = 0, shift = Shift.AFTER), method = "onHandshake(Lnet/minecraft/network/packet/c2s/handshake/HandshakeC2SPacket;)V")
    public void onHandshake(HandshakeC2SPacket handshakePacket, CallbackInfo ci) { // First to go.
        MixinHelpers.handshake(handshakePacket, this.connection);
    }
}
