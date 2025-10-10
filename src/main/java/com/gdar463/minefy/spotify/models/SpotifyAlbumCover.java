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

package com.gdar463.minefy.spotify.models;

import com.gdar463.minefy.MinefyClient;
import com.gdar463.minefy.mixin.TextureManagerMixin;
import com.gdar463.minefy.ui.PlaybackHUD;
import com.gdar463.minefy.util.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

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

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public String url;
    public String trackId;
    public Identifier id;
    public boolean texturized;
    public boolean texturizing;
    public int height;
    public int width;

    public SpotifyAlbumCover() {
    }

    public void fromJson(JsonArray json, String trackId) {
        JsonObject lastCover = json.get(json.size() - 1).getAsJsonObject();

        this.url = lastCover.get("url").getAsString();
        this.trackId = trackId.replace(":", "/").replaceAll("([A-Z])", "$1$1").toLowerCase();
        this.height = lastCover.get("height").getAsInt();
        this.width = lastCover.get("width").getAsInt();
        this.texturized = false;
    }

    public void texturize() {
        texturizing = true;
        Identifier prev = this.id;
        this.id = Identifier.of(MinefyClient.MOD_ID, this.trackId);
        if (((TextureManagerMixin) client.getTextureManager()).getTextures().get(id) != null)
            return;
        client.getTextureManager().destroyTexture(prev);

        MinefyClient.LOGGER.info("texturizing");
        HTTP_CLIENT.sendAsync(HttpRequest.newBuilder()
                                .uri(URI.create(this.url))
                                .build(),
                        HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .thenAccept(bytes -> client.execute(() -> {
                    try {
                        BufferedImage jpeg = ImageIO.read(new ByteArrayInputStream(bytes));
                        ByteArrayOutputStream pngBytes = new ByteArrayOutputStream();
                        ImageIO.write(jpeg, "PNG", pngBytes);
                        NativeImage image = NativeImage.read(new ByteArrayInputStream(pngBytes.toByteArray()));
                        client.getTextureManager().registerTexture(id, new NativeImageBackedTexture(() -> this.id.toString(), image));
                        PlaybackHUD.INSTANCE.player.track.albumCover.texturized = true;
                    } catch (IOException e) {
                        Utils.logError(e);
                    } finally {
                        PlaybackHUD.INSTANCE.player.track.albumCover.texturizing = false;
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
