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
import com.gdar463.minefy.config.Config;
import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.events.HudRenderEvents;
import com.gdar463.minefy.spotify.SpotifyAPI;
import com.gdar463.minefy.spotify.SpotifyAuth;
import com.gdar463.minefy.spotify.models.SpotifyPlayer;
import com.gdar463.minefy.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class PlaybackHUD {
    private static final int bgColor = 0x50121212;
    private static final int spotifyColor = 0x1ED760;
    private static final int width = 178, height = 60;
    private static final int x = 0, y = 0;
    private static final float textScale = 0.75F;
    private static final int barSize = 100;

    public static PlaybackHUD INSTANCE;
    private final MinecraftClient client;
    private final Config config;
    public SpotifyPlayer player = SpotifyPlayer.EMPTY;

    private DurationSource durationSource;
    private Duration duration;
    private long progress;

    public PlaybackHUD() {
        this.client = MinecraftClient.getInstance();
        this.config = ConfigManager.get();

        HudRenderEvents.AFTER_MAIN_HUD.register(this::render);
        Utils.schedule(() -> getPlayer(true), 2, TimeUnit.SECONDS);

        MinefyClient.LOGGER.debug("PlaybackHUD registered");
    }

    public static void init() {
        INSTANCE = new PlaybackHUD();
    }

    private void render(DrawContext ctx, RenderTickCounter tickCounter) {
        if (client.getDebugHud().shouldShowDebugHud()) return;
        if (player == null || !player.found) return;
        if (!ConfigManager.get().playbackHudEnabled) return;

        ctx.fill(x, y, x + width, y + height, bgColor);
        Utils.drawBorder(ctx, x, y, width, height, spotifyColor + 0x88000000, 2);

        if (player.track.albumCover.textureState == TextureState.READY) {
            ctx.drawTexture(RenderPipelines.GUI_TEXTURED, player.track.albumCover.id, 7, 7, 0, 0, 46, 46, player.track.albumCover.width, player.track.albumCover.height, player.track.albumCover.width, player.track.albumCover.height);
        } else if (player.track.albumCover.textureState == TextureState.NOT_READY) {
            player.track.albumCover.textureState = TextureState.TEXTURIZING;
            player.track.albumCover.texturize();
        }

        if (this.durationSource != DurationSource.PLAYER) {
            this.duration = this.duration.plusNanos((long) tickCounter.getDynamicDeltaTicks() * 1000);
        }
        this.durationSource = DurationSource.DELTA_TIME;

        int lerpedAmount = Math.toIntExact(this.progress * barSize / this.duration.toMillis());

        ctx.fill(61, 46, 61 + lerpedAmount, 49, spotifyColor + 0xFF000000);
        ctx.fill(61 + lerpedAmount, 46, 61 + barSize, 49, 0xFF242424);

        ctx.getMatrices().pushMatrix();
        ctx.getMatrices().translate(61, 10);
        ctx.drawText(client.textRenderer, player.track.name, 0, 0, spotifyColor + 0xFF000000, false);
        ctx.getMatrices().scale(textScale, textScale);
        ctx.drawText(client.textRenderer, player.track.artists[0], 0, client.textRenderer.fontHeight + 6, 0xFFFFFFFF, false);
        ctx.getMatrices().popMatrix();
    }

    public void getPlayer() {
        getPlayer(false);
    }

    public void getPlayer(boolean firstRun) {
        if (!firstRun && (config.spotifyAccessToken == null || config.spotifyAccessToken.isEmpty())) {
            if (config.spotifyRefreshToken == null || config.spotifyRefreshToken.isEmpty()) {
                MinefyClient.LOGGER.error("tried to go to api without refresh token");
                Utils.sendClientSideMessage(Text.of("Please login, before trying to access anything"));
                return;
            }
            if (SpotifyAuth.refreshTokens()) Utils.schedule(this::getPlayer, 2, TimeUnit.SECONDS);
            return;
        }
        SpotifyAPI.getPlaybackState(config.spotifyAccessToken)
                .thenApply(s -> this.player.fromJson(Utils.convertToJsonObject(s)))
                .thenAccept(player -> {
                    this.player = player;
                    this.durationSource = DurationSource.PLAYER;
                    this.duration = player.track.duration;
                    this.progress = player.progressMs;
                    Utils.schedule(this::getPlayer, 2, TimeUnit.SECONDS);
                });
    }
}
