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

package com.gdar463.minefy.client;

import com.gdar463.minefy.events.HudRenderEvents;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 All credits to the Skyblocker Mod Contributers on GitHub
 Link: https://github.com/SkyblockerMod/Skyblocker/blob/fa9e6b7663c6f81e08e6e3cc1cf25907522ae82a/src/main/java/de/hysky/skyblocker/mixins/InGameHudMixin.java

 Readapted from the original to exclude everything not needed for this project

 All the following code is under the LGPL-3.0-only license
 available at: https://github.com/SkyblockerMod/Skyblocker/blob/fa9e6b7663c6f81e08e6e3cc1cf25907522ae82a/LICENSE
*/
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private LayeredDrawer layeredDrawer;

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDrawer;addLayer(Lnet/minecraft/client/gui/LayeredDrawer$Layer;)Lnet/minecraft/client/gui/LayeredDrawer;", ordinal = 2))
    private LayeredDrawer.Layer minefy$afterMainHud(LayeredDrawer.Layer mainHudLayer) {
        return (context, tickCounter) -> {
            mainHudLayer.render(context, tickCounter);
            HudRenderEvents.AFTER_MAIN_HUD.invoker().onRender(context, tickCounter);
        };
    }

    @ModifyArg(method = "<init>", slice = @Slice(from = @At(value = "NEW", target = "Lnet/minecraft/client/gui/LayeredDrawer;", ordinal = 2)), at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDrawer;addLayer(Lnet/minecraft/client/gui/LayeredDrawer$Layer;)Lnet/minecraft/client/gui/LayeredDrawer;", ordinal = 5))
    private LayeredDrawer.Layer minefy$beforeChat(LayeredDrawer.Layer beforeChatLayer) {
        return (context, tickCounter) -> {
            HudRenderEvents.BEFORE_CHAT.invoker().onRender(context, tickCounter);
            beforeChatLayer.render(context, tickCounter);
        };
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void minefy$afterDrawersInitialized(CallbackInfo ci) {
        this.layeredDrawer.addLayer(HudRenderEvents.LAST.invoker()::onRender);
    }
}