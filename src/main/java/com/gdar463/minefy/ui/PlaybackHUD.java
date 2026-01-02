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
import com.gdar463.minefy.spotify.models.state.SpotifyContextType;
import com.gdar463.minefy.spotify.models.state.SpotifyPlayerState;
import com.gdar463.minefy.api.TextureState;
import com.gdar463.minefy.spotify.util.SpotifyURI;
import com.gdar463.minefy.ui.state.DurationSource;
import com.gdar463.minefy.util.ClientUtils;
import com.gdar463.minefy.util.DrawingUtils;
import com.gdar463.minefy.util.PlayerScheduler;
import com.gdar463.minefy.util.Utils;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
//? if !=1.21.1 {
import net.minecraft.client.renderer.RenderPipelines;
        //? }

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PlaybackHUD {
    private static final Logger LOGGER = MinefyClient.LOGGER;
    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final MinefyConfig CONFIG = ConfigManager.get();
    private static final HudConfig.HudThemeConfig HUD_THEME = CONFIG.hud.theme;
    private static final ResourceLocation PLAYER_ICONS = ResourceLocation.fromNamespaceAndPath(MinefyClient.MOD_ID, "textures/gui/player.png");

    public static PlaybackHUD INSTANCE;
    private static Window WINDOW = CLIENT.getWindow();
    public SpotifyPlayer player = SpotifyPlayer.EMPTY;

    private DurationSource durationSource;
    private Duration duration;
    private long lastMeasure = 0;
    private Duration progress;
    private SpotifyURI trackUri;

    private TextMarquee titleMarquee;
    private TextMarquee artistsMarquee;

    private int height;
    private boolean hovered;

    public PlaybackHUD() {
        HudRenderEvents.afterMainHudToRun = this::render;
        if (!CONFIG.spotify.refreshToken.isEmpty()) {
            PlayerScheduler.schedule(() -> getPlayer(true), CONFIG.spotify.updateInterval, TimeUnit.MILLISECONDS);
        } else {
            ClientUtils.sendClientSideMessage(Component.empty()
                    .append(Component.literal("[").withStyle(ChatFormatting.DARK_GREEN))
                    .append(Component.literal("Minefy").withStyle(ChatFormatting.GREEN))
                    .append(Component.literal("] ").withStyle(ChatFormatting.DARK_GREEN))
                    .append(Component.translatable("text.minefy.chat.loggedout.head")
                            .withStyle(ChatFormatting.GOLD))
                    .append(Component.literal("/minefy login")
                            .withStyle(ChatFormatting.RED, ChatFormatting.UNDERLINE)
                            //? if 1.21.1 {
                            /*.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "minefy login"))))
                             *///?} else {
                            .withStyle(style -> style.withClickEvent(new ClickEvent.RunCommand("minefy login"))))
                    //?}
                    .append(Component.translatable("text.minefy.chat.loggedout.end")
                            .withStyle(ChatFormatting.GOLD)));
        }

        height = HUD_THEME.sizes.height;
        LOGGER.debug("PlaybackHUD registered");
    }

    public static void init() {
        INSTANCE = new PlaybackHUD();
    }

    private void render(GuiGraphics ctx, DeltaTracker tickCounter) {
        if (CLIENT.player == null) return;
        if (CLIENT.getDebugOverlay().showDebugScreen()) return;
        if (player == null || player.state != SpotifyPlayerState.READY) return;
        if (!CONFIG.hud.enabled) return;
        if (WINDOW == null)
            WINDOW = CLIENT.getWindow();

        ctx.fill(HUD_THEME.sizes.x, HUD_THEME.sizes.y,
                HUD_THEME.sizes.x + HUD_THEME.sizes.width,
                HUD_THEME.sizes.y + height,
                HUD_THEME.colors.bgColor.getRGB());
        double mouseX = WINDOW != null ? Utils.getScaledX(CLIENT.getWindow(), CLIENT.mouseHandler.xpos()) : -1;
        double mouseY = WINDOW != null ? Utils.getScaledY(CLIENT.getWindow(), CLIENT.mouseHandler.ypos()) : -1;
        hovered = !CLIENT.mouseHandler.isMouseGrabbed() && Utils.pointInBounds(mouseX, mouseY,
                HUD_THEME.sizes.x, HUD_THEME.sizes.y,
                HUD_THEME.sizes.width, height);
        if (hovered) {
            height = HUD_THEME.sizes.activeHeight;
            DrawingUtils.drawBorder(ctx, HUD_THEME.sizes.x, HUD_THEME.sizes.y,
                    HUD_THEME.sizes.width, height,
                    HUD_THEME.colors.activeBorderColor.getRGB(),
                    HUD_THEME.sizes.borderSize);
        } else {
            height = HUD_THEME.sizes.height;
            DrawingUtils.drawBorder(ctx, HUD_THEME.sizes.x, HUD_THEME.sizes.y,
                    HUD_THEME.sizes.width, height,
                    HUD_THEME.colors.borderColor.getRGB(),
                    HUD_THEME.sizes.borderSize);
        }

        switch (player.track.albumCover.textureState) {
            case TextureState.READY:
                //? if 1.21.1 {
                /*ctx.blit(player.track.albumCover.textureId,
                        HUD_THEME.cover.x, HUD_THEME.cover.y,
                        HUD_THEME.cover.size, HUD_THEME.cover.size,
                        0, 0,
                        player.track.albumCover.width, player.track.albumCover.height,
                        player.track.albumCover.width, player.track.albumCover.height);
                *///?} else {
                ctx.blit(RenderPipelines.GUI_TEXTURED, player.track.albumCover.textureId,
                        HUD_THEME.cover.x, HUD_THEME.cover.y,
                        0, 0,
                        HUD_THEME.cover.size, HUD_THEME.cover.size,
                        player.track.albumCover.width, player.track.albumCover.height,
                        player.track.albumCover.width, player.track.albumCover.height);
                //?}
                break;
            case TextureState.ERROR:
                ctx.fill(HUD_THEME.cover.x, HUD_THEME.cover.y,
                        HUD_THEME.cover.x + HUD_THEME.cover.size, HUD_THEME.cover.y + HUD_THEME.cover.size,
                        0xFFFF0000);
                break;
        }

        if (durationSource == DurationSource.DELTA_TIME &&
                this.progress.getSeconds() < this.duration.getSeconds() &&
                this.player.isPlaying) {
            long measure = Util.getMillis();
            this.progress = this.progress.plusMillis(measure - lastMeasure);
            lastMeasure = measure;
        } else {
            lastMeasure = Util.getMillis();
            durationSource = DurationSource.DELTA_TIME;
        }

        int lerpedAmount = Math.toIntExact(progress.toMillis() * HUD_THEME.bar.sizeX / this.duration.toMillis());

        DrawingUtils.pushMatrix(ctx);
        ctx.pose().translate(HUD_THEME.sizes.columnX, HUD_THEME.bar.y/*? 1.21.1 >> ');'*//*, 1*/);
        ctx.fill(0, HUD_THEME.bar.progressY, lerpedAmount, HUD_THEME.bar.progressY + HUD_THEME.bar.sizeY, HUD_THEME.colors.accentColor.getRGB());
        ctx.fill(lerpedAmount, HUD_THEME.bar.progressY, HUD_THEME.bar.sizeX, HUD_THEME.bar.progressY + HUD_THEME.bar.sizeY, HUD_THEME.colors.emptyBarColor.getRGB());
        ctx.pose().scale(HUD_THEME.bar.textScale, HUD_THEME.bar.textScale/*? 1.21.1 >> ');'*//*, 1*/);
        ctx.drawString(CLIENT.font, Utils.durationToString(progress), 0, 0, HUD_THEME.colors.textColor.getRGB(), false);
        ctx.drawString(CLIENT.font, Utils.durationToString(duration), HUD_THEME.bar.sizeX * 2 - (int) (10 / HUD_THEME.bar.textScale), 0, HUD_THEME.colors.textColor.getRGB(), false);
        DrawingUtils.popMatrix(ctx);

        DrawingUtils.pushMatrix(ctx);
        ctx.pose().translate(HUD_THEME.sizes.columnX, HUD_THEME.sizes.columnY/*? 1.21.1 >> ');'*//*, 1*/);
        this.titleMarquee.render(ctx, HUD_THEME.colors.accentColor.getRGB(), false);
        ctx.pose().scale(HUD_THEME.text.artistsScale, HUD_THEME.text.artistsScale/*? 1.21.1 >> ');'*//*, 1*/);
        this.artistsMarquee.render(ctx, HUD_THEME.colors.textColor.getRGB(), false, 0, CLIENT.font.lineHeight + HUD_THEME.text.artistsOffsetY);
        DrawingUtils.popMatrix(ctx);

        if (hovered) {
            drawButton(ctx, 1, player.isPlaying ? 0 : 1, 0);
            drawButton(ctx, 0, 3, 0);
            drawButton(ctx, 2, 2, 0);
            if (player.context != null && player.context.type == SpotifyContextType.PLAYLIST)
                drawButton(ctx, 3, 0, 1);
        }
    }

    //? if fabric && 1.21.1 {
    /*public void onMouseClicked(double x, double y) {
        if (hovered && Utils.pointInBounds(x, y, 72, 55, 82, 65)) {
            if (progress.toMillis() <= 4000)
                SpotifyAPI.skipToPrevious(CONFIG.spotify.accessToken);
            else
                SpotifyAPI.seekToPosition(0, CONFIG.spotify.accessToken);
            return;
        }
        if (hovered && Utils.pointInBounds(x, y, 84, 55, 94, 65)) {
            if (player.isPlaying)
                SpotifyAPI.pausePlayback(CONFIG.spotify.accessToken);
            else
                SpotifyAPI.startPlayback(CONFIG.spotify.accessToken);
            player.isPlaying = !player.isPlaying;
            return;
        }
        if (hovered && Utils.pointInBounds(x, y, 96, 55, 106, 65)) {
            SpotifyAPI.skipToNext(CONFIG.spotify.accessToken);
            return;
        }
        if (player.context != null && player.context.type == SpotifyContextType.PLAYLIST) {
            if (hovered && Utils.pointInBounds(x, y, 108, 55, 118, 65)) {
                SpotifyAPI.addToPlaylist(player.context.uri, player.track.uri, CONFIG.spotify.accessToken)
                        .thenAcceptAsync(s -> {
                            if (s) {
                                ClientUtils.sendClientSideMessage(Component.literal("Added \"" + player.track.name + "\" to current playlist"));
                            } else {
                                ClientUtils.sendClientSideMessage(Component.literal("Failed to add track to current playlist")
                                        .withStyle(ChatFormatting.RED));
                            }
                        });
            }
        }
    }
    *///?} else {
    public boolean onMouseClicked(double x, double y) {
        if (hovered && Utils.pointInBounds(x, y, 72, 55, 82, 65)) {
            if (progress.toMillis() <= 4000)
                SpotifyAPI.skipToPrevious(CONFIG.spotify.accessToken);
            else
                SpotifyAPI.seekToPosition(0, CONFIG.spotify.accessToken);
            return true;
        }
        if (hovered && Utils.pointInBounds(x, y, 84, 55, 94, 65)) {
            if (player.isPlaying)
                SpotifyAPI.pausePlayback(CONFIG.spotify.accessToken);
            else
                SpotifyAPI.startPlayback(CONFIG.spotify.accessToken);
            player.isPlaying = !player.isPlaying;
            return true;
        }
        if (hovered && Utils.pointInBounds(x, y, 96, 55, 106, 65)) {
            SpotifyAPI.skipToNext(CONFIG.spotify.accessToken);
            return true;
        }
        if (player.context != null && player.context.type == SpotifyContextType.PLAYLIST) {
            if (hovered && Utils.pointInBounds(x, y, 108, 55, 118, 65)) {
                SpotifyAPI.addToPlaylist(player.context.uri, player.track.uri, CONFIG.spotify.accessToken)
                        .thenAcceptAsync(s -> {
                            if (s) {
                                ClientUtils.sendClientSideMessage(Component.literal("Added \"" + player.track.name + "\" to current playlist"));
                            } else {
                                ClientUtils.sendClientSideMessage(Component.literal("Failed to add track to current playlist")
                                        .withStyle(ChatFormatting.RED));
                            }
                        });
                return true;
            }
        }
        return false;
    }
    //?}

    public void getPlayer() {
        getPlayer(false);
    }

    public void getPlayer(boolean firstRun) {
        if (CLIENT.player == null) {
            PlayerScheduler.schedule(this::getPlayer, 5, TimeUnit.SECONDS);
            return;
        }
        if (!firstRun && (CONFIG.spotify.accessToken.isEmpty())) {
            if (CONFIG.spotify.refreshToken.isEmpty()) {
                LOGGER.error("tried to go to api without refresh token");
                ClientUtils.sendClientSideMessage(Component.literal("Please login, before trying to access anything"));
                return;
            }
            if (SpotifyAuth.refreshTokens())
                PlayerScheduler.schedule(this::getPlayer, CONFIG.spotify.updateInterval, TimeUnit.MILLISECONDS);
            return;
        }
        SpotifyAPI.getPlaybackState(CONFIG.spotify.accessToken)
                .thenApply(s -> this.player.fromJson(Utils.convertToJsonObject(s)))
                .thenAccept(player -> {
                    if (player.state == SpotifyPlayerState.PARSING) {
                        this.durationSource = DurationSource.PLAYER;
                        this.duration = player.track.duration;
                        this.progress = Duration.of(player.progressMs, ChronoUnit.MILLIS);
                        if (!Objects.equals(player.track.uri, this.trackUri)) {
                            this.trackUri = player.track.uri;
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
                    PlayerScheduler.schedule(this::getPlayer, CONFIG.spotify.updateInterval, TimeUnit.MILLISECONDS);
                });
    }

    private void drawButton(GuiGraphics ctx, int ordinal, int u, int v) {
        //? if 1.21.1 {
        /*ctx.blit(PLAYER_ICONS,
                HUD_THEME.buttons.x + ordinal * (HUD_THEME.buttons.size + HUD_THEME.buttons.offset) + 1, HUD_THEME.buttons.y + 1,
                HUD_THEME.buttons.size - 2, HUD_THEME.buttons.size - 2,
                u * 32, v * 32,
                32, 32,
                128, 128);
        *///?} else {
        ctx.blit(RenderPipelines.GUI_TEXTURED, PLAYER_ICONS,
                HUD_THEME.buttons.x + ordinal * (HUD_THEME.buttons.size + HUD_THEME.buttons.offset) + 1, HUD_THEME.buttons.y + 1,
                u * 32, v * 32,
                HUD_THEME.buttons.size - 2, HUD_THEME.buttons.size - 2,
                32, 32,
                128, 128);
        //?}
        DrawingUtils.drawBorder(ctx, HUD_THEME.buttons.x + ordinal * (HUD_THEME.buttons.size + HUD_THEME.buttons.offset), HUD_THEME.buttons.y,
                HUD_THEME.buttons.size, HUD_THEME.buttons.size, HUD_THEME.colors.activeBorderColor.getRGB(), 1);
    }
}
