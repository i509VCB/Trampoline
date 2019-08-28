package me.i509.fabric.trampoline.mixin;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.i509.fabric.trampoline.accessors.HandshakeC2SPacketModifier;
import net.minecraft.network.NetworkState;
import net.minecraft.server.network.packet.HandshakeC2SPacket;
import net.minecraft.util.PacketByteBuf;

@Mixin(HandshakeC2SPacket.class)
public class MixinHandshakeC2SPacket implements HandshakeC2SPacketModifier {

    @Shadow
    private int port;
    
    @Shadow
    private NetworkState state;
    
    @Shadow
    private int version;
    
    @Shadow
    private String address;

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public int getPort() {
        return this.port;
    }
    
    /**
     * @reason Why? I am lazy. Also to shut up Loom complaing about docs
     * @author i509VCB
     */
    @Overwrite
    public void read(PacketByteBuf packetByteBuf_1) throws IOException {
        this.version = packetByteBuf_1.readVarInt();
        // Bungee sends a much larger handshake address than 255 characters. Also 32767 is the maximum packet size.
        this.address = packetByteBuf_1.readString(Short.MAX_VALUE);         
        this.port = packetByteBuf_1.readUnsignedShort();
        this.state = NetworkState.byId(packetByteBuf_1.readVarInt());
    }
}
