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

package com.gdar463.minefy.config.controllers;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.gui.controllers.string.IStringController;

@SuppressWarnings("ClassCanBeRecord")
public class HiddenStringController implements IStringController<String> {
    private final Option<String> option;

    public HiddenStringController(Option<String> option) {
        this.option = option;
    }

    @Override
    public Option<String> option() {
        return option;
    }

    @Override
    public String getString() {
        //noinspection SuspiciousRegexArgument
        return option().pendingValue().replaceAll(".", "*");
    }

    @Override
    public void setFromString(String s) {
        option().requestSet(s);
    }
}
