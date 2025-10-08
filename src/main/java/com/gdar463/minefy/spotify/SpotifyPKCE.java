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

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SpotifyPKCE {
    public static final String POSSIBLE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_.-~";
    public static final int CODE_VERIFIER_LENGTH = 128;

    public String codeVerifier;

    private void generateCodeVerifier() {
        SecureRandom random = new SecureRandom(ByteBuffer.allocate(Long.BYTES).putLong(System.currentTimeMillis()).array());
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < CODE_VERIFIER_LENGTH; i++) {
            int rand = random.nextInt(POSSIBLE_CHARS.length());
            codeBuilder.append(POSSIBLE_CHARS.charAt(rand));
        }
        codeVerifier = codeBuilder.toString();
    }

    public String getCodeChallenge() {
        generateCodeVerifier();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] shaVerifier = digest.digest(codeVerifier.getBytes());
            return Base64.getEncoder().encodeToString(shaVerifier);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
