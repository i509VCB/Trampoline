package me.i509.fabric.trampoline.mixin;

import me.i509.fabric.trampoline.accessors.BungeeConnectionModifier;
import me.i509.fabric.trampoline.accessors.BungeeProxiedPlayer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.InetSocketAddress;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity implements BungeeProxiedPlayer {

    @Shadow
    private ServerPlayNetworkHandler networkHandler;

    @Override
    public InetSocketAddress getRealAddress() {
        return ((BungeeConnectionModifier) networkHandler.connection).getRealAddress();
    }

}
