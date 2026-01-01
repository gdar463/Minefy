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

import com.gdar463.minefy.MinefyClient;
import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.config.MinefyConfig;
import com.gdar463.minefy.spotify.exceptions.NoTokenSuppliedException;
import com.gdar463.minefy.spotify.exceptions.RefreshTokenRevokedException;
import com.gdar463.minefy.spotify.models.SpotifyUser;
import com.gdar463.minefy.spotify.server.LoginServer;
import com.gdar463.minefy.ui.PlaybackHUD;
import com.gdar463.minefy.util.DesktopUtils;
import com.gdar463.minefy.util.Utils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static com.gdar463.minefy.util.ClientUtils.logError;
import static com.gdar463.minefy.util.Utils.convertToJsonObject;

public class SpotifyAuth {
    // For Auth Url
    private static final String SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String SPOTIFY_SCOPE = "user-read-currently-playing%20user-modify-playback-state%20user-read-playback-state%20user-read-private%20user-read-email%20playlist-modify-public%20playlist-modify-private%20user-library-modify";
    private static final String SPOTIFY_RESPONSE_TYPE = "code";
    private static final String SPOTIFY_CODE_METHOD = "S256";

    // For Code exchange
    private static final String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String SPOTIFY_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String SPOTIFY_CODE_GRANT_TYPE = "authorization_code";

    // For Refresh token
    private static final String SPOTIFY_REFRESH_GRANT_TYPE = "refresh_token";

    // From Config
    private static final String SPOTIFY_CLIENT_ID;
    private static final String SPOTIFY_REDIRECT_URI;
    private static final int SPOTIFY_CALLBACK_PORT;

    private static final Logger LOGGER = MinefyClient.LOGGER;
    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final MinefyConfig CONFIG = ConfigManager.get();
    private static final SpotifyPKCE PKCE_INSTANCE = new SpotifyPKCE();
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    static {
        SPOTIFY_CLIENT_ID = CONFIG.spotify.oAuthClient.clientId;
        SPOTIFY_REDIRECT_URI = CONFIG.spotify.oAuthClient.redirectUri;
        SPOTIFY_CALLBACK_PORT = CONFIG.spotify.oAuthClient.callbackPort;
    }

    public static void startAuthProcess() {
        String authUrl = SPOTIFY_AUTH_URL +
                "?client_id=" + SPOTIFY_CLIENT_ID +
                "&response_type=" + SPOTIFY_RESPONSE_TYPE +
                "&redirect_uri=" + SPOTIFY_REDIRECT_URI +
                "&scope=" + SPOTIFY_SCOPE +
                "&code_challenge_method=" + SPOTIFY_CODE_METHOD +
                "&code_challenge=" + PKCE_INSTANCE.getCodeChallenge();
        LoginServer.createServer(SPOTIFY_CALLBACK_PORT);
        DesktopUtils.openUrl(authUrl);
    }

    public static void processCode(String code) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SPOTIFY_TOKEN_URL))
                .header("Content-Type", SPOTIFY_CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.ofString("client_id=" + SPOTIFY_CLIENT_ID +
                        "&code=" + code.replace("code=", "") +
                        "&redirect_uri=" + SPOTIFY_REDIRECT_URI +
                        "&grant_type=" + SPOTIFY_CODE_GRANT_TYPE +
                        "&code_verifier=" + PKCE_INSTANCE.codeVerifier))
                .build();
        HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(SpotifyAuth::processTokens);
    }

    public static boolean refreshTokens() {
        return _refreshTokens().thenApply(v -> true).exceptionallyAsync(error -> {
            if (error.getCause() instanceof RefreshTokenRevokedException) {
                CONFIG.spotify.accessToken = "";
                CONFIG.spotify.refreshToken = "";
                ConfigManager.save();
                logError(error.getCause());
                return false;
            }
            logError(error.getCause());
            return false;
        }).join();
    }

    private static void processTokens(String response) {
        JsonObject jsonObject = convertToJsonObject(response);
        CONFIG.spotify.accessToken = jsonObject.get("access_token").getAsString();
        CONFIG.spotify.refreshToken = jsonObject.get("refresh_token").getAsString();
        ConfigManager.save();
        SpotifyAPI.getUserProfile(CONFIG.spotify.accessToken)
                .thenApply(s -> SpotifyUser.INSTANCE.fromJson(Utils.convertToJsonObject(s)));
        if (CLIENT.player != null) PlaybackHUD.INSTANCE.getPlayer();
    }

    private static CompletableFuture<Void> _refreshTokens() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SPOTIFY_TOKEN_URL))
                .header("Content-Type", SPOTIFY_CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.ofString("client_id=" + SPOTIFY_CLIENT_ID +
                        "&refresh_token=" + CONFIG.spotify.refreshToken +
                        "&grant_type=" + SPOTIFY_REFRESH_GRANT_TYPE))
                .build();
        LOGGER.debug("Refreshing tokens...");
        return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(res -> {
                    int code = res.statusCode();
                    if (code == 400 && res.body().contains("refresh")) {
                        throw new NoTokenSuppliedException();
                    }
                    if (code == 401 && res.body().contains("Invalid refresh")) {
                        throw new RefreshTokenRevokedException();
                    }
                    return CompletableFuture.completedStage(res.body());
                }).thenAccept(SpotifyAuth::processTokens);
    }
}
