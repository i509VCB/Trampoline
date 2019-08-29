package me.i509.fabric.trampoline;

import static net.minecraft.command.arguments.EntityArgumentType.getPlayer;
import static net.minecraft.command.arguments.EntityArgumentType.player;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;

import me.i509.fabric.trampoline.accessors.BungeeAdaptedPlayer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.HoverEvent.Action;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

public class TrampolineServerMod implements DedicatedServerModInitializer {
    
    public static BungeeAdaptedPlayer adapt(ServerPlayerEntity entity) {
        return (BungeeAdaptedPlayer) entity;
    }
    
    public static final Logger LOGGER = LogManager.getLogger("Fabric-Trampoline");
    public static final String PREFIX = "[Trampoline-Fabric] ";

    @Override
    public void onInitializeServer() {
        LOGGER.info(PREFIX + "Enabling Trampoline");
        
        CommandRegistry.INSTANCE.register(true, dispatcher -> registerCommands(dispatcher));
    }

    private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> bungeeBase = dispatcher.register(literal("bungee-trampoline")
                .then(literal("info").requires(csource -> csource.hasPermissionLevel(4))
                        .then(argument("player", player())
                                .executes(ctx -> getInfo(ctx.getSource(), getPlayer(ctx, "player").getGameProfile())))
                        .executes(ctx -> getInfo(ctx.getSource(), ctx.getSource().getPlayer().getGameProfile())))
                .executes(ctx -> aboutThis(ctx.getSource())));
        
        dispatcher.register(literal("trampoline")
                .redirect(bungeeBase));
    }

    private int aboutThis(ServerCommandSource source) throws CommandSyntaxException {
        source.sendFeedback(Texts.bracketed(new LiteralText("========Trampoline========").formatted(Formatting.GOLD)), false);
        source.sendFeedback(new LiteralText("Allows IP-Forwarding on Fabric servers connected to Bungeecord").formatted(Formatting.GRAY), false);
        source.sendFeedback(new LiteralText("By i509VCB").formatted(Formatting.GRAY), false);
        
        Text t = new LiteralText("https://github.com/i509VCB/Trampoline").setStyle((new Style().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/i509VCB/Trampoline")))).formatted(Formatting.YELLOW);
        
        source.sendFeedback(new LiteralText("Github Link: ").formatted(Formatting.GREEN).append(t), false);
        return 1;
    }

    private int getInfo(ServerCommandSource source, GameProfile profile) throws CommandSyntaxException {
        ServerPlayerEntity targetted = source.getMinecraftServer().getPlayerManager().getPlayer(profile.getId());
        
        if(targetted == null) {
            throw EntityArgumentType.PLAYER_NOT_FOUND_EXCEPTION.create();
        }
        
        BungeeAdaptedPlayer bu = adapt(targetted);
        source.sendFeedback(new LiteralText("The selected player has the following properties:").formatted(Formatting.YELLOW), false);
        source.sendFeedback(new LiteralText("Connected IP Address: " + bu.getRealAddress().toString().substring(1)), false);
        source.sendFeedback(new LiteralText("Username: " + targetted.getGameProfile().getName()), false);
        source.sendFeedback(new LiteralText("Online-Mode/Current UUID: " + profile.getId().toString()), false);
        source.sendFeedback(new LiteralText("Calculated Offline-Mode UUID: " + ServerPlayerEntity.getUuidFromProfile(new GameProfile(null, profile.getName())).toString()), false);
        return 1;
    }
}
