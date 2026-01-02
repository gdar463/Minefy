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

package com.gdar463.minefy;

import com.gdar463.minefy.config.ConfigManager;
import com.gdar463.minefy.spotify.models.SpotifyUser;
import com.gdar463.minefy.ui.PlaybackHUD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//? if fabric {
import net.minecraft.client.gui.screens.Screen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
//?} elif neoforge {
/*import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import com.gdar463.minefy.util.PlayerScheduler;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import com.gdar463.minefy.events.HudRenderEvents;
import com.gdar463.minefy.spotify.models.state.SpotifyPlayerState;
*///?}

public class MinefyClient {
    public static final String MOD_ID = "minefy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    //? if fabric {
    public static Screen toOpen;
    public static int screenTicks = 0;
    //?}

    public static void onInitializeClient() {
        LOGGER.debug("Initializing MinefyClient");
        ConfigManager.init();

        //? if fabric {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (toOpen != null) {
                screenTicks++;
                if (screenTicks > 3) {
                    client.setScreen(toOpen);
                    toOpen = null;
                    screenTicks = 0;
                }
            }
        });
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> PlaybackHUD.init());
        //?}

        SpotifyUser.init();
        LOGGER.info("MinefyClient initialized");
    }

    //? if neoforge {
    /*public static void onClientLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        PlaybackHUD.init();
    }

    public static void onClientLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        if (PlaybackHUD.INSTANCE != null) {
            PlayerScheduler.stopAll();
            PlaybackHUD.INSTANCE.player.state = SpotifyPlayerState.NULL;
        }
    }

    public static void onRenderGuiLayerPost(RenderGuiLayerEvent.Post event) {
        if (event.getName() == VanillaGuiLayers.BOSS_OVERLAY) {
            HudRenderEvents.AFTER_MAIN_HUD.onRender(event.getGuiGraphics(), event.getPartialTick());
        }
    }

    public static boolean onScreenMouseClickedPre(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() == 0 && PlaybackHUD.INSTANCE != null && PlaybackHUD.INSTANCE.player != null)
            return PlaybackHUD.INSTANCE.onMouseClicked(event.getMouseX(), event.getMouseY());
        return false;
    }
    *///?}
}