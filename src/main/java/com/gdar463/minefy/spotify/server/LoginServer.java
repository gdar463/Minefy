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

package com.gdar463.minefy.spotify.server;

import com.gdar463.minefy.MinefyClient;
import com.gdar463.minefy.util.Utils;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LoginServer {
    private static HttpServer server;
    private static ScheduledFuture<?> timeout;

    public static void createServer(int port) {
        try {
            if (server == null) {
                server = HttpServer.create(new InetSocketAddress(port), 0);
                server.createContext("/callback", new CallbackPage());
                server.start();
                scheduleTimeout();
            } else if (timeout != null) {
                timeout.cancel(true);
                scheduleTimeout();
            }
        } catch (IOException e) {
            MinefyClient.LOGGER.error(e.getMessage());
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.sendMessage(Text.of("Failed to start Callback Server"), false);
        }
    }

    private static void scheduleTimeout() {
        timeout = Utils.schedule(() -> {
            if (server != null) {
                server.stop(0);
                destroyServer();
            }
        }, 2, TimeUnit.MINUTES);
    }

    public static void destroyServer() {
        server = null;
    }
}
