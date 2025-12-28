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

package com.gdar463.minefy;

//? if fabric {

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MinefyBootstrap implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MinefyClient.onInitializeClient();
        MinefyCommand.init();
    }
}
//?} elif neoforge {

/*import com.gdar463.minefy.config.ConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = "minefy", dist = Dist.CLIENT)
public class MinefyBootstrap {
    public MinefyBootstrap(FMLModContainer container, IEventBus modBus, Dist dist) {
        ModLoadingContext.get().registerExtensionPoint(
                net.neoforged.neoforge.client.gui.IConfigScreenFactory.class,
                () -> (client, parent) -> ConfigScreen.generate(parent)
        );
        MinefyClient.onInitializeClient();

        NeoForge.EVENT_BUS.addListener(MinefyClient::onClientLoggingIn);
        NeoForge.EVENT_BUS.addListener(MinefyClient::onClientLoggingOut);
        NeoForge.EVENT_BUS.addListener(MinefyClient::onRenderGuiLayerPost);
        NeoForge.EVENT_BUS.addListener(MinefyClient::onScreenMouseClickedPre);
        NeoForge.EVENT_BUS.addListener(MinefyCommand::onClientCommandRegistration);
    }
}
*///?}