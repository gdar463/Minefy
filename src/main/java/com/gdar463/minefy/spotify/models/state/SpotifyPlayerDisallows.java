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

package com.gdar463.minefy.spotify.models.state;

public enum SpotifyPlayerDisallows {
    INTERRUPTING_PLAYBACK,
    PAUSING,
    RESUMING,
    SEEKING,
    SKIPPING_NEXT,
    SKIPPING_PREV,
    TOGGLING_REPEAT_CONTEXT,
    TOGGLING_SHUFFLE,
    TOGGLING_REPEAT_TRACK,
    TRASFERRING_PLAYBACK;

    public int mask() {
        return 1 << this.ordinal();
    }
}
