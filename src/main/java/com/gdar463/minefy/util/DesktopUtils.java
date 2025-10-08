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

public class DesktopUtils {
    public static Boolean isLinux;
    public static Boolean isMac;
    public static Boolean isWindows;

    public DesktopUtils() {
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.startsWith("linux")) isLinux = true;
            if (osName.startsWith("max")) isMac = true;
            if (osName.startsWith("windows")) isWindows = true;
        } catch (SecurityException e) {
            // empty
        }
    }
}
