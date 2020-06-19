package me.i509.fabric.trampoline.impl;

import static net.minecraft.command.arguments.EntityArgumentType.getPlayer;
import static net.minecraft.command.arguments.EntityArgumentType.player;
import static net.minecraft.server.command.CommandManager.*;
import static net.minecraft.server.command.CommandManager.argument;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import net.minecraft.command.EntitySelector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

final class TrampolineCommands {
	static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		final RootCommandNode<ServerCommandSource> root = dispatcher.getRoot();
		final LiteralCommandNode<ServerCommandSource> bungee = literal("bungee-trampoline")
				.executes(TrampolineCommands::aboutThis)
				.build();
		final LiteralCommandNode<ServerCommandSource> trampoline = literal("trampoline")
				.executes(TrampolineCommands::aboutThis)
				.redirect(bungee)
				.build();
		final LiteralCommandNode<ServerCommandSource> info = literal("info")
				.requires(s -> s.hasPermissionLevel(4))
				.executes(TrampolineCommands::getInfoSelf)
				.build();
		final ArgumentCommandNode<ServerCommandSource, EntitySelector> otherTarget = argument("player", player())
				.requires(s -> s.hasPermissionLevel(4))
				.executes(TrampolineCommands::getInfoTargetted)
				.build();

		info.addChild(otherTarget);

		bungee.addChild(info);

		root.addChild(bungee);
		root.addChild(trampoline);
	}

	private static int getInfoSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final ServerCommandSource source = context.getSource();
		return getInfo(source, source.getPlayer());
	}

	private static int getInfoTargetted(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final ServerCommandSource source = context.getSource();
		final ServerPlayerEntity target = getPlayer(context, "player");

		return getInfo(source, target);
	}

	private static int getInfo(ServerCommandSource source, ServerPlayerEntity target) {
		final GameProfile profile = target.getGameProfile();

		source.sendFeedback(TrampolineCommands.createUsernameText(target), false);
		source.sendFeedback(TrampolineCommands.createDisplayNameText(target), false);
		source.sendFeedback(TrampolineCommands.createAddressText(target), false);
		source.sendFeedback(TrampolineCommands.createOnlineUuidText(profile), false);
		source.sendFeedback(TrampolineCommands.createOfflineUuidText(profile), false);
		return 1;
	}

	private static String getOfflineUUID(GameProfile profile) {
		return PlayerEntity.getOfflinePlayerUuid(profile.getName()).toString();
	}

	private static Text createDisplayNameText(ServerPlayerEntity player) {
		return new LiteralText("Display Name: ").append(player.getDisplayName());
	}

	private static Text createUsernameText(ServerPlayerEntity player) {
		return new LiteralText("Username: ").formatted(Formatting.GOLD)
				.append(player.getEntityName());
	}

	private static Text createAddressText(ServerPlayerEntity player) {
		return new LiteralText("Connected IP Address: ").formatted(Formatting.GOLD)
				.append(new LiteralText(player.getIp()));
	}

	private static Text createOnlineUuidText(GameProfile profile) {
		return new LiteralText("Online-Mode/Current UUID:").formatted(Formatting.GOLD)
				.append(new LiteralText(profile.getId().toString()));
	}

	private static Text createOfflineUuidText(GameProfile profile) {
		return new LiteralText("Calculated Offline-Mode UUID: ").formatted(Formatting.GOLD)
				.append(new LiteralText(TrampolineCommands.getOfflineUUID(profile)));
	}

	private static int aboutThis(CommandContext<ServerCommandSource> context) {
		final ServerCommandSource source = context.getSource();

		source.sendFeedback(new LiteralText("Trampoline").styled(style -> {
			return style.withColor(TextColor.parse("#FFA500"))
					.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/i509VCB/Trampoline"));
		}), false);

		source.sendFeedback(new LiteralText("Allows IP-Forwarding to Fabric servers connected via a Bungeecord proxy.")
				.formatted(Formatting.GRAY), false);
		source.sendFeedback(new LiteralText("By i509VCB")
				.styled(style -> style.withColor(TextColor.parse("#FFA500"))), false);
		return 1;
	}
}
