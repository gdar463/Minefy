package com.gdar463.minefy;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinefyClient implements ClientModInitializer {
    public static final String MOD_ID = "minefy/client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Screen toOpen;
    public static int screenTicks = 0;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (toOpen != null) {
                screenTicks++;
                if (screenTicks > 5) {
                    client.setScreen(toOpen);
                    toOpen = null;
                    screenTicks = 0;
                }
            }
        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("minefy")
                .then(ClientCommandManager.literal("debug_screen").executes(this::openDebugScreen))));
    }

    private int openDebugScreen(CommandContext<FabricClientCommandSource> ctx) {
        LOGGER.info("Opening debug screen");
        toOpen = new CustomScreen(Text.of("Debug Screen"));
        return Command.SINGLE_SUCCESS;
    }
}