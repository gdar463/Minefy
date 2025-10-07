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
