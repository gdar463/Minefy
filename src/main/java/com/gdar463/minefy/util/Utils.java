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

package com.gdar463.minefy.util;

import com.gdar463.minefy.MinefyClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Utils {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public static ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return scheduler.schedule(task, delay, unit);
    }

    public static JsonObject convertToJsonObject(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    public static void sendClientSideMessage(Text message) {
        sendClientSideMessage(message, false);
    }

    public static void sendClientSideMessage(Text message, boolean overlay) {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(message, overlay);
    }

    public static void logError(Throwable e) {
        MinefyClient.LOGGER.error("Error occured!\n{}\n{}", e, Arrays.toString(e.getStackTrace()));
    }

    public static void drawBorder(DrawContext ctx, int x, int y, int width, int height, int color, int size) {
        ctx.fill(x, y, x + width, y + size, color);
        ctx.fill(x, y + height - size, x + width, y + height, color);
        ctx.fill(x, y + size, x + size, y + height - size, color);
        ctx.fill(x + width - size, y + size, x + width, y + height - size, color);
    }

    public static void openUrl(String url) {
        if (DesktopUtils.isLinux) {
            //noinspection ResultOfMethodCallIgnored
            Stream.of("xdg-open", "kde-open", "gnome-open").anyMatch(s -> run(new String[]{s, url}));
        }
        if (DesktopUtils.isMac) {
            run(new String[]{"open", url});
        }
        if (DesktopUtils.isWindows) {
            run(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
        }
    }

    private static Boolean run(String[] cmd) {
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            return !proc.waitFor(3, TimeUnit.SECONDS) || proc.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
