package me.i509.fabric.trampoline.accessors;

import java.net.InetSocketAddress;
import java.util.UUID;

import com.mojang.authlib.properties.Property;

public interface BungeeConnectionModifier {

    void setSocketAddress(InetSocketAddress inetSocketAddress);
    
    void setSpoofedUUID(UUID uuid);
    
    void setSpoofedProfile(Property[] spoofedProfile);
    
    UUID getSpoofedUUID();
    
    Property[] getSpoofedProfile();
    
    InetSocketAddress getRealAddress();
}
