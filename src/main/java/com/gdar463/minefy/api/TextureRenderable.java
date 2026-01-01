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

package com.gdar463.minefy.api;

import com.gdar463.minefy.MinefyClient;
import com.gdar463.minefy.util.ClientUtils;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TextureRenderable {
    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    public int width, height;
    public ResourceLocation textureId;
    public TextureState textureState = TextureState.NULL;

    public void createTexture(String url, String id) {
        this.textureState = TextureState.TEXTURIZING;
        HTTP_CLIENT.sendAsync(HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .build(),
                        HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .thenAccept(bytes -> CLIENT.execute(() ->
                {
                    try {
                        BufferedImage jpeg = ImageIO.read(new ByteArrayInputStream(bytes));
                        ByteArrayOutputStream pngBytes = new ByteArrayOutputStream();
                        ImageIO.write(jpeg, "PNG", pngBytes);
                        NativeImage image = NativeImage.read(new ByteArrayInputStream(pngBytes.toByteArray()));
                        this.width = image.getWidth();
                        this.height = image.getHeight();
                        //? if 1.21.1 {
                        /*DynamicTexture texture = new DynamicTexture(image);
                        if (this.textureId != null)
                            CLIENT.getTextureManager().release(this.textureId);
                        this.textureId = CLIENT.getTextureManager().register(id, texture);
                        texture.upload();
                        *///?} else {
                        ResourceLocation texId = ResourceLocation.fromNamespaceAndPath(MinefyClient.MOD_ID, id);
                        DynamicTexture texture = new DynamicTexture(texId::toString, image);
                        if (this.textureId != null)
                            CLIENT.getTextureManager().release(this.textureId);
                        CLIENT.getTextureManager().register(texId, texture);
                        this.textureId = texId;
                        //?}
                        this.textureState = TextureState.READY;
                    } catch (IOException e) {
                        this.textureState = TextureState.ERROR;
                        ClientUtils.logError(e);
                    }
                }));
    }
}
