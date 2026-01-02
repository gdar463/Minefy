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
import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.config.MinefyConfig;
import com.gdar463.minefy.spotify.SpotifyAPI;
import com.gdar463.minefy.spotify.models.state.SpotifySubscriptionType;
import com.gdar463.minefy.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class SpotifyUser {
    public static final SpotifyUser INSTANCE = new SpotifyUser();
    private static final MinefyConfig CONFIG = ConfigManager.get();

    public String email;
    public String displayName;
    public SpotifySubscriptionType type;
    public List<SpotifyPlaylist> playlists = new ArrayList<>(10);

    public SpotifyUser() {
    }

    public static void init() {
        if (!CONFIG.spotify.accessToken.isEmpty()) {
            SpotifyAPI.getUserProfile(CONFIG.spotify.accessToken)
                    .thenApply(s -> SpotifyUser.INSTANCE.fromJson(Utils.convertToJsonObject(s)))
                    .thenRun(SpotifyUser.INSTANCE::getPlaylists);
        }
    }

    public SpotifyUser fromJson(QuickJsonObject json) {
        if (json.isEmpty()) return this;
        this.email = json.getString("email");
        this.displayName = json.getString("display_name");

        switch (json.getString("product")) {
            case "free" -> this.type = SpotifySubscriptionType.FREE;
            case "premium" -> this.type = SpotifySubscriptionType.PREMIUM;
            default -> this.type = SpotifySubscriptionType.OTHER;
        }

        return this;
    }

    public void getPlaylists() {
        playlists.add(new SpotifyPlaylist());
        SpotifyAPI.getCurrentUsersPlaylist(CONFIG.spotify.accessToken)
                .thenAccept(array -> array.forEach(e -> playlists.add(new SpotifyPlaylist(new QuickJsonObject(e.getAsJsonObject())))));
    }

    @Override
    public String toString() {
        return "SpotifyUser {\n" +
                "\temail: " + email + "\n" +
                "\tdisplayName: " + displayName + "\n" +
                "\ttype: " + type + "\n" +
                "}";
    }
}
