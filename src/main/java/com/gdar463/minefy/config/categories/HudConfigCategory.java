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
import com.gdar463.minefy.config.builders.*;
import dev.isxander.yacl3.api.ConfigCategory;
import net.minecraft.text.Text;

public class HudConfigCategory {
    public static ConfigCategory create(MinefyConfig config) {
        return ConfigCategory.createBuilder()
                .name(Text.translatable("text.minefy.config.category.hud"))
                .group(OptionGroupBuilder.create("text.minefy.config.hud.playback.name",
                                "text.minefy.config.hud.playback.description", false)
                        .option(BooleanOptionBuilder.create("text.minefy.config.hud.playback.enabled.name",
                                        "text.minefy.config.hud.playback.enabled.description")
                                .binding(config.hud.enabled, () -> config.hud.enabled, val -> config.hud.enabled = val)
                                .build())
                        .build())
                .group(OptionGroupBuilder.create("text.minefy.config.hud.colors.name",
                                "text.minefy.config.hud.colors.description")
                        .option(ColorOptionBuilder.create("text.minefy.config.hud.colors.accent.name",
                                        "text.minefy.config.hud.colors.accent.description")
                                .binding(config.hud.theme.colors.accentColor, () -> config.hud.theme.colors.accentColor, val -> config.hud.theme.colors.accentColor = val)
                                .build())
                        .option(ColorOptionBuilder.create("text.minefy.config.hud.colors.text.name",
                                        "text.minefy.config.hud.colors.text.description")
                                .binding(config.hud.theme.colors.textColor, () -> config.hud.theme.colors.textColor, val -> config.hud.theme.colors.textColor = val)
                                .build())
                        .option(ColorOptionBuilder.create("text.minefy.config.hud.colors.bg.name",
                                        "text.minefy.config.hud.colors.bg.description")
                                .binding(config.hud.theme.colors.bgColor, () -> config.hud.theme.colors.bgColor, val -> config.hud.theme.colors.bgColor = val)
                                .build())
                        .option(ColorOptionBuilder.create("text.minefy.config.hud.colors.border.name",
                                        "text.minefy.config.hud.colors.border.description")
                                .binding(config.hud.theme.colors.borderColor, () -> config.hud.theme.colors.borderColor, val -> config.hud.theme.colors.borderColor = val)
                                .build())
                        .option(ColorOptionBuilder.create("text.minefy.config.hud.colors.activeBorder.name",
                                        "text.minefy.config.hud.colors.activeBorder.description")
                                .binding(config.hud.theme.colors.activeBorderColor, () -> config.hud.theme.colors.activeBorderColor, val -> config.hud.theme.colors.activeBorderColor = val)
                                .build())
                        .option(ColorOptionBuilder.create("text.minefy.config.hud.colors.emptyBar.name",
                                        "text.minefy.config.hud.colors.emptyBar.description")
                                .binding(config.hud.theme.colors.emptyBarColor, () -> config.hud.theme.colors.emptyBarColor, val -> config.hud.theme.colors.emptyBarColor = val)
                                .build())
                        .build())
                .group(OptionGroupBuilder.create("text.minefy.config.hud.sizes.name",
                                "text.minefy.config.hud.sizes.description")
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.sizes.x.name",
                                        "text.minefy.config.hud.sizes.x.description", 0, 350)
                                .binding(config.hud.theme.sizes.x, () -> config.hud.theme.sizes.x, val -> config.hud.theme.sizes.x = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.sizes.y.name",
                                        "text.minefy.config.hud.sizes.y.description", 0, 250)
                                .binding(config.hud.theme.sizes.y, () -> config.hud.theme.sizes.y, val -> config.hud.theme.sizes.y = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.sizes.width.name",
                                        "text.minefy.config.hud.sizes.width.description", 100, 400)
                                .binding(config.hud.theme.sizes.width, () -> config.hud.theme.sizes.width, val -> config.hud.theme.sizes.width = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.sizes.height.name",
                                        "text.minefy.config.hud.sizes.height.description", 40, 150)
                                .binding(config.hud.theme.sizes.height, () -> config.hud.theme.sizes.height, val -> config.hud.theme.sizes.height = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.sizes.activeHeight.name",
                                        "text.minefy.config.hud.sizes.activeHeight.description", 40, 160)
                                .binding(config.hud.theme.sizes.activeHeight, () -> config.hud.theme.sizes.activeHeight, val -> config.hud.theme.sizes.activeHeight = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.sizes.columnX.name",
                                        "text.minefy.config.hud.sizes.columnX.description", 30, 200)
                                .binding(config.hud.theme.sizes.columnX, () -> config.hud.theme.sizes.columnX, val -> config.hud.theme.sizes.columnX = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.sizes.columnY.name",
                                        "text.minefy.config.hud.sizes.columnY.description", 0, 100)
                                .binding(config.hud.theme.sizes.columnY, () -> config.hud.theme.sizes.columnY, val -> config.hud.theme.sizes.columnY = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.sizes.borderSize.name",
                                        "text.minefy.config.hud.sizes.borderSize.description", 0, 10)
                                .binding(config.hud.theme.sizes.borderSize, () -> config.hud.theme.sizes.borderSize, val -> config.hud.theme.sizes.borderSize = val)
                                .build())
                        .build())
                .group(OptionGroupBuilder.create("text.minefy.config.hud.bar.name",
                                "text.minefy.config.hud.bar.description")
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.bar.sizeX.name",
                                        "text.minefy.config.hud.bar.sizeX.description", 30, 250)
                                .binding(config.hud.theme.bar.sizeX, () -> config.hud.theme.bar.sizeX, val -> config.hud.theme.bar.sizeX = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.bar.sizeY.name",
                                        "text.minefy.config.hud.bar.sizeY.description", 1, 6)
                                .binding(config.hud.theme.bar.sizeY, () -> config.hud.theme.bar.sizeY, val -> config.hud.theme.bar.sizeY = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.bar.y.name",
                                        "text.minefy.config.hud.bar.y.description", 20, 80)
                                .binding(config.hud.theme.bar.y, () -> config.hud.theme.bar.y, val -> config.hud.theme.bar.y = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.bar.progressY.name",
                                        "text.minefy.config.hud.bar.progressY.description", 0, 20)
                                .binding(config.hud.theme.bar.progressY, () -> config.hud.theme.bar.progressY, val -> config.hud.theme.bar.progressY = val)
                                .build())
                        .option(FloatFieldOptionBuilder.create("text.minefy.config.hud.bar.textScale.name",
                                        "text.minefy.config.hud.bar.textScale.description", 0.1F, 3F)
                                .binding(config.hud.theme.bar.textScale, () -> config.hud.theme.bar.textScale, val -> config.hud.theme.bar.textScale = val)
                                .build())
                        .build())
                .group(OptionGroupBuilder.create("text.minefy.config.hud.buttons.name",
                                "text.minefy.config.hud.buttons.description")
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.buttons.x.name",
                                        "text.minefy.config.hud.buttons.x.description", 0, 350)
                                .binding(config.hud.theme.buttons.x, () -> config.hud.theme.buttons.x, val -> config.hud.theme.buttons.x = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.buttons.y.name",
                                        "text.minefy.config.hud.buttons.y.description", 0, 250)
                                .binding(config.hud.theme.buttons.y, () -> config.hud.theme.buttons.y, val -> config.hud.theme.buttons.y = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.buttons.size.name",
                                        "text.minefy.config.hud.buttons.size.description", 6, 30)
                                .binding(config.hud.theme.buttons.size, () -> config.hud.theme.buttons.size, val -> config.hud.theme.buttons.size = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.buttons.offset.name",
                                        "text.minefy.config.hud.buttons.size.description", 0, 10)
                                .binding(config.hud.theme.buttons.offset, () -> config.hud.theme.buttons.offset, val -> config.hud.theme.buttons.offset = val)
                                .build())
                        .build())
                .group(OptionGroupBuilder.create("text.minefy.config.hud.cover.name",
                                "text.minefy.config.hud.cover.description")
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.cover.x.name",
                                        "text.minefy.config.hud.cover.x.description", 0, 40)
                                .binding(config.hud.theme.cover.x, () -> config.hud.theme.cover.x, val -> config.hud.theme.cover.x = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.cover.y.name",
                                        "text.minefy.config.hud.cover.y.description", 0, 30)
                                .binding(config.hud.theme.cover.y, () -> config.hud.theme.cover.y, val -> config.hud.theme.cover.y = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.cover.size.name",
                                        "text.minefy.config.hud.cover.size.description", 20, 100)
                                .binding(config.hud.theme.cover.size, () -> config.hud.theme.cover.size, val -> config.hud.theme.cover.size = val)
                                .build())
                        .build())
                .group(OptionGroupBuilder.create("text.minefy.config.hud.text.name",
                                "text.minefy.config.hud.text.description")
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.text.artistsOffsetY.name",
                                        "text.minefy.config.hud.text.artistsOffsetY.description", 0, 40)
                                .binding(config.hud.theme.text.artistsOffsetY, () -> config.hud.theme.text.artistsOffsetY, val -> config.hud.theme.text.artistsOffsetY = val)
                                .build())
                        .option(FloatFieldOptionBuilder.create("text.minefy.config.hud.text.artistsScale.name",
                                        "text.minefy.config.hud.text.artistsScale.description", 0.1F, 3F)
                                .binding(config.hud.theme.text.artistsScale, () -> config.hud.theme.text.artistsScale, val -> config.hud.theme.text.artistsScale = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.text.titleMarqueeCap.name",
                                        "text.minefy.config.hud.text.titleMarqueeCap.description", 0, 60)
                                .binding(config.hud.theme.text.titleMarqueeCap, () -> config.hud.theme.text.titleMarqueeCap, val -> config.hud.theme.text.titleMarqueeCap = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.text.titleMarqueeSpaces.name",
                                        "text.minefy.config.hud.text.titleMarqueeSpaces.description", 0, 15)
                                .binding(config.hud.theme.text.titleMarqueeSpaces, () -> config.hud.theme.text.titleMarqueeSpaces, val -> config.hud.theme.text.titleMarqueeSpaces = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.text.titleMarqueeTicks.name",
                                        "text.minefy.config.hud.text.titleMarqueeTicks.description", 5, 50)
                                .binding(config.hud.theme.text.titleMarqueeTicks, () -> config.hud.theme.text.titleMarqueeTicks, val -> config.hud.theme.text.titleMarqueeTicks = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.text.artistsMarqueeCap.name",
                                        "text.minefy.config.hud.text.artistsMarqueeCap.description", 0, 80)
                                .binding(config.hud.theme.text.artistsMarqueeCap, () -> config.hud.theme.text.artistsMarqueeCap, val -> config.hud.theme.text.artistsMarqueeCap = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.text.artistsMarqueeSpaces.name",
                                        "text.minefy.config.hud.text.artistsMarqueeSpaces.description", 0, 20)
                                .binding(config.hud.theme.text.artistsMarqueeSpaces, () -> config.hud.theme.text.artistsMarqueeSpaces, val -> config.hud.theme.text.artistsMarqueeSpaces = val)
                                .build())
                        .option(IntegerFieldOptionBuilder.create("text.minefy.config.hud.text.artistsMarqueeTicks.name",
                                        "text.minefy.config.hud.text.artistsMarqueeTicks.description", 5, 75)
                                .binding(config.hud.theme.text.artistsMarqueeTicks, () -> config.hud.theme.text.artistsMarqueeTicks, val -> config.hud.theme.text.artistsMarqueeTicks = val)
                                .build())
                        .build())
                .build();
    }
}
