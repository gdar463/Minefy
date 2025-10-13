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

package com.gdar463.minefy.ui;

import com.gdar463.minefy.MinefyClient;
import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.config.MinefyConfig;
import com.gdar463.minefy.events.HudRenderEvents;
import com.gdar463.minefy.spotify.SpotifyAPI;
import com.gdar463.minefy.spotify.SpotifyAuth;
import com.gdar463.minefy.spotify.models.SpotifyPlayer;
import com.gdar463.minefy.spotify.models.state.SpotifyPlayerState;
import com.gdar463.minefy.spotify.models.state.TextureState;
import com.gdar463.minefy.ui.state.DurationSource;
import com.gdar463.minefy.util.ClientUtils;
import com.gdar463.minefy.util.DrawingUtils;
import com.gdar463.minefy.util.Scheduler;
import com.gdar463.minefy.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.joml.Matrix3x2fStack;
import org.slf4j.Logger;

import java.awt.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PlaybackHUD {
    private static final Color bgColor = new Color(0x50121212, true);
    private static final Color textColor = new Color(0xFFFFFFFF, true);
    private static final Color borderColor = new Color(0x881ED760, true);
    private static final Color accentColor = new Color(0xFF1ED760, true);
    private static final Color emptyBarColor = new Color(0xFF242424, true);
    private static final int width = 178, height = 60;
    private static final int x = 0, y = 0;
    private static final int borderSize = 2;
    private static final float artistsScale = 0.75F, barTextScale = 0.5F;
    private static final int barSizeX = 105, barSizeY = 3;
    private static final int columnX = 61, columnY = 10;
    private static final int barY = 42, barOffsetY = 6;
    private static final int coverX = 7, coverY = 7;
    private static final int coverSize = 46;
    private static final int artistsOffsetY = 6;
    private static final int titleMarqueeCap = 20, titleMarqueeSpaces = 4, titleMarqueeTicks = 25;
    private static final int artistsMarqueeCap = 27, artistsMarqueeSpaces = 6, artistsMarqueeTicks = 40;

    private static final Logger LOGGER = MinefyClient.LOGGER;
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final MinefyConfig CONFIG = ConfigManager.get();
    public static PlaybackHUD INSTANCE;
    public SpotifyPlayer player = SpotifyPlayer.EMPTY;

    private DurationSource durationSource;
    private Duration duration;
    private long lastMeasure = 0;
    private Duration progress;
    private String trackId = "";

    private TextMarquee titleMarquee;
    private TextMarquee artistsMarquee;

    public PlaybackHUD() {
        HudRenderEvents.AFTER_MAIN_HUD.register(this::render);
        Scheduler.schedule(() -> getPlayer(true), 2, TimeUnit.SECONDS);

        LOGGER.debug("PlaybackHUD registered");
    }

    public static void init() {
        INSTANCE = new PlaybackHUD();
    }

    private void render(DrawContext ctx, RenderTickCounter tickCounter) {
        if (CLIENT.getDebugHud().shouldShowDebugHud()) return;
        if (player == null || player.state != SpotifyPlayerState.READY) return;
        if (!CONFIG.hud.enabled) return;

        ctx.fill(x, y, x + width, y + height, DrawingUtils.getRGBA(bgColor));
        DrawingUtils.drawBorder(ctx, x, y, width, height, DrawingUtils.getRGBA(borderColor), borderSize);

        if (player.track.albumCover.textureState == TextureState.READY) {
            ctx.drawTexture(RenderPipelines.GUI_TEXTURED, player.track.albumCover.id, coverX, coverY, 0, 0, coverSize, coverSize, player.track.albumCover.width, player.track.albumCover.height, player.track.albumCover.width, player.track.albumCover.height);
        } else if (player.track.albumCover.textureState == TextureState.NOT_READY) {
            player.track.albumCover.textureState = TextureState.TEXTURIZING;
            player.track.albumCover.texturize();
        }

        if (durationSource == DurationSource.DELTA_TIME &&
                this.progress.getSeconds() < this.duration.getSeconds() &&
                this.player.isPlaying) {
            long measure = Util.getMeasuringTimeMs();
            this.progress = this.progress.plusMillis(measure - lastMeasure);
            lastMeasure = measure;
        } else {
            lastMeasure = Util.getMeasuringTimeMs();
            durationSource = DurationSource.DELTA_TIME;
        }

        int lerpedAmount = Math.toIntExact(progress.toMillis() * barSizeX / this.duration.toMillis());

        Matrix3x2fStack barStack = ctx.getMatrices().pushMatrix();
        barStack.translate(columnX, barY);
        ctx.fill(0, barOffsetY, lerpedAmount, barOffsetY + barSizeY, DrawingUtils.getRGBA(accentColor));
        ctx.fill(lerpedAmount, barOffsetY, barSizeX, barOffsetY + barSizeY, DrawingUtils.getRGBA(emptyBarColor));
        barStack.scale(barTextScale, barTextScale);
        ctx.drawText(CLIENT.textRenderer, Utils.durationToString(progress), 0, 0, DrawingUtils.getRGBA(textColor), false);
        ctx.drawText(CLIENT.textRenderer, Utils.durationToString(duration), barSizeX * 2 - (int) (10 / barTextScale), 0, DrawingUtils.getRGBA(textColor), false);
        barStack.popMatrix();

        Matrix3x2fStack stack = ctx.getMatrices().pushMatrix();
        stack.translate(columnX, columnY);
        this.titleMarquee.render(ctx, DrawingUtils.getRGBA(accentColor), false);
        stack.scale(artistsScale, artistsScale);
        this.artistsMarquee.render(ctx, DrawingUtils.getRGBA(textColor), false, 0, CLIENT.textRenderer.fontHeight + artistsOffsetY);
        stack.popMatrix();
    }

    public void getPlayer() {
        getPlayer(false);
    }

    public void getPlayer(boolean firstRun) {
        if (!firstRun && (CONFIG.spotify.accessToken.isEmpty())) {
            if (CONFIG.spotify.refreshToken.isEmpty()) {
                LOGGER.error("tried to go to api without refresh token");
                ClientUtils.sendClientSideMessage(Text.of("Please login, before trying to access anything"));
                return;
            }
            if (SpotifyAuth.refreshTokens())
                Scheduler.schedule(this::getPlayer, 2, TimeUnit.SECONDS);
            return;
        }
        SpotifyAPI.getPlaybackState(CONFIG.spotify.accessToken)
                .thenApply(s -> this.player.fromJson(Utils.convertToJsonObject(s)))
                .thenAccept(player -> {
                    if (player.state == SpotifyPlayerState.PARSING) {
                        this.durationSource = DurationSource.PLAYER;
                        this.duration = player.track.duration;
                        this.progress = Duration.of(player.progressMs, ChronoUnit.MILLIS);
                        if (!Objects.equals(player.track.id, this.trackId)) {
                            this.trackId = player.track.id;
                            this.titleMarquee = new TextMarquee(player.track.name,
                                    titleMarqueeSpaces,
                                    titleMarqueeCap,
                                    titleMarqueeTicks);
                            this.artistsMarquee = new TextMarquee(player.track.artistsToString(),
                                    artistsMarqueeSpaces,
                                    artistsMarqueeCap,
                                    artistsMarqueeTicks);
                        }
                        this.player.state = SpotifyPlayerState.READY;
                    }
                    Scheduler.schedule(this::getPlayer, 2, TimeUnit.SECONDS);
                });
    }
}
