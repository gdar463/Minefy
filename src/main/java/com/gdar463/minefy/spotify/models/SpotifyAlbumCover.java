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

package com.gdar463.minefy.spotify.models;

import com.gdar463.minefy.MinefyClient;
import com.gdar463.minefy.api.TextureRenderable;
import com.gdar463.minefy.api.TextureState;
import com.gdar463.minefy.util.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Objects;

public class SpotifyAlbumCover extends TextureRenderable {
    public static final SpotifyAlbumCover EMPTY = new SpotifyAlbumCover();

    public String trackId;
    public String id;

    public SpotifyAlbumCover() {
    }

    public void fromJson(JsonArray json, String trackId) {
        JsonObject lastCover = json.get(json.size() - 2).getAsJsonObject();

        if (!Objects.equals(this.trackId, trackId)) {
            this.trackId = trackId;
            this.textureState = TextureState.NOT_READY;
            this.id = MinefyClient.MOD_ID + "/" + Utils.sanitizeURI(trackId);
            this.createTexture(lastCover.get("url").getAsString(), id);
        }
    }

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String padding) {
        return "SpotifyAlbumCover {\n" + padding +
                "\ttrackId: " + trackId + "\n" + padding +
                "\tid: " + id + "\n" + padding +
                "\theight: " + height + "\n" + padding +
                "\twidth: " + width + "\n" + padding +
                "}";
    }
}
