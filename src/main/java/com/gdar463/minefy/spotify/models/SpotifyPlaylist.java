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

package com.gdar463.minefy.spotify.models;

import com.gdar463.minefy.api.QuickJsonObject;
import com.gdar463.minefy.api.TextureRenderable;
import com.gdar463.minefy.api.TextureState;
import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.config.MinefyConfig;
import com.gdar463.minefy.spotify.SpotifyAPI;
import com.gdar463.minefy.spotify.models.state.SpotifyPlaylistType;
import com.gdar463.minefy.spotify.util.SpotifyURI;
import com.gdar463.minefy.util.ClientUtils;
import com.gdar463.minefy.util.Utils;
import net.minecraft.network.chat.Component;

public class SpotifyPlaylist extends TextureRenderable {
    private static final MinefyConfig CONFIG = ConfigManager.get();
    private static final String LIBRARY_ICON_URL = "https://misc.scdn.co/liked-songs/liked-songs-300.jpg";

    public SpotifyURI uri;
    public String name;
    public String owner;
    public int tracksTotal;
    public SpotifyPlaylistType type = SpotifyPlaylistType.WRITABLE;

    public SpotifyPlaylist() {
        this.name = "Liked Songs";
        this.type = SpotifyPlaylistType.LIBRARY;
        this.owner = SpotifyUser.INSTANCE.displayName;

        this.textureState = TextureState.NOT_READY;
        CLIENT.execute(() -> this.createTexture(LIBRARY_ICON_URL, "playlist/library"));
    }

    public SpotifyPlaylist(QuickJsonObject json) {
        this.uri = new SpotifyURI(json.getString("uri"));
        this.name = json.getString("name");
        this.owner = json.getJsonObject("owner").getString("display_name");
        this.tracksTotal = json.getJsonObject("tracks").getInt("total");

        if (this.textureState == TextureState.NULL) {
            this.textureState = TextureState.NOT_READY;
            String url = json.getJsonArray("images").get(0).getAsJsonObject().get("url").getAsString();
            CLIENT.execute(() -> this.createTexture(url, Utils.sanitizeURI(this.uri.getUri())));
        }
    }

    public void addToPlaylist(SpotifyURI trackUri) {
        switch (this.type) {
            case LIBRARY -> SpotifyAPI.saveTrack(trackUri.id, CONFIG.spotify.accessToken);
            case WRITABLE ->
                    SpotifyAPI.addToPlaylist(this.uri.id, trackUri.getUri(), CONFIG.spotify.accessToken)
                            .thenAccept(b -> {
                                if (b) {
                                    this.tracksTotal++;
                                    ClientUtils.sendClientSideMessage(Component.literal("Added track to \"" + this.name + "\""));
                                } else {
                                    this.type = SpotifyPlaylistType.READ_ONLY;
                                    ClientUtils.sendClientSideMessage(Component.literal("Couldn't add track to \"" + this.name + "\" as you aren't allowed to add to it"));
                                }
                            });
        }
    }
}
