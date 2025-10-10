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
import com.gdar463.minefy.spotify.SpotifyAPI;
import com.gdar463.minefy.spotify.models.SpotifyPlayer;
import com.gdar463.minefy.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.concurrent.TimeUnit;

public class PlaybackHUD {
    public static PlaybackHUD INSTANCE;

    private final MinecraftClient client;
    public SpotifyPlayer player = null;

    public PlaybackHUD() {
        this.client = MinecraftClient.getInstance();
        HudRenderEvents.AFTER_MAIN_HUD.register(this::render);
        Utils.schedule(this::getPlayer, 2, TimeUnit.SECONDS);
        MinefyClient.LOGGER.debug("PlaybackHUD registered");
    }

    public static void init() {
        INSTANCE = new PlaybackHUD();
    }

    private void render(DrawContext ctx, RenderTickCounter tickCounter) {
        if (player == null || !player.found) return;
        if (!ConfigManager.get().playbackHudEnabled) return;

        int bgColor = 0x88000000;
        int borderColor = 0x50ffffff;
        int width = 170, height = 70;
        int x = 0, y = 0;

        ctx.fill(x, y, x + width, y + height, bgColor);
        ctx.drawBorder(x, y, width, height, borderColor);

        ctx.drawText(client.textRenderer, player.track.name, 10, 10, 0xFFFFFFFF, false);
        ctx.drawText(client.textRenderer, player.track.artists[0], 10, 30, 0xFFFFFFFF, false);
    }

    public void getPlayer() {
        SpotifyAPI.getPlaybackState().thenAccept(player -> {
            this.player = player;
            Utils.schedule(this::getPlayer, 2, TimeUnit.SECONDS);
        });
    }
}
