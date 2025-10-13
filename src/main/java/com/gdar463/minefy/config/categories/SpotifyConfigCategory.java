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

package com.gdar463.minefy.config.categories;

import com.gdar463.minefy.MinefyClient;
import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.config.MinefyConfig;
import com.gdar463.minefy.config.builders.ButtonOptionBuilder;
import com.gdar463.minefy.config.builders.IntegerFieldOptionBuilder;
import com.gdar463.minefy.config.builders.OptionGroupBuilder;
import com.gdar463.minefy.config.builders.StringOptionBuilder;
import com.gdar463.minefy.spotify.SpotifyAPI;
import com.gdar463.minefy.spotify.models.SpotifyPlayer;
import com.gdar463.minefy.util.Utils;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.text.Text;

public class SpotifyConfigCategory {
    public static ConfigCategory create(MinefyConfig config) {
        return ConfigCategory.createBuilder()
                .name(Text.translatable("text.minefy.config.category.spotify"))
                .group(OptionGroupBuilder.create("text.minefy.config.spotify.client.name",
                                "text.minefy.config.spotify.client.description")
                        .option(StringOptionBuilder.create("text.minefy.config.spotify.client.id.name",
                                        "text.minefy.config.spotify.client.id.description", true)
                                .binding(config.spotify.oAuthClient.clientId, () -> config.spotify.oAuthClient.clientId, val -> config.spotify.oAuthClient.clientId = val)
                                .build())
                        .option(StringOptionBuilder.create("text.minefy.config.spotify.client.redirect.name",
                                        "text.minefy.config.spotify.client.redirect.description")
                                .binding(config.spotify.oAuthClient.redirectUri, () -> config.spotify.oAuthClient.redirectUri, val -> config.spotify.oAuthClient.redirectUri = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.spotify.client.port.name",
                                        "text.minefy.config.spotify.client.port.description", 1024, 65535)
                                .binding(config.spotify.oAuthClient.callbackPort, () -> config.spotify.oAuthClient.callbackPort, val -> config.spotify.oAuthClient.callbackPort = val)
                                .build())
                        .build())
                .group(OptionGroupBuilder.create("text.minefy.config.spotify.tokens.name",
                                "text.minefy.config.spotify.tokens.description")
                        .option(StringOptionBuilder.create("text.minefy.config.spotify.tokens.access.name",
                                        "text.minefy.config.spotify.tokens.access.description", true)
                                .binding(config.spotify.accessToken, () -> config.spotify.accessToken, val -> config.spotify.accessToken = val)
                                .build())
                        .option(StringOptionBuilder.create("text.minefy.config.spotify.tokens.refresh.name",
                                        "text.minefy.config.spotify.tokens.refresh.description", true)
                                .binding(config.spotify.refreshToken, () -> config.spotify.refreshToken, val -> config.spotify.refreshToken = val)
                                .build())
                        .build())
                .group(OptionGroupBuilder.create("text.minefy.config.spotify.debug.name",
                                "text.minefy.config.spotify.debug.description")
                        .option(ButtonOptionBuilder.create("text.minefy.config.spotify.debug.player.name",
                                        "text.minefy.config.spotify.debug.player.description")
                                .action(SpotifyConfigCategory::testSpotifyPlayerAPI)
                                .build())
                        .build())
                .build();
    }

    private static void testSpotifyPlayerAPI(YACLScreen screen, ButtonOption option) {
        SpotifyAPI.getPlaybackState(ConfigManager.get().spotify.accessToken)
                .thenApply(s -> new SpotifyPlayer().fromJson(Utils.convertToJsonObject(s)))
                .thenAccept(player -> MinefyClient.LOGGER.info(player.toString()));
    }
}
