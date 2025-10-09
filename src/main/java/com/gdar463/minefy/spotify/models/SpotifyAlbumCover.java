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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SpotifyAlbumCover {
    public String url;
    public int height;
    public int width;

    public SpotifyAlbumCover(String url, int height, int width) {
        this.url = url;
        this.height = height;
        this.width = width;
    }

    public static SpotifyAlbumCover fromJson(JsonArray json) {
        JsonObject lastCover = json.get(json.size() - 1).getAsJsonObject();
        return new SpotifyAlbumCover(
                lastCover.get("url").getAsString(),
                lastCover.get("height").getAsInt(),
                lastCover.get("width").getAsInt()
        );
    }

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String padding) {
        return "SpotifyAlbumCover {\n" + padding +
                "\turl: " + url + "\n" + padding +
                "\theight: " + height + "\n" + padding +
                "\twidth: " + width + "\n" + padding +
                "}";
    }
}
