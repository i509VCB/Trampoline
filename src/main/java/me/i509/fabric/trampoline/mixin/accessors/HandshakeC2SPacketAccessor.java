package me.i509.fabric.trampoline.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;

@Mixin(HandshakeC2SPacket.class)
public interface HandshakeC2SPacketAccessor {
	@Accessor
	String getAddress();

	@Accessor
	void setAddress(String address);

	@Accessor
	int getPort();
}
