package me.i509.fabric.trampoline.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;

@Mixin(HandshakeC2SPacket.class)
public abstract class HandshakeC2SPacketMixin {
    /**
     * @author i509VCB
     * @reason Bungee sends a much larger handshake address than 255 characters, so we must read further for profile data.
     */
    @Redirect(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;readString(I)Ljava/lang/String;"))
    public String read(PacketByteBuf buf, int i) {
        return buf.readString(Short.MAX_VALUE);
    }
}
