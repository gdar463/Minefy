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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class SpotifyTrack {
    public String id;
    public String name;
    public String[] artists;
    public Duration duration;
    public SpotifyAlbumCover albumCover;

    public SpotifyTrack(String id, String name, String[] artists, Duration duration, SpotifyAlbumCover albumCover) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.duration = duration;
        this.albumCover = albumCover;
    }

    public static SpotifyTrack fromJson(JsonObject json) {
        List<JsonElement> artistsJson = json.get("artists").getAsJsonArray().asList();
        String[] artists = new String[artistsJson.size()];
        for (int i = 0; i < artistsJson.size(); i++) {
            artists[i] = artistsJson.get(i).getAsJsonObject().get("name").getAsString();
        }

        return new SpotifyTrack(
                json.get("uri").getAsString(),
                json.get("name").getAsString(),
                artists,
                Duration.ofMillis(json.get("duration_ms").getAsLong()),
                SpotifyAlbumCover.fromJson(json.get("album").getAsJsonObject().get("images").getAsJsonArray())
        );
    }

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String padding) {
        return "SpotifyTrack {\n" + padding +
                "\tid: " + id + "\n" + padding +
                "\tname: " + name + "\n" + padding +
                "\tartists: " + Arrays.toString(artists) + "\n" + padding +
                "\tduration: " + duration.toMillis() + "\n" + padding +
                "\talbumCover: " + albumCover.toString(padding + "\t") + "\n" + padding +
                "}";
    }
}
