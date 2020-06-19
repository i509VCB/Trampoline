package me.i509.fabric.trampoline.impl;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class TrampolineServerMod implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("Trampoline-Forwarding");

    @Override
    public void onInitializeServer() {
        LOGGER.info("Enabling Trampoline");
        LOGGER.warn("The server is in offline mode to allow connection to Bungeecord. Please secure your server using the tutorial below, otherwise anyone can join the server:");
        LOGGER.warn("https://www.spigotmc.org/wiki/firewall-guide/");
        CommandRegistrationCallback.EVENT.register(TrampolineCommands::registerCommands);
    }
}
