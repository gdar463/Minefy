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

package com.gdar463.minefy.spotify.models;

import com.gdar463.minefy.MinefyClient;
import com.gdar463.minefy.spotify.models.state.TextureState;
import com.gdar463.minefy.util.ClientUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.slf4j.Logger;

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

public class SpotifyAlbumCover {
    public static final SpotifyAlbumCover EMPTY = new SpotifyAlbumCover();

    private static final Logger LOGGER = MinefyClient.LOGGER;
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    private static final Minecraft CLIENT = Minecraft.getInstance();

    public String url;
    public String trackId;
    public String id;
    public ResourceLocation textureId;
    public TextureState textureState = TextureState.NULL;
    public int height;
    public int width;

    public SpotifyAlbumCover() {
    }

    public void fromJson(JsonArray json, String trackId) {
        JsonObject lastCover = json.get(json.size() - 2).getAsJsonObject();

        this.url = lastCover.get("url").getAsString();
        this.trackId = trackId.replace(":", "/").replaceAll("([A-Z])", "$1$1").toLowerCase();
        this.height = lastCover.get("height").getAsInt();
        this.width = lastCover.get("width").getAsInt();
        this.textureState = TextureState.NOT_READY;
    }

    public void texturize() {
        this.textureState = TextureState.TEXTURIZING;
        this.id = MinefyClient.MOD_ID + "/" + this.trackId;

        LOGGER.debug("texturizing");
        HTTP_CLIENT.sendAsync(HttpRequest.newBuilder()
                                .uri(URI.create(this.url))
                                .build(),
                        HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .thenAccept(bytes -> CLIENT.execute(() -> {
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
                        this.textureId = CLIENT.getTextureManager().register(this.id, texture);
                        texture.upload();
                        *///?} else {
                        ResourceLocation texId = ResourceLocation.fromNamespaceAndPath(MinefyClient.MOD_ID, this.id);
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

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String padding) {
        return "SpotifyAlbumCover {\n" + padding +
                "\turl: " + url + "\n" + padding +
                "\ttrackId: " + trackId + "\n" + padding +
                "\tid: " + id + "\n" + padding +
                "\theight: " + height + "\n" + padding +
                "\twidth: " + width + "\n" + padding +
                "}";
    }
}
