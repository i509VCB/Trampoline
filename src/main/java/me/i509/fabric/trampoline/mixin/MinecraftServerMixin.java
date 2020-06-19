package me.i509.fabric.trampoline.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    /**
     * @author i509VCB
     * @reason Return offline mode for server messages
     */
    @Overwrite
    public boolean isOnlineMode() {
        return false;
    }
}
