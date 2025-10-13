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
import com.gdar463.minefy.config.configs.HudConfig;
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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PlaybackHUD {
    private static final Logger LOGGER = MinefyClient.LOGGER;
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final MinefyConfig CONFIG = ConfigManager.get();
    private static final HudConfig.HudThemeConfig HUD_THEME = CONFIG.hud.theme;

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

        ctx.fill(HUD_THEME.sizes.x, HUD_THEME.sizes.y,
                HUD_THEME.sizes.x + HUD_THEME.sizes.width,
                HUD_THEME.sizes.y + HUD_THEME.sizes.height,
                HUD_THEME.colors.bgColor.getRGB());
        DrawingUtils.drawBorder(ctx, HUD_THEME.sizes.x, HUD_THEME.sizes.y,
                HUD_THEME.sizes.width, HUD_THEME.sizes.height,
                HUD_THEME.colors.borderColor.getRGB(),
                HUD_THEME.sizes.borderSize);

        if (player.track.albumCover.textureState == TextureState.READY) {
            ctx.drawTexture(RenderPipelines.GUI_TEXTURED,
                    player.track.albumCover.id,
                    HUD_THEME.cover.x, HUD_THEME.cover.y, 0, 0,
                    HUD_THEME.cover.size, HUD_THEME.cover.size,
                    player.track.albumCover.width, player.track.albumCover.height,
                    player.track.albumCover.width, player.track.albumCover.height);
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

        int lerpedAmount = Math.toIntExact(progress.toMillis() * HUD_THEME.bar.sizeX / this.duration.toMillis());

        Matrix3x2fStack barStack = ctx.getMatrices().pushMatrix();
        barStack.translate(HUD_THEME.sizes.columnX, HUD_THEME.bar.Y);
        ctx.fill(0, HUD_THEME.bar.progressY, lerpedAmount, HUD_THEME.bar.progressY + HUD_THEME.bar.sizeY, HUD_THEME.colors.accentColor.getRGB());
        ctx.fill(lerpedAmount, HUD_THEME.bar.progressY, HUD_THEME.bar.sizeX, HUD_THEME.bar.progressY + HUD_THEME.bar.sizeY, HUD_THEME.colors.emptyBarColor.getRGB());
        barStack.scale(HUD_THEME.bar.textScale, HUD_THEME.bar.textScale);
        ctx.drawText(CLIENT.textRenderer, Utils.durationToString(progress), 0, 0, HUD_THEME.colors.textColor.getRGB(), false);
        ctx.drawText(CLIENT.textRenderer, Utils.durationToString(duration), HUD_THEME.bar.sizeX * 2 - (int) (10 / HUD_THEME.bar.textScale), 0, HUD_THEME.colors.textColor.getRGB(), false);
        barStack.popMatrix();

        Matrix3x2fStack stack = ctx.getMatrices().pushMatrix();
        stack.translate(HUD_THEME.sizes.columnX, HUD_THEME.sizes.columnY);
        this.titleMarquee.render(ctx, HUD_THEME.colors.accentColor.getRGB(), false);
        stack.scale(HUD_THEME.text.artistsScale, HUD_THEME.text.artistsScale);
        this.artistsMarquee.render(ctx, HUD_THEME.colors.textColor.getRGB(), false, 0, CLIENT.textRenderer.fontHeight + HUD_THEME.text.artistsOffsetY);
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
                                    HUD_THEME.text.titleMarqueeSpaces,
                                    HUD_THEME.text.titleMarqueeCap,
                                    HUD_THEME.text.titleMarqueeTicks);
                            this.artistsMarquee = new TextMarquee(player.track.artistsToString(),
                                    HUD_THEME.text.artistsMarqueeSpaces,
                                    HUD_THEME.text.artistsMarqueeCap,
                                    HUD_THEME.text.artistsMarqueeTicks);
                        }
                        this.player.state = SpotifyPlayerState.READY;
                    }
                    Scheduler.schedule(this::getPlayer, 2, TimeUnit.SECONDS);
                });
    }
}
