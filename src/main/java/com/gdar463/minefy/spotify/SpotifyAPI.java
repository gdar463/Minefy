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

package com.gdar463.minefy.spotify;

import com.gdar463.minefy.spotify.http.WrapperHttpClient;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class SpotifyAPI {
    public static final String API_BASE = "https://api.spotify.com/v1";

    private static final WrapperHttpClient HTTP_CLIENT = new WrapperHttpClient();

    public static CompletableFuture<String> getPlaybackState(String spotifyAccessToken) {
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/me/player"))
                .header("Content-Type", "application/json")
                .GET();

        return HTTP_CLIENT.sendAsync(request, spotifyAccessToken)
                .thenCompose(res -> {
                    int code = res.statusCode();
                    if (code == 204) return CompletableFuture.completedFuture("{}");
                    return CompletableFuture.completedStage(res.body());
                });
    }

    public static CompletableFuture<String> getUserProfile(String spotifyAccessToken) {
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/me"))
                .header("Content-Type", "application/json")
                .GET();

        return HTTP_CLIENT.sendAsync(request, spotifyAccessToken).thenApply(HttpResponse::body);
    }
}
