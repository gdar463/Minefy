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

import com.google.gson.JsonObject;

import java.time.Duration;
import java.util.Arrays;

public class SpotifyTrack {
    public String name;
    public String[] artists;
    public Duration duration;
    public SpotifyAlbumCover albumCover;

    public SpotifyTrack(String name, Object[] artists, Duration duration, SpotifyAlbumCover albumCover) {
        this.name = name;
        this.artists = (String[]) artists;
        this.duration = duration;
        this.albumCover = albumCover;
    }

    public static SpotifyTrack fromJson(JsonObject json) {
        return new SpotifyTrack(
                json.get("name").getAsString(),
                json.get("artists").getAsJsonArray().asList().stream().map(element -> element.getAsJsonObject().get("name").getAsString()).toArray(),
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
                "\tname: " + name + "\n" + padding +
                "\tartists: " + Arrays.toString(artists) + "\n" + padding +
                "\tduration: " + duration.toMillis() + "\n" + padding +
                "\talbumCover: " + albumCover.toString(padding + "\t") + "\n" + padding +
                "}";
    }
}
