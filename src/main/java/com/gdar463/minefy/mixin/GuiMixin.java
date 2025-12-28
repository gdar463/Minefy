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
//? if fabric {
package com.gdar463.minefy.mixin;

import com.gdar463.minefy.events.HudRenderEvents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if 1.21.1 {
/*import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
*///?}

/*
  All credits to the Skyblocker Mod Contributers for this idea on GitHub
  Link: https://github.com/SkyblockerMod/Skyblocker/blob/fa9e6b7663c6f81e08e6e3cc1cf25907522ae82a/src/main/java/de/hysky/skyblocker/mixins/InGameHudMixin.java

  Older version was Skyblocker's code, now it's been updated by me (gdar463),
  but the idea is still theirs

  2025-12-27 gdar463: ported to 1.21.11
*/
@Mixin(Gui.class)
public abstract class GuiMixin {
    //? if 1.21.1 {
    /*@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDraw;add(Lnet/minecraft/client/gui/LayeredDraw;Ljava/util/function/BooleanSupplier;)Lnet/minecraft/client/gui/LayeredDraw;"))
    private void minefy$afterMainHud(Minecraft minecraft, CallbackInfo ci, @Local(ordinal = 0) LayeredDraw layeredDraw) {
        layeredDraw.add(HudRenderEvents.AFTER_MAIN_HUD::onRender);
    }
    *///?} else {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderBossOverlay(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", shift = At.Shift.AFTER))
    private void minefy$afterMainHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        HudRenderEvents.AFTER_MAIN_HUD.onRender(guiGraphics, deltaTracker);
    }
    //?}
}
//?}