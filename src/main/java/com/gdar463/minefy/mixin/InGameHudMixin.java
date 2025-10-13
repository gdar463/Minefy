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

package com.gdar463.minefy.mixin;

import com.gdar463.minefy.events.HudRenderEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 All credits to the Skyblocker Mod Contributers for this idea on GitHub
 Link: https://github.com/SkyblockerMod/Skyblocker/blob/fa9e6b7663c6f81e08e6e3cc1cf25907522ae82a/src/main/java/de/hysky/skyblocker/mixins/InGameHudMixin.java

 Older version was Skyblocker's code, now it's been updated by me (gdar463),
 but the idea is still theirs
*/
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderMainHud(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", shift = At.Shift.AFTER))
    private void minefy$afterMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        HudRenderEvents.AFTER_MAIN_HUD.invoker().onRender(context, tickCounter);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderChat(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"))
    private void minefy$beforeChat(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        HudRenderEvents.BEFORE_CHAT.invoker().onRender(context, tickCounter);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void minefy$afterDrawersInitialized(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        HudRenderEvents.LAST.invoker().onRender(context, tickCounter);
    }
}