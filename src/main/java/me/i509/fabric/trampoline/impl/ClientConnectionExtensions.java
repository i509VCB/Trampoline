package me.i509.fabric.trampoline.impl;

import java.net.InetSocketAddress;
import java.util.UUID;

import com.mojang.authlib.properties.Property;

public interface ClientConnectionExtensions {
    void setRealAddress(InetSocketAddress inetSocketAddress);
    
    void setSpoofedUUID(UUID uuid);
    
    void setSpoofedProfile(Property[] spoofedProfile);
    
    UUID getSpoofedUUID();
    
    Property[] getSpoofedProfile();
}
