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

package com.gdar463.minefy.config;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class MinefyConfig {
    @SerialEntry
    public boolean playbackHudEnabled = true;

    @SerialEntry
    public String spotifyClientId = "494bbac877a94427a5e6137a4a1816cc";

    @SerialEntry
    public String spotifyRedirectUri = "http://127.0.0.1:12589/callback";

    @SerialEntry
    public int spotifyCallbackPort = 12589;

    @SerialEntry
    public String spotifyAccessToken = "";

    @SerialEntry
    public String spotifyRefreshToken = "";
}
