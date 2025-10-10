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

import com.gdar463.minefy.MinefyClient;
import com.gdar463.minefy.spotify.SpotifyAuth;
import com.gdar463.minefy.spotify.exceptions.BadTokenException;
import com.gdar463.minefy.spotify.exceptions.NoTokenSuppliedException;
import com.gdar463.minefy.spotify.exceptions.TooManyRequestsException;
import com.gdar463.minefy.util.Utils;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class WrapperHttpClient {
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    public WrapperHttpClient() {
    }

    public CompletableFuture<HttpResponse<String>> sendAsync(HttpRequest request) {
        return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(res -> {
                    int code = res.statusCode();
                    return switch (code) {
                        case 401 -> throw new BadTokenException();
                        case 429 ->
                                throw new TooManyRequestsException(res.headers().firstValueAsLong("Retry-After").orElse(10));
                        default -> CompletableFuture.completedStage(res);
                    };
                })
                .exceptionally(error -> {
                    Throwable cause = error.getCause();
                    if (cause instanceof BadTokenException) {
                        boolean refreshed = SpotifyAuth.refreshTokens().thenApply(v -> true).exceptionally(refreshError -> {
                            if (error.getCause() instanceof NoTokenSuppliedException) {
                                MinefyClient.LOGGER.error(error.getMessage());
                                return false;
                            }
                            MinefyClient.LOGGER.error("Error occured!\n{}\n{}", error.getCause(), Arrays.toString(error.getStackTrace()));
                            return false;
                        }).join();
                        if (refreshed) sendAsync(request);
                        return null;
                    }
                    if (cause instanceof TooManyRequestsException) {
                        Utils.schedule(() -> sendAsync(request), ((TooManyRequestsException) cause).timeOut, TimeUnit.SECONDS);
                    }
                    MinefyClient.LOGGER.error("Error occured!\n{}\n{}", cause, Arrays.toString(error.getStackTrace()));
                    return null;
                });
    }
}
