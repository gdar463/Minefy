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

package com.gdar463.minefy.events;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

/*
 All credits to the Skyblocker Mod Contributers on GitHub
 Link: https://github.com/SkyblockerMod/Skyblocker/blob/fa9e6b7663c6f81e08e6e3cc1cf25907522ae82a/src/main/java/de/hysky/skyblocker/events/HudRenderEvents.java

 All the following code is under the LGPL-3.0-only license
 available at: https://github.com/SkyblockerMod/Skyblocker/blob/fa9e6b7663c6f81e08e6e3cc1cf25907522ae82a/LICENSE

 2025-12-27 gdar463: ported to 1.21.10 and MojMaps
*/
public class HudRenderEvents {
    public static HudRenderStage afterMainHudToRun = null;

    /**
     * Called after the hotbar, status bars, and experience bar have been rendered.
     */
    public static final HudRenderStage AFTER_MAIN_HUD = (context, tickCounter) -> {
        if (afterMainHudToRun != null) {
            afterMainHudToRun.onRender(context, tickCounter);
        }
    };

    /**
     * @implNote Similar to Fabric's {@link net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback}
     */
    @FunctionalInterface
    public interface HudRenderStage {
        /**
         * Called sometime during a specific HUD render stage.
         *
         * @param context     The {@link GuiGraphics} instance
         * @param tickCounter The {@link DeltaTracker} instance
         */
        void onRender(GuiGraphics context, DeltaTracker tickCounter);
    }
}