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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.BiConsumer;

public class QuickJsonObject {
    private final JsonObject wrapped;

    public QuickJsonObject(JsonObject orig) {
        this.wrapped = orig;
    }

    public String getString(String memberName) {
        return wrapped.get(memberName).getAsString();
    }

    public int getInt(String memberName) {
        return wrapped.get(memberName).getAsInt();
    }

    public boolean getBoolean(String memberName) {
        return wrapped.get(memberName).getAsBoolean();
    }

    public long getLong(String memberName) {
        return wrapped.get(memberName).getAsLong();
    }

    public QuickJsonObject getJsonObject(String memberName) {
        return new QuickJsonObject(wrapped.get(memberName).getAsJsonObject());
    }

    public JsonArray getJsonArray(String memberName) {
        return wrapped.get(memberName).getAsJsonArray();
    }

    public boolean isJsonNull(String memberName) {
        return wrapped.get(memberName) != null && wrapped.get(memberName).isJsonNull();
    }

    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    public void forEach(BiConsumer<? super String, ? super JsonElement> action) {
        wrapped.asMap().forEach(action);
    }
}
