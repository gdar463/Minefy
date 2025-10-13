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

import com.gdar463.minefy.config.MinefyConfig;
import com.gdar463.minefy.config.builders.BooleanOptionBuilder;
import com.gdar463.minefy.config.builders.OptionGroupBuilder;
import dev.isxander.yacl3.api.ConfigCategory;
import net.minecraft.text.Text;

public class PlaybackConfigCategory {
    public static ConfigCategory create(MinefyConfig config) {
        return ConfigCategory.createBuilder()
                .name(Text.translatable("text.minefy.config.category.hud"))
                .group(OptionGroupBuilder.create("text.minefy.config.hud.playback.name",
                                "text.minefy.config.hud.playback.description", false)
                        .option(BooleanOptionBuilder.create("text.minefy.config.hud.playback.enabled.name",
                                        "text.minefy.config.hud.playback.enabled.description")
                                .binding(config.hud.enabled, () -> config.hud.enabled, val -> config.hud.enabled = val)
                                .build())
                        .build()
                ).build();
    }
}
