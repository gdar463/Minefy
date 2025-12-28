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

package com.gdar463.minefy.config.builders;

import com.demonwav.mcdev.annotations.Translatable;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class ColorOptionBuilder {
    public static Option.Builder<Color> create(@Translatable String nameKey,
                                               @Translatable String descriptionKey) {
        return create(nameKey, descriptionKey, true);
    }

    public static Option.Builder<Color> create(@Translatable String nameKey,
                                               @Translatable String descriptionKey,
                                               boolean hasAlpha) {
        return Option.<Color>createBuilder()
                .name(Component.translatable(nameKey))
                .description(OptionDescription.of(Component.translatable(descriptionKey)))
                .controller(opt -> ColorControllerBuilder.create(opt)
                        .allowAlpha(hasAlpha));
    }
}
