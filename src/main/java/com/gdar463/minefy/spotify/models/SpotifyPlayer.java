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

import java.util.Objects;

public class SpotifyPlayer {
    public static final SpotifyPlayer EMPTY = new SpotifyPlayer();

    public boolean found;
    public boolean isPlaying;
    public long progressMs;

    public SpotifyTrack track = SpotifyTrack.EMPTY;

    public SpotifyPlayer() {
        this.found = false;
    }

    public SpotifyPlayer fromJson(JsonObject json) {
        if (json.isEmpty()) return this;

        this.found = true;
        this.isPlaying = json.get("is_playing").getAsBoolean();
        this.progressMs = json.get("progress_ms").getAsLong();

        String trackUri = json.get("item").getAsJsonObject().get("uri").getAsString();
        if (!Objects.equals(trackUri, this.track.id))
            this.track.fromJson(json.get("item").getAsJsonObject());
        return this;
    }

    @Override
    public String toString() {
        return "SpotifyPlayer {\n" +
                "\tisPlaying: " + isPlaying + "\n" +
                "\tprogress: " + progressMs + "\n" +
                "\ttrack: " + track.toString("\t") + "\n" +
                "}";
    }
}
