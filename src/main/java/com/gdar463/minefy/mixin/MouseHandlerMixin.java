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

import com.gdar463.minefy.ui.PlaybackHUD;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if !=1.21.1 {
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.input.MouseButtonInfo;
import com.mojang.blaze3d.platform.Window;
//? }

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    //? if 1.21.1 {
    /*@Inject(method = "onPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;afterMouseAction()V"))
    void minefy$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci, @Local(name = "d") double x, @Local(name = "e") double y) {
        if (button == 0 && PlaybackHUD.INSTANCE.player != null) {
            PlaybackHUD.INSTANCE.onMouseClicked(x, y);
        }
    }
    *///?} else {
    @Shadow
    public abstract double getScaledXPos(Window par1);

    @Shadow
    public abstract double getScaledYPos(Window par1);

    @Inject(method = "onButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z"), cancellable = true)
    void minefy$onMouseButton(long window, MouseButtonInfo buttonInfo, int action, CallbackInfo ci, @Local Window window2) {
        if (buttonInfo.button() == 0 && PlaybackHUD.INSTANCE.player != null) {
            if (PlaybackHUD.INSTANCE.onMouseClicked(getScaledXPos(window2), getScaledYPos(window2)))
                ci.cancel();
        }
    }
    //?}
}
//? }