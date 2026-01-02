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

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
//? if !=1.21.1 {
import net.minecraft.client.renderer.RenderPipelines;
//? }

public class DrawingUtils {
    public static void drawBorder(GuiGraphics ctx, int x, int y, int width, int height, int color, int size) {
        ctx.fill(x, y, x + width, y + size, color);
        ctx.fill(x, y + height - size, x + width, y + height, color);
        ctx.fill(x, y + size, x + size, y + height - size, color);
        ctx.fill(x + width - size, y + size, x + width, y + height - size, color);
    }

    public static void pushMatrix(GuiGraphics ctx) {
        //? if 1.21.1 {
        /*ctx.pose().pushPose();
         *///? } else {
        ctx.pose().pushMatrix();
        //? }
    }

    public static void popMatrix(GuiGraphics ctx) {
        //? if 1.21.1 {
        /*ctx.pose().popPose();
         *///? } else {
        ctx.pose().pushMatrix();
        //? }
    }

    public static void blit(GuiGraphics ctx, ResourceLocation atlas, int x, int y, int u, int v, int width, int height, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        //? if 1.21.1 {
        /*ctx.blit(atlas, x, y, width, height, u, v, uWidth, vHeight, textureWidth, textureHeight);
         *///?} else {
        ctx.blit(RenderPipelines.GUI_TEXTURED, atlas, x, y, u, v, width, height, uWidth, vHeight, textureWidth, textureHeight);
        //?}
    }
}
