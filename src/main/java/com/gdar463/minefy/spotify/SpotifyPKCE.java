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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SpotifyPKCE {
    private static final int CODE_VERIFIER_LENGTH = 96;

    public String codeVerifier;

    private void generateCodeVerifier() {
        byte[] randomBytes = new byte[CODE_VERIFIER_LENGTH];
        new SecureRandom().nextBytes(randomBytes);
        codeVerifier = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public String getCodeChallenge() {
        generateCodeVerifier();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] shaVerifier = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(shaVerifier);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
