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

package com.gdar463.minefy.spotify.http;

import com.gdar463.minefy.config.Config;
import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.spotify.SpotifyAuth;
import com.gdar463.minefy.util.Utils;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class WrapperHttpClient {
    public static final Config config = ConfigManager.get();

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    public WrapperHttpClient() {
    }

    public CompletableFuture<HttpResponse<String>> sendAsync(HttpRequest.Builder builder, String spotifyAccessToken) {
        return HTTP_CLIENT.sendAsync(builder.header("Authorization", "Bearer " + spotifyAccessToken)
                                .build(),
                        HttpResponse.BodyHandlers.ofString())
                .thenComposeAsync(res -> {
                    int code = res.statusCode();
                    return switch (code) {
                        case 400, 401 -> {
                            if (SpotifyAuth.refreshTokens())
                                sendAsync(builder, config.spotifyAccessToken);
                            yield null;
                        }
                        case 429 -> {
                            Utils.schedule(() -> sendAsync(builder, config.spotifyAccessToken),
                                    res.headers().firstValueAsLong("Retry-After").orElse(30),
                                    TimeUnit.SECONDS);
                            yield null;
                        }
                        default -> CompletableFuture.completedStage(res);
                    };
                });
    }
}
