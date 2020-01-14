package me.i509.fabric.trampoline.mixin;

import com.mojang.authlib.GameProfile;
import me.i509.fabric.trampoline.TrampolineServerMod;
import me.i509.fabric.trampoline.accessors.BungeeConnectionModifier;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ServerLoginNetworkHandler.class)
public class MixinServerLoginNetworkHandler {
    
    @Shadow
    private GameProfile profile;
    
    @Shadow
    public ClientConnection client;
    
    @Shadow
    private ServerPlayerEntity clientEntity;
    
    @Shadow
    private void disconnect(Text text_1) {}

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerLoginNetworkHandler$State;READY_TO_ACCEPT:Lnet/minecraft/server/network/ServerLoginNetworkHandler$State;", opcode = Opcodes.GETSTATIC), method = "onHello(Lnet/minecraft/server/network/packet/LoginHelloC2SPacket;)V", cancellable = true)
    public void onHello(CallbackInfo ci) {
        UUID uuid;
        BungeeConnectionModifier bungeeConnection = (BungeeConnectionModifier) client;
        
        if (bungeeConnection.getSpoofedUUID() != null) {
            uuid = bungeeConnection.getSpoofedUUID();
        } else {
            uuid = ServerPlayerEntity.getOfflinePlayerUuid(profile.getName());
        }
        
        profile = new GameProfile(uuid, profile.getName());

        if (bungeeConnection.getSpoofedProfile() != null) {
            for (com.mojang.authlib.properties.Property property : bungeeConnection.getSpoofedProfile()) {
                profile.getProperties().put(property.getName(), property);
            }
        }
        TrampolineServerMod.LOGGER.info(TrampolineServerMod.PREFIX + "Real UUID of player {} is {}", profile.getName(), profile.getId());
    }
}
