package me.i509.fabric.trampoline.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    // isOnlineMode
    @Inject(at = @At("HEAD"), method = "isOnlineMode()Z", cancellable = true)
    public void isOnline(CallbackInfoReturnable<Boolean> ci) {
        ci.setReturnValue(false);
    }
}
