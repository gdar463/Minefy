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

package com.gdar463.minefy.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.Window;

import java.time.Duration;

public class Utils {
    public static JsonObject convertToJsonObject(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    public static String durationToString(Duration dur) {
        return (int) (dur.getSeconds() / 60) + ":" +
                String.format("%02d", dur.getSeconds() % 60);
    }

    public static boolean pointInBounds(double x, double y, double x1, double y1, double x2, double y2) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    public static double getScaledX(Window window, double x) {
        return x * (double) window.getGuiScaledWidth() / (double) window.getWidth();
    }

    public static double getScaledY(Window window, double y) {
        return y * (double) window.getGuiScaledHeight() / (double) window.getHeight();
    }
}
