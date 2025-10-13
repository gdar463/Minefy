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

package com.gdar463.minefy.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class SpotifyConfig {
    @SerialEntry
    public OAuthClient oAuthClient = new OAuthClient();

    @SerialEntry
    public String accessToken = "";

    @SerialEntry
    public String refreshToken = "";

    public static class OAuthClient {
        @SerialEntry
        public String clientId = "494bbac877a94427a5e6137a4a1816cc";

        @SerialEntry
        public String redirectUri = "http://127.0.0.1:12589/callback";

        @SerialEntry
        public int callbackPort = 12589;
    }
}
