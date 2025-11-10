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

package com.gdar463.minefy.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

import java.awt.*;

public class HudConfig {
    @SerialEntry
    public boolean enabled = true;

    @SerialEntry
    public HudThemeConfig theme = new HudThemeConfig();

    public static class HudThemeConfig {
        @SerialEntry
        public HudThemeColorsConfig colors = new HudThemeColorsConfig();
        @SerialEntry
        public HudThemeSizesConfig sizes = new HudThemeSizesConfig();
        @SerialEntry
        public HudThemeBarConfig bar = new HudThemeBarConfig();
        @SerialEntry
        public HudThemeCoverConfig cover = new HudThemeCoverConfig();
        @SerialEntry
        public HudThemeTextConfig text = new HudThemeTextConfig();

        public static class HudThemeColorsConfig {
            @SerialEntry
            public Color accentColor = new Color(0xFF1ED760, true);
            @SerialEntry
            public Color textColor = new Color(0xFFFFFFFF, true);
            @SerialEntry
            public Color bgColor = new Color(0x50121212, true);
            @SerialEntry
            public Color borderColor = new Color(0x881ED760, true);
            @SerialEntry
            public Color activeBorderColor = new Color(0xDD1ED760, true);
            @SerialEntry
            public Color emptyBarColor = new Color(0xFF242424, true);
        }

        public static class HudThemeSizesConfig {
            @SerialEntry
            public int x = 0, y = 0;
            @SerialEntry
            public int width = 178, height = 60;
            @SerialEntry
            public int activeHeight = 70;
            @SerialEntry
            public int columnX = 61, columnY = 10;
            @SerialEntry
            public int borderSize = 2;
        }

        public static class HudThemeBarConfig {
            @SerialEntry
            public int sizeX = 105, sizeY = 3;
            @SerialEntry
            public int y = 42, progressY = 6;
            @SerialEntry
            public float textScale = 0.5F;
        }

        public static class HudThemeCoverConfig {
            @SerialEntry
            public int x = 7, y = 7;
            @SerialEntry
            public int size = 46;
        }

        public static class HudThemeTextConfig {
            @SerialEntry
            public int artistsOffsetY = 6;
            @SerialEntry
            public float artistsScale = 0.75F;
            @SerialEntry
            public int titleMarqueeCap = 20,
                    titleMarqueeSpaces = 4,
                    titleMarqueeTicks = 25;
            @SerialEntry
            public int artistsMarqueeCap = 27,
                    artistsMarqueeSpaces = 6,
                    artistsMarqueeTicks = 40;
        }
    }
}
