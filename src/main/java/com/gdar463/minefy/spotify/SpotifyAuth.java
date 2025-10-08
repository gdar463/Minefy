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
import com.gdar463.minefy.spotify.server.LoginServer;
import com.gdar463.minefy.util.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class SpotifyAuth {
    // For Auth Url
    public static final String SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize";
    public static final String SPOTIFY_SCOPE = "user-read-currently-playing%20user-modify-playback-state%20user-read-playback-state%20user-read-private%20user-read-email";
    public static final String SPOTIFY_RESPONSE_TYPE = "code";
    public static final String SPOTIFY_CODE_METHOD = "S256";

    // For Code exchange
    public static final String SPOTIFY_CODE_URL = "https://accounts.spotify.com/api/token";
    public static final String SPOTIFY_CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String SPOTIFY_GRANT_TYPE = "authorization_code";

    // From Config
    public static final String SPOTIFY_CLIENT_ID;
    public static final String SPOTIFY_REDIRECT_URI;
    public static final int SPOTIFY_CALLBACK_PORT;

    static {
        Config config = ConfigManager.get();
        SPOTIFY_CLIENT_ID = config.spotifyClientId;
        SPOTIFY_REDIRECT_URI = config.spotifyRedirectUri;
        SPOTIFY_CALLBACK_PORT = config.spotifyCallbackPort;
    }

    private static final SpotifyPKCE PKCE_ISTANCE = new SpotifyPKCE();
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    public static void startAuthProcess() {
        String authUrl = SPOTIFY_AUTH_URL +
                "?client_id=" + SPOTIFY_CLIENT_ID +
                "&response_type=" + SPOTIFY_RESPONSE_TYPE +
                "&redirect_uri=" + SPOTIFY_REDIRECT_URI +
                "&scope=" + SPOTIFY_SCOPE +
                "&code_challenge_method=" + SPOTIFY_CODE_METHOD +
                "&code_challenge=" + PKCE_ISTANCE.getCodeChallenge();
        LoginServer.createServer(SPOTIFY_CALLBACK_PORT);
        Utils.openUrl(authUrl);
    }

    public static void processCode(String code) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SPOTIFY_CODE_URL))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", SPOTIFY_CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "client_id: " + SPOTIFY_CLIENT_ID + "," +
                        "grant_type: " + SPOTIFY_GRANT_TYPE + "," +
                        "code: " + code + "," +
                        "redirect_uri: " + SPOTIFY_REDIRECT_URI + "," +
                        "code_verifier: " + PKCE_ISTANCE.codeVerifier +
                        "}"))
                .build();
        HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(SpotifyAuth::processTokens);
    }

    public static void processTokens(String response) {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        Config config = ConfigManager.get();
        config.spotifyAccessToken = jsonObject.get("access_token").getAsString();
        config.spotifyRefreshToken = jsonObject.get("refresh_token").getAsString();
    }
}
