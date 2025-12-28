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

package com.gdar463.minefy.util;

import com.gdar463.minefy.MinefyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

import java.util.Arrays;

public class ClientUtils {
    private static final Logger LOGGER = MinefyClient.LOGGER;
    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static void sendClientSideMessage(Component message) {
        if (CLIENT.player != null)
            //? if 1.21.1 {
            /*CLIENT.player.sendSystemMessage(message);
             *///?} else {
            CLIENT.player.displayClientMessage(message, false);
        //?}
    }

    public static void logError(Throwable e) {
        LOGGER.error("Error occured!\n{}\n{}", e, Arrays.toString(e.getStackTrace()));
    }
}
