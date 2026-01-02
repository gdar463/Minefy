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

import com.gdar463.minefy.api.TextureState;
import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.config.MinefyConfig;
import com.gdar463.minefy.config.configs.HudConfig;
import com.gdar463.minefy.spotify.models.SpotifyPlaylist;
import com.gdar463.minefy.spotify.models.SpotifyTrack;
import com.gdar463.minefy.spotify.models.SpotifyUser;
import com.gdar463.minefy.spotify.models.state.SpotifyPlaylistType;
import com.gdar463.minefy.spotify.util.SpotifyURI;
import com.gdar463.minefy.util.DrawingUtils;
import com.gdar463.minefy.util.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
//? if !=1.21.1 {
import net.minecraft.client.input.MouseButtonEvent;
//? }

public class SaveToPlaylistScreen extends Screen {
    public static final int ICON_SIZE = 48;
    public static final int ICON_OFFSET = 6;
    public static final int BORDER_OFFSET = 10;
    public static final int TEXT_OFFSET = 4;

    private static final MinefyConfig CONFIG = ConfigManager.get();
    private static final HudConfig.HudThemeConfig HUD_THEME = CONFIG.hud.theme;

    public SpotifyURI trackUri;
    public String trackName;
    public Integer listLength;

    private int maxLength = 6;
    private int scrollOffset = 0;

    private int newWidth;
    private int newHeight;
    private int x;
    private int y;

    public SaveToPlaylistScreen(SpotifyTrack track) {
        super(Component.literal("Save to Playlist"));
        this.trackUri = track.uri;
        this.trackName = Utils.cutoffString(track.name, 20);
    }

    @Override
    public void render(GuiGraphics ctx, int mouseX, int mouseY, float delta) {
        if (this.listLength == null) {
            this.listLength = SpotifyUser.INSTANCE.playlists.size();
        }
        this.maxLength = ctx.guiHeight() / (ICON_SIZE + ICON_OFFSET + 40);
        super.render(ctx, mouseX, mouseY, delta);
        this.newWidth = Math.min(this.width / 5 * 2, 240);
        this.newHeight = Math.min((BORDER_OFFSET * 2) + this.font.lineHeight + TEXT_OFFSET + (ICON_SIZE * this.maxLength) + (ICON_OFFSET * (this.maxLength - 1)),
                (BORDER_OFFSET * 2) + this.font.lineHeight + TEXT_OFFSET + (ICON_SIZE * this.listLength) + (ICON_OFFSET * (this.listLength - 1)));
        this.x = (this.width / 2) - (newWidth / 2);
        this.y = (this.height / 2) - (newHeight / 2);
        int j = this.x + BORDER_OFFSET;
        int k = this.y + BORDER_OFFSET;

        DrawingUtils.drawBorder(ctx, this.x, this.y, this.newWidth, this.newHeight, HUD_THEME.colors.activeBorderColor.getRGB(), 1);
        ctx.drawString(this.font, "Save \"" + this.trackName + "\"", j, k, HUD_THEME.colors.textColor.getRGB(), true);

        k += this.font.lineHeight + TEXT_OFFSET;
        int n = 0;
        for (SpotifyPlaylist playlist : SpotifyUser.INSTANCE.playlists) {
            if (playlist.type != SpotifyPlaylistType.READ_ONLY) {
                if (!this.canScroll() || n >= this.scrollOffset && n < this.maxLength + this.scrollOffset) {
                    switch (playlist.textureState) {
                        case TextureState.READY -> DrawingUtils.blit(ctx, playlist.textureId,
                                j, k,
                                0, 0,
                                ICON_SIZE, ICON_SIZE,
                                playlist.width, playlist.height,
                                playlist.width, playlist.height
                        );
                        case TextureState.ERROR -> ctx.fill(j, k,
                                j + ICON_SIZE, k + ICON_SIZE,
                                0xFFFF0000);
                    }

                    ctx.drawString(this.font, playlist.name, j + ICON_SIZE + 4, k + 4, 0xFFFFFFFF, true);
                    DrawingUtils.pushMatrix(ctx);
                    ctx.pose().scale(0.8f, 0.8f/*? 1.21.1 >> ');'*//*, 1*/);
                    String subtitle = "by " + playlist.owner;
                    if (playlist.type != SpotifyPlaylistType.LIBRARY) {
                        subtitle += " - " + playlist.tracksTotal + " track(s)";
                    }
                    ctx.drawString(this.font, subtitle,
                            (int) ((j + ICON_SIZE + 4) * (1 / 0.8f)), (int) ((k + 4 + this.font.lineHeight + 2) * (1 / 0.8f)),
                            0xFFEEEEEE, true);
                    DrawingUtils.popMatrix(ctx);
                    k += ICON_SIZE + ICON_OFFSET;
                }
            }
            n++;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            if (this.canScroll()) {
                int j = this.listLength - this.maxLength;
                this.scrollOffset = Mth.clamp((int) (this.scrollOffset - scrollY), 0, j);
            }
        }
        return true;
    }

    //? if 1.21.1 {
    /*@Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button))
            return true;
        if (button == 0) {
            int x1 = this.x;
            int y1 = this.y + BORDER_OFFSET + this.font.lineHeight + TEXT_OFFSET;
            int x2 = this.x + this.newWidth;
            int y2 = this.y + this.newHeight - BORDER_OFFSET;
            if (Utils.pointInBounds(mouseX, mouseY, x1, y1, x2, y2)) {
                int len = this.listLength - this.scrollOffset;
                SpotifyUser.INSTANCE.playlists.get(Math.abs((x2 - x1) / ((x2 - x1) / len) - 1 - len) + this.scrollOffset).addToPlaylist(trackUri);
                assert this.minecraft != null;
                this.minecraft.setScreen(null);
                return true;
            }
        }
        return false;
    }
    *///? } else {
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        if (super.mouseClicked(event, isDoubleClick))
            return true;
        if (event.button() == 0) {
            double d = event.x();
            double e = event.y();
            int x1 = this.x;
            int y1 = this.y + BORDER_OFFSET + this.font.lineHeight + TEXT_OFFSET;
            int x2 = this.x + this.newWidth;
            int y2 = this.y + this.newHeight - BORDER_OFFSET;
            if (Utils.pointInBounds(d, e, x1, y1, x2, y2)) {
                int len = this.listLength - this.scrollOffset;
                SpotifyUser.INSTANCE.playlists.get(Math.abs((x2 - x1) / ((x2 - x1) / len) - 1 - len) + this.scrollOffset).addToPlaylist(trackUri);
                assert this.minecraft != null;
                this.minecraft.setScreen(null);
                return true;
            }
        }
        return false;
    }
    //? }

    private boolean canScroll() {
        return this.listLength > this.maxLength;
    }
}
