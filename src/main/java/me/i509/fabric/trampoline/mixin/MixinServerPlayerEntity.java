package me.i509.fabric.trampoline.mixin;

import java.net.InetSocketAddress;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.i509.fabric.trampoline.accessors.BungeeProxiedPlayer;
import me.i509.fabric.trampoline.accessors.BungeeConnectionModifier;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity implements BungeeProxiedPlayer {
    
    @Shadow
    private ServerPlayNetworkHandler networkHandler;
    
    @Override
    public InetSocketAddress getRealAddress() {
        return ((BungeeConnectionModifier) networkHandler.client).getRealAddress();
    }
    
}
