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
    public static Boolean option;

    public static Screen generate(Screen parent) {
        return YetAnotherConfigLib.createBuilder().title(Text.literal("Testing test"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("CategoryName"))
                        .tooltip(Text.literal("Tooltip magical"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("GroupName"))
                                .description(OptionDescription.of(Text.literal("GroupDesc")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("OptionName"))
                                        .description(OptionDescription.of(Text.literal("OptionDesc")))
                                        .binding(true, () -> option, val -> option = val)
                                        .controller(TickBoxControllerBuilder::create).build()
                                ).build()
                        ).build()
                ).build()
                .generateScreen(parent);
    }
}
