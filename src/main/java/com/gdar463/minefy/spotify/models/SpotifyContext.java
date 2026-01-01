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

import com.gdar463.minefy.spotify.models.state.SpotifyContextType;
import com.gdar463.minefy.spotify.util.SpotifyURI;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public class SpotifyContext {
    public static final SpotifyContext EMPTY = new SpotifyContext();

    public SpotifyContextType type;
    public SpotifyURI uri;

    public SpotifyContext() {
    }

    @SuppressWarnings("UnusedReturnValue")
    @Nullable
    public SpotifyContext fromJson(JsonObject json) {
        if (json.isEmpty()) return null;

        this.type = switch (json.get("type").getAsString()) {
            case "artist" -> SpotifyContextType.ARTIST;
            case "playlist" -> SpotifyContextType.PLAYLIST;
            case "album" -> SpotifyContextType.ALBUM;
            case "show" -> SpotifyContextType.SHOW;
            default -> SpotifyContextType.OTHER;
        };
        this.uri = new SpotifyURI(json.get("uri").getAsString());

        return this;
    }
}
