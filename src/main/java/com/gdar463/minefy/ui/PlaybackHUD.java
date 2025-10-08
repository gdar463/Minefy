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

import com.gdar463.minefy.MinefyClient;
import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.events.HudRenderEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class PlaybackHUD {
    public static PlaybackHUD INSTANCE;

    public PlaybackHUD() {
        HudRenderEvents.AFTER_MAIN_HUD.register((this::render));
        MinefyClient.LOGGER.debug("PlaybackHUD registered");
    }

    public static void init() {
        INSTANCE = new PlaybackHUD();
    }

    private void render(DrawContext ctx, RenderTickCounter tickCounter) {
        if (!ConfigManager.get().playbackHudEnabled) return;

        int color = 0xFFFF0000;
        int targetColor = 0xFF00FF00;

        double currentTime = Util.getMeasuringTimeMs() / 1000.0;

        float lerpedAmount = MathHelper.abs(MathHelper.sin((float) currentTime));
        int lerpedColor = ColorHelper.lerp(lerpedAmount, color, targetColor);

        ctx.fill(0, 0, 10, 10, lerpedColor);
    }
}
