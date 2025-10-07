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

//package com.gdar463.minefy;
//
//import net.minecraft.client.gui.DrawContext;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.widget.ButtonWidget;
//import net.minecraft.client.toast.SystemToast;
//import net.minecraft.text.Text;
//import net.minecraft.util.Colors;
//
//public class CustomScreen extends Screen {
//    public CustomScreen(Text title) {
//        super(title);
//    }
//
//    @Override
//    protected void init() {
//        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("Hello World"), (btn) -> {
//            assert this.client != null;
//            this.client.getToastManager().add(
//                    SystemToast.create(this.client, SystemToast.Type.PERIODIC_NOTIFICATION, Text.of("Hllo"), Text.of("I'm toast"))
//            );
//        }).dimensions(40, 40, 80, 20).build();
//
//        this.addDrawableChild(buttonWidget);
//    }
//
//    @Override
//    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        this.renderBackground(context, mouseX, mouseY, delta);
//        super.render(context, mouseX, mouseY, delta);
//
//        context.drawText(this.textRenderer, "Magic Button", 40, 40 - this.textRenderer.fontHeight - 10, Colors.WHITE, true);
//    }
//}
