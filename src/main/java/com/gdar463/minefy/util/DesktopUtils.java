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

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class DesktopUtils {
    public static final Boolean IS_LINUX = System.getProperty("os.name").toLowerCase().startsWith("linux");
    public static final Boolean IS_MAC = System.getProperty("os.name").toLowerCase().startsWith("mac");
    public static final Boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("windows");

    public static void openUrl(String url) {
        if (IS_LINUX) {
            //noinspection ResultOfMethodCallIgnored
            Stream.of("xdg-open", "kde-open", "gnome-open").anyMatch(s -> run(new String[]{s, url}));
        }
        if (IS_MAC) {
            run(new String[]{"open", url});
        }
        if (IS_WINDOWS) {
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
