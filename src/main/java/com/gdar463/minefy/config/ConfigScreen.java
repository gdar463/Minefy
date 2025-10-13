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

package com.gdar463.minefy.config;

import com.gdar463.minefy.config.categories.PlaybackConfigCategory;
import com.gdar463.minefy.config.categories.SpotifyConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen {
    public static Screen generate(Screen parent) {
        return YetAnotherConfigLib.create(ConfigManager.HANDLER, ((defaults, config, builder) -> builder
                        .title(Text.translatable("text.minefy.config.title"))
                        .category(PlaybackConfigCategory.create(config))
                        .category(SpotifyConfigCategory.create(config))
                        .save(ConfigManager::save)))
                .generateScreen(parent);
    }
}
