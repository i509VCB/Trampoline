package me.i509.fabric.trampoline.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerConfigHandler;

@Mixin(ServerConfigHandler.class)
public abstract class ServerConfigHandlerMixin {
    /**
     * @author i509VCB
     * @reason Use online mode for profile lookups.
     */
    @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/server/MinecraftServer.isOnlineMode()Z"), method = "lookupProfile(Lnet/minecraft/server/MinecraftServer;Ljava/util/Collection;Lcom/mojang/authlib/ProfileLookupCallback;)V")
    private static boolean useOnlineProfileChecks(MinecraftServer server) {
        return true;
    }
}
