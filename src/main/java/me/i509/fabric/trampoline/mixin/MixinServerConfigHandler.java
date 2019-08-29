package me.i509.fabric.trampoline.mixin;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.authlib.Agent;
import com.mojang.authlib.ProfileLookupCallback;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerConfigHandler;

@Mixin(ServerConfigHandler.class)
public class MixinServerConfigHandler {
    // While IP-Forwarding is enabled, make the game lookup profiles from Mojang rather than calculating an offline UUID
    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/server/MinecraftServer.isOnlineMode()Z"), method = "lookupProfile(Lnet/minecraft/server/MinecraftServer;Ljava/util/Collection;Lcom/mojang/authlib/ProfileLookupCallback;)V", locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private static void checkOnline(MinecraftServer minecraftServer_1, Collection collection_1, ProfileLookupCallback profileLookupCallback_1, CallbackInfo ci, String[] strings_1) {
        minecraftServer_1.getGameProfileRepo().findProfilesByNames(strings_1, Agent.MINECRAFT, profileLookupCallback_1);
        ci.cancel();
    }
}
