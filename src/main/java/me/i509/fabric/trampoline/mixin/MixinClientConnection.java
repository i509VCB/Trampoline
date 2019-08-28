package me.i509.fabric.trampoline.mixin;

import java.net.InetSocketAddress;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.Property;

import io.netty.channel.Channel;
import me.i509.fabric.trampoline.accessors.BungeeConnectionModifier;
import net.minecraft.network.ClientConnection;

@Mixin(ClientConnection.class)
public class MixinClientConnection implements BungeeConnectionModifier {
    
    Gson gson = new GsonBuilder().create();
    
    @Shadow
    private Channel channel;
    
    private java.util.UUID spoofedUUID;
    private com.mojang.authlib.properties.Property[] spoofedProfile;

    private InetSocketAddress socketAddress;
    
    @Override
    public void setSpoofedUUID(UUID uuid) {
        this.spoofedUUID = uuid;
    }
    @Override
    public void setSpoofedProfile(Property[] spoofedProfile) {
        this.spoofedProfile = spoofedProfile;
    }
    @Override
    public UUID getSpoofedUUID() {
        return spoofedUUID;
    }
    @Override
    public Property[] getSpoofedProfile() {
        return spoofedProfile;
    }
    @Override
    public void setSocketAddress(InetSocketAddress inetSocketAddress) {
        this.socketAddress = inetSocketAddress;
    }
    
    public InetSocketAddress getRealAddress() {
        return socketAddress;
    }
}
