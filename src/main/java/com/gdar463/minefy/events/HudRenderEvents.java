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

package com.gdar463.minefy.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

/*
 All credits to the Skyblocker Mod Contributers on GitHub
 Link: https://github.com/SkyblockerMod/Skyblocker/blob/fa9e6b7663c6f81e08e6e3cc1cf25907522ae82a/src/main/java/de/hysky/skyblocker/events/HudRenderEvents.java

 All the following code is under the LGPL-3.0-only license
 available at: https://github.com/SkyblockerMod/Skyblocker/blob/fa9e6b7663c6f81e08e6e3cc1cf25907522ae82a/LICENSE
*/
public class HudRenderEvents {
    /**
     * Called after the hotbar, status bars, and experience bar have been rendered.
     */
    public static final Event<HudRenderStage> AFTER_MAIN_HUD = createEventForStage();

    /**
     * Called before the {@link net.minecraft.client.gui.hud.ChatHud} is rendered.
     */
    public static final Event<HudRenderStage> BEFORE_CHAT = createEventForStage();

    /**
     * Called after the entire HUD is rendered.
     */
    public static final Event<HudRenderStage> LAST = createEventForStage();

    private static Event<HudRenderStage> createEventForStage() {
        return EventFactory.createArrayBacked(HudRenderStage.class, listeners -> (context, tickDelta) -> {
            for (HudRenderStage listener : listeners) {
                listener.onRender(context, tickDelta);
            }
        });
    }

    /**
     * @implNote Similar to Fabric's {@link net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback}
     */
    @FunctionalInterface
    public interface HudRenderStage {
        /**
         * Called sometime during a specific HUD render stage.
         *
         * @param context     The {@link DrawContext} instance
         * @param tickCounter The {@link RenderTickCounter} instance
         */
        void onRender(DrawContext context, RenderTickCounter tickCounter);
    }
}