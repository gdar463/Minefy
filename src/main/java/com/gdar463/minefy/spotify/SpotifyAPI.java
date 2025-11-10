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
import com.gdar463.minefy.spotify.models.SpotifyUser;
import com.gdar463.minefy.spotify.models.state.SpotifyPlayerDisallows;
import com.gdar463.minefy.spotify.models.state.SpotifySubscriptionType;
import com.gdar463.minefy.ui.PlaybackHUD;

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
                    if (res == null) return CompletableFuture.completedFuture(null);
                    int code = res.statusCode();
                    if (code == 204) return CompletableFuture.completedFuture("{}");
                    return CompletableFuture.completedStage(res.body());
                });
    }

    public static CompletableFuture<Boolean> startPlayback(String spotifyAccessToken) {
        if (SpotifyUser.INSTANCE.type != SpotifySubscriptionType.PREMIUM ||
                (PlaybackHUD.INSTANCE.player.disallows & SpotifyPlayerDisallows.RESUMING.mask()) != 0)
            return CompletableFuture.completedFuture(false);
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/me/player/play"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody());

        return HTTP_CLIENT.sendAsync(request, spotifyAccessToken)
                .thenApply(s -> true);
    }

    public static CompletableFuture<Boolean> pausePlayback(String spotifyAccessToken) {
        if (SpotifyUser.INSTANCE.type != SpotifySubscriptionType.PREMIUM ||
                (PlaybackHUD.INSTANCE.player.disallows & SpotifyPlayerDisallows.PAUSING.mask()) != 0)
            return CompletableFuture.completedFuture(false);
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/me/player/pause"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody());

        return HTTP_CLIENT.sendAsync(request, spotifyAccessToken)
                .thenApply(s -> true);
    }

    public static CompletableFuture<Boolean> skipToNext(String spotifyAccessToken) {
        if (SpotifyUser.INSTANCE.type != SpotifySubscriptionType.PREMIUM ||
                (PlaybackHUD.INSTANCE.player.disallows & SpotifyPlayerDisallows.SKIPPING_NEXT.mask()) != 0)
            return CompletableFuture.completedFuture(false);
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/me/player/next"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());

        return HTTP_CLIENT.sendAsync(request, spotifyAccessToken)
                .thenApply(s -> true);
    }

    public static CompletableFuture<Boolean> skipToPrevious(String spotifyAccessToken) {
        if (SpotifyUser.INSTANCE.type != SpotifySubscriptionType.PREMIUM ||
                (PlaybackHUD.INSTANCE.player.disallows & SpotifyPlayerDisallows.SKIPPING_PREV.mask()) != 0)
            return CompletableFuture.completedFuture(false);
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/me/player/previous"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());

        return HTTP_CLIENT.sendAsync(request, spotifyAccessToken)
                .thenApply(s -> true);
    }

    public static CompletableFuture<Boolean> seekToPosition(int positionMs, String spotifyAccessToken) {
        if (SpotifyUser.INSTANCE.type != SpotifySubscriptionType.PREMIUM ||
                (PlaybackHUD.INSTANCE.player.disallows & SpotifyPlayerDisallows.SEEKING.mask()) != 0)
            return CompletableFuture.completedFuture(false);
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/me/player/seek?position_ms=" + positionMs))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody());

        return HTTP_CLIENT.sendAsync(request, spotifyAccessToken)
                .thenApply(s -> true);
    }

    public static CompletableFuture<String> getUserProfile(String spotifyAccessToken) {
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/me"))
                .header("Content-Type", "application/json")
                .GET();

        return HTTP_CLIENT.sendAsync(request, spotifyAccessToken).thenApply(HttpResponse::body);
    }
}
