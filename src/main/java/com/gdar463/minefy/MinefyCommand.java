/*
 * Copyright (c) 2025 gdar463 <dev@gdar463.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.gdar463.minefy;

import com.gdar463.minefy.config.ConfigScreen;
import com.gdar463.minefy.spotify.SpotifyAuth;
import com.gdar463.minefy.ui.PlaybackHUD;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class MinefyCommand {
    public static void init() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) ->
                dispatcher.register(ClientCommandManager.literal("minefy")
                        .executes(MinefyCommand::openConfigScreen)
                        .then(ClientCommandManager.literal("login")
                                .executes(MinefyCommand::loginToSpotify))
                        .then(ClientCommandManager.literal("config")
                                .executes(MinefyCommand::openConfigScreen))
                        .then(ClientCommandManager.literal("debug")
                                .then(ClientCommandManager.literal("get_player")
                                        .executes(MinefyCommand::getPlayer)))));
        MinefyClient.LOGGER.debug("ConfigCommand registered");
    }

    private static int openConfigScreen(CommandContext<FabricClientCommandSource> ctx) {
        MinefyClient.toOpen = ConfigScreen.generate(null);
        return 0;
    }

    private static int loginToSpotify(CommandContext<FabricClientCommandSource> ctx) {
        SpotifyAuth.startAuthProcess();
        return 0;
    }

    private static int getPlayer(CommandContext<FabricClientCommandSource> ctx) {
        PlaybackHUD.INSTANCE.getPlayer();
        return 0;
    }
}
