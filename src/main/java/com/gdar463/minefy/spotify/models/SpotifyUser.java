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

import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.config.MinefyConfig;
import com.gdar463.minefy.spotify.SpotifyAPI;
import com.gdar463.minefy.spotify.models.state.SpotifySubscriptionType;
import com.gdar463.minefy.util.Utils;
import com.google.gson.JsonObject;


public class SpotifyUser {
    public static final SpotifyUser INSTANCE = new SpotifyUser();
    private static final MinefyConfig CONFIG = ConfigManager.get();
    public String email;
    public SpotifySubscriptionType type;

    public SpotifyUser() {
    }

    public static void init() {
        if (!CONFIG.spotify.accessToken.isEmpty())
            SpotifyAPI.getUserProfile(CONFIG.spotify.accessToken)
                    .thenApply(s -> SpotifyUser.INSTANCE.fromJson(Utils.convertToJsonObject(s)));
    }

    public SpotifyUser fromJson(JsonObject json) {
        if (json.isEmpty()) return this;
        this.email = json.get("email").getAsString();

        switch (json.get("product").getAsString()) {
            case "free" -> this.type = SpotifySubscriptionType.FREE;
            case "premium" -> this.type = SpotifySubscriptionType.PREMIUM;
            default -> this.type = SpotifySubscriptionType.OTHER;
        }

        return this;
    }

    @Override
    public String toString() {
        return "SpotifyUser {\n" +
                "\temail: " + email + "\n" +
                "\ttype: " + type + "\n" +
                "}";
    }
}
