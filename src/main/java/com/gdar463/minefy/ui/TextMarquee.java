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

package com.gdar463.minefy.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class TextMarquee {
    private static final TextRenderer TEXT_RENDERER = MinecraftClient.getInstance().textRenderer;

    private final String text;
    private final int textLength;
    private final int maxSize;
    private final int maxTicks;

    private String marquee;
    private int ticks = 0;

    public TextMarquee(String text, int spaces, int maxSize, int maxTicks) {
        this.marquee = text + String.format("%1$" + spaces + "s", "");
        this.text = text;
        this.textLength = text.length();
        this.maxSize = maxSize;
        this.maxTicks = maxTicks;
    }

    public void render(DrawContext ctx, int color, boolean shadow) {
        render(ctx, color, shadow, 0, 0);
    }

    public void render(DrawContext ctx, int color, boolean shadow, int x, int y) {
        if (this.textLength <= this.maxSize) {
            ctx.drawText(TEXT_RENDERER, this.text, x, y, color, shadow);
        } else {
            if (this.ticks >= maxTicks) {
                char first = marquee.charAt(0);
                marquee = marquee.substring(1) + first;
                this.ticks = 0;
            }
            ctx.drawText(TEXT_RENDERER, marquee.substring(0, this.maxSize), x, y, color, shadow);
            this.ticks++;
        }
    }
}
