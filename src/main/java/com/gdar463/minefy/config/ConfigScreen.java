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

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen {
    public static Screen generate(Screen parent) {
        Config config = ConfigManager.get();

        return YetAnotherConfigLib.createBuilder().title(Text.translatable("text.minefy.config.title"))
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
                ).build()
                .generateScreen(parent);
    }
}
