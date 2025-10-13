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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class SpotifyTrack {
    public static final SpotifyTrack EMPTY = new SpotifyTrack();

    public String id;
    public String name;
    public String[] artists;
    public Duration duration;
    public SpotifyAlbumCover albumCover = SpotifyAlbumCover.EMPTY;

    public SpotifyTrack() {
    }

    private static String[] getArtistsFromJson(JsonArray json) {
        List<JsonElement> arrJson = json.asList();
        String[] arr = new String[arrJson.size()];
        for (int i = 0; i < arrJson.size(); i++) {
            arr[i] = arrJson.get(i).getAsJsonObject().get("name").getAsString();
        }
        return arr;
    }

    public void fromJson(JsonObject json) {
        this.id = json.get("uri").getAsString();
        this.name = json.get("name").getAsString();
        this.artists = getArtistsFromJson(json.get("artists").getAsJsonArray());
        this.duration = Duration.ofMillis(json.get("duration_ms").getAsLong());

        JsonArray albumImages = json.get("album").getAsJsonObject().get("images")
                .getAsJsonArray();
        String coverUrl = albumImages.get(albumImages.size() - 1).getAsJsonObject()
                .get("url").getAsString();
        if (!Objects.equals(coverUrl, this.albumCover.url))
            this.albumCover.fromJson(json.get("album").getAsJsonObject().get("images").getAsJsonArray(), this.id);
    }

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String padding) {
        return "SpotifyTrack {\n" + padding +
                "\tid: " + id + "\n" + padding +
                "\tname: " + name + "\n" + padding +
                "\tartists: " + artistsToString() + "\n" + padding +
                "\tduration: " + duration.toMillis() + "\n" + padding +
                "\talbumCover: " + albumCover.toString(padding + "\t") + "\n" + padding +
                "}";
    }

    public String artistsToString() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < this.artists.length; i++) {
            out.append(artists[i]);
            if (i != this.artists.length - 1) out.append(", ");
        }
        return out.toString();
    }
}
