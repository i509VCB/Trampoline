package me.i509.fabric.trampoline.mixin;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

import com.mojang.authlib.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.network.ClientConnection;

import me.i509.fabric.trampoline.impl.ClientConnectionExtensions;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin implements ClientConnectionExtensions {
    @Unique private java.util.UUID spoofedUUID;
    @Unique private com.mojang.authlib.properties.Property[] spoofedProfile;
    @Unique private InetSocketAddress realAddress;

    @Override
    public UUID getSpoofedUUID() {
        return this.spoofedUUID;
    }

    @Override
    public void setSpoofedUUID(UUID uuid) {
        this.spoofedUUID = uuid;
    }

    @Override
    public Property[] getSpoofedProfile() {
        return this.spoofedProfile;
    }

    @Override
    public void setSpoofedProfile(Property[] spoofedProfile) {
        this.spoofedProfile = spoofedProfile;
    }

    @Override
    public void setRealAddress(InetSocketAddress inetSocketAddress) {
        this.realAddress = inetSocketAddress;
    }

    /**
     * @author i509VCB
     * @reason Pass real address to connection
     */
    @Overwrite
    public SocketAddress getAddress() {
        return this.realAddress;
    }
}
