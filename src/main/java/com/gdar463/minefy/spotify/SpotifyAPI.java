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

package com.gdar463.minefy.spotify;

import com.gdar463.minefy.config.Config;
import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.spotify.exceptions.BadTokenException;
import com.gdar463.minefy.spotify.exceptions.TooManyRequestsException;
import com.gdar463.minefy.spotify.models.SpotifyPlayer;
import com.gdar463.minefy.util.Utils;
import net.minecraft.text.Text;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class SpotifyAPI {
    public static final String API_BASE = "https://api.spotify.com/v1";

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    public static CompletableFuture<SpotifyPlayer> getPlaybackState() {
        Config config = ConfigManager.get();
        if (config.spotifyAccessToken == null) {
            Utils.sendClientSideMessage(Text.literal("Login needed before trying to connect to API"));
            return CompletableFuture.completedFuture(null);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/me/player"))
                .header("Authorization", "Bearer " + config.spotifyAccessToken)
                .header("Content-Type", "application/json")
                .GET().build();
        return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(res -> {
                    int code = res.statusCode();
                    return switch (code) {
                        case 204 -> CompletableFuture.completedFuture(null);
                        case 401 -> throw new BadTokenException();
                        case 429 -> throw new TooManyRequestsException();
                        default -> CompletableFuture.completedStage(res.body());
                    };
                })
                .thenApply(s -> Utils.convertFromJson(s, SpotifyPlayer::fromJson));
    }
}
