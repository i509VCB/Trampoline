package me.i509.fabric.trampoline;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import me.i509.fabric.trampoline.accessors.BungeeProxiedPlayer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.command.EntitySelector;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraft.command.arguments.EntityArgumentType.getPlayer;
import static net.minecraft.command.arguments.EntityArgumentType.player;

public class TrampolineServerMod implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("Fabric-Trampoline");
    public static final String PREFIX = "[Trampoline-Fabric] ";

    public static BungeeProxiedPlayer adapt(ServerPlayerEntity entity) {
        return (BungeeProxiedPlayer) entity;
    }

    @Override
    public void onInitializeServer() {
        LOGGER.info(PREFIX + "Enabling Trampoline");
        LOGGER.warn(PREFIX + "The server is in offline mode to allow connection to Bungeecord. Please secure your server using the tutorial below, otherwise anyone can join the server:");
        LOGGER.warn(PREFIX + "https://www.spigotmc.org/wiki/firewall-guide/");
        CommandRegistry.INSTANCE.register(true, this::registerCommands);
    }

    private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        RootCommandNode<ServerCommandSource> root = dispatcher.getRoot();

        LiteralCommandNode<ServerCommandSource> bungee = CommandManager.literal("bungee-trampoline").executes(this::aboutThis).build();
        LiteralCommandNode<ServerCommandSource> redirect = CommandManager.literal("trampoline").redirect(bungee).build();

        LiteralCommandNode<ServerCommandSource> info = CommandManager.literal("info").requires(s -> s.hasPermissionLevel(4)).executes(this::getInfoSelf).build();

        ArgumentCommandNode<ServerCommandSource, EntitySelector> otherTarget = CommandManager.argument("player", player()).executes(this::getInfoTargetted).build();

        info.addChild(otherTarget);
        bungee.addChild(info);
        root.addChild(bungee);
        root.addChild(redirect);
    }

    private int getInfoSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        return getInfo(source, source.getPlayer());
    }

    private int getInfoTargetted(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity target = getPlayer(context, "player");

        return getInfo(source, target);
    }

    private int getInfo(ServerCommandSource source, ServerPlayerEntity target) throws CommandSyntaxException {
        BungeeProxiedPlayer proxied = adapt(target);
        source.sendFeedback(new LiteralText("The selected player has the following properties:").formatted(Formatting.YELLOW), false);
        source.sendFeedback(new LiteralText("Connected IP Address: " + proxied.getRealAddress().toString().substring(1)), false);
        source.sendFeedback(new LiteralText("Username: " + target.getGameProfile().getName()), false);
        source.sendFeedback(new LiteralText("Online-Mode/Current UUID: " + target.getGameProfile().getId().toString()), false);
        source.sendFeedback(new LiteralText("Calculated Offline-Mode UUID: " + ServerPlayerEntity.getUuidFromProfile(new GameProfile(null, target.getGameProfile().getName())).toString()), false);
        return 1;
    }

    private int aboutThis(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource source = ctx.getSource();
        source.sendFeedback(Texts.bracketed(new LiteralText("========Trampoline========").formatted(Formatting.GOLD)), false);
        source.sendFeedback(new LiteralText("Allows IP-Forwarding on Fabric servers connected to Bungeecord").formatted(Formatting.GRAY), false);
        source.sendFeedback(new LiteralText("By i509VCB").formatted(Formatting.GRAY), false);
        
        Text t = new LiteralText("https://github.com/i509VCB/Trampoline").setStyle((new Style().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/i509VCB/Trampoline")))).formatted(Formatting.YELLOW);
        
        source.sendFeedback(new LiteralText("Github Link: ").formatted(Formatting.GREEN).append(t), false);
        return 1;
    }
}
