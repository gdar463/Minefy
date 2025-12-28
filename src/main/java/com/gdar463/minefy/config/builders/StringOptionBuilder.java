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
import com.gdar463.minefy.config.controllers.HiddenStringControllerBuilder;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.network.chat.Component;

public class StringOptionBuilder {
    public static Option.Builder<String> create(@Translatable String nameKey,
                                                @Translatable String descriptionKey) {
        return create(nameKey, descriptionKey, false);
    }

    public static Option.Builder<String> create(@Translatable String nameKey,
                                                @Translatable String descriptionKey,
                                                boolean hidden) {
        return Option.<String>createBuilder()
                .name(Component.translatable(nameKey))
                .description(OptionDescription.of(Component.translatable(descriptionKey)))
                .controller(hidden
                        ? HiddenStringControllerBuilder::create
                        : StringControllerBuilder::create);
    }
}
