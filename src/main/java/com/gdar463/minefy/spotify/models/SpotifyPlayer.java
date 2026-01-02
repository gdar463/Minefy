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

import com.gdar463.minefy.api.QuickJsonObject;
import com.gdar463.minefy.spotify.models.state.SpotifyPlayerDisallows;
import com.gdar463.minefy.spotify.models.state.SpotifyPlayerState;

import java.util.Objects;

public class SpotifyPlayer {
    public static final SpotifyPlayer EMPTY = new SpotifyPlayer();

    public boolean isPlaying;
    public long progressMs;
    public SpotifyPlayerState state = SpotifyPlayerState.NULL;
    public int disallows = 0;

    public SpotifyContext context = SpotifyContext.EMPTY;
    public SpotifyTrack track = SpotifyTrack.EMPTY;

    public SpotifyPlayer() {
    }

    public SpotifyPlayer fromJson(QuickJsonObject json) {
        if (json.isEmpty()) return this;
        this.state = SpotifyPlayerState.PARSING;

        this.isPlaying = json.getBoolean("is_playing");
        this.progressMs = json.getLong("progress_ms");

        QuickJsonObject actions = json.getJsonObject("actions").getJsonObject("disallows");
        this.disallows = 0;
        actions.forEach((key, val) -> {
            boolean valBool = val.getAsBoolean();
            switch (key) {
                case "interrupting_playback" ->
                        updateDisallowsWithBool(valBool, SpotifyPlayerDisallows.INTERRUPTING_PLAYBACK);
                case "pausing" -> updateDisallowsWithBool(valBool, SpotifyPlayerDisallows.PAUSING);
                case "resuming" ->
                        updateDisallowsWithBool(valBool, SpotifyPlayerDisallows.RESUMING);
                case "seeking" -> updateDisallowsWithBool(valBool, SpotifyPlayerDisallows.SEEKING);
                case "skipping_next" ->
                        updateDisallowsWithBool(valBool, SpotifyPlayerDisallows.SKIPPING_NEXT);
                case "skipping_prev" ->
                        updateDisallowsWithBool(valBool, SpotifyPlayerDisallows.SKIPPING_PREV);
                case "toggling_repeat_context" ->
                        updateDisallowsWithBool(valBool, SpotifyPlayerDisallows.TOGGLING_REPEAT_CONTEXT);
                case "toggling_shuffle" ->
                        updateDisallowsWithBool(valBool, SpotifyPlayerDisallows.TOGGLING_SHUFFLE);
                case "toggling_repeat_track" ->
                        updateDisallowsWithBool(valBool, SpotifyPlayerDisallows.TOGGLING_REPEAT_TRACK);
                case "transferring_playback" ->
                        updateDisallowsWithBool(valBool, SpotifyPlayerDisallows.TRASFERRING_PLAYBACK);
            }
        });

        String trackUri = json.getJsonObject("item").getString("uri");
        if (!Objects.equals(trackUri, this.track.uri.getUri()))
            this.track.fromJson(json.getJsonObject("item"));

        this.context.fromJson(json.getJsonObject("context"));
        return this;
    }

    private void updateDisallowsWithBool(boolean bool, SpotifyPlayerDisallows perm) {
        if (bool) {
            this.disallows |= perm.mask();
        } else {
            this.disallows &= ~perm.mask();
        }
    }

    @Override
    public String toString() {
        if (state == SpotifyPlayerState.NULL) return "SpotifyPlayer { NULL }";
        return "SpotifyPlayer {\n" +
                "\tstate: " + state + "\n" +
                "\tisPlaying: " + isPlaying + "\n" +
                "\tprogress: " + progressMs + "\n" +
                "\ttrack: " + track.toString("\t") + "\n" +
                "}";
    }
}
