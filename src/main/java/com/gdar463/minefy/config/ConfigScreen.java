/*
 Copyright (c) gdar463 (Dario) <dev@gdar463.com>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.gdar463.minefy.config;

import com.gdar463.minefy.config.controllers.HiddenStringControllerBuilder;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen {
    public static Screen generate(Screen parent) {
        return YetAnotherConfigLib.create(ConfigManager.HANDLER, ((defaults, config, builder) -> builder
                        .title(Text.translatable("text.minefy.config.title"))
                        .category(ConfigCategory.createBuilder()
                                .name(Text.translatable("text.minefy.config.category.hud"))
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("text.minefy.config.hud.playback.name"))
                                        .description(OptionDescription.of(Text.translatable("text.minefy.config.hud.playback.description")))
                                        .option(Option.<Boolean>createBuilder()
                                                .name(Text.translatable("text.minefy.config.hud.playback.enabled.name"))
                                                .binding(config.playbackHudEnabled, () -> config.playbackHudEnabled, val -> config.playbackHudEnabled = val)
                                                .controller(TickBoxControllerBuilder::create).build()
                                        ).build()
                                ).build()
                        ).category(ConfigCategory.createBuilder()
                                .name(Text.translatable("text.minefy.config.category.spotify"))
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("text.minefy.config.spotify.client.name"))
                                        .description(OptionDescription.of(Text.translatable("text.minefy.config.spotify.client.description")))
                                        .collapsed(true)
                                        .option(Option.<String>createBuilder()
                                                .name(Text.translatable("text.minefy.config.spotify.client.id.name"))
                                                .description(OptionDescription.of(Text.translatable("text.minefy.config.spotify.client.id.description")))
                                                .binding(config.spotifyClientId, () -> config.spotifyClientId, val -> config.spotifyClientId = val)
                                                .controller(HiddenStringControllerBuilder::create)
                                                .build())
                                        .option(Option.<String>createBuilder()
                                                .name(Text.translatable("text.minefy.config.spotify.client.redirect.name"))
                                                .description(OptionDescription.of(Text.translatable("text.minefy.config.spotify.client.redirect.description")))
                                                .binding(config.spotifyRedirectUri, () -> config.spotifyRedirectUri, val -> config.spotifyRedirectUri = val)
                                                .controller(StringControllerBuilder::create)
                                                .build())
                                        .option(Option.<Integer>createBuilder()
                                                .name(Text.translatable("text.minefy.config.spotify.client.port.name"))
                                                .description(OptionDescription.of(Text.translatable("text.minefy.config.spotify.client.port.description")))
                                                .binding(config.spotifyCallbackPort, () -> config.spotifyCallbackPort, val -> config.spotifyCallbackPort = val)
                                                .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                                                        .range(1024, 65565)
                                                        .formatValue(value -> Text.of(value.toString())))
                                                .build())
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("text.minefy.config.spotify.tokens.name"))
                                        .description(OptionDescription.of(Text.translatable("text.minefy.config.spotify.tokens.description")))
                                        .collapsed(true)
                                        .option(Option.<String>createBuilder()
                                                .name(Text.translatable("text.minefy.config.spotify.tokens.access.name"))
                                                .description(OptionDescription.of(Text.translatable("text.minefy.config.spotify.tokens.access.description")))
                                                .binding(config.spotifyAccessToken, () -> config.spotifyAccessToken, val -> config.spotifyAccessToken = val)
                                                .controller(HiddenStringControllerBuilder::create)
                                                .build())
                                        .option(Option.<String>createBuilder()
                                                .name(Text.translatable("text.minefy.config.spotify.tokens.refresh.name"))
                                                .description(OptionDescription.of(Text.translatable("text.minefy.config.spotify.tokens.refresh.description")))
                                                .binding(config.spotifyRefreshToken, () -> config.spotifyRefreshToken, val -> config.spotifyRefreshToken = val)
                                                .controller(HiddenStringControllerBuilder::create)
                                                .build())
                                        .build())
                                .build())
                        .save(ConfigManager::save)))
                .generateScreen(parent);
    }
}
