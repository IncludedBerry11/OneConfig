/*
 * This file is part of OneConfig.
 * OneConfig - Next Generation Config Library for Minecraft: Java Edition
 * Copyright (C) 2021~2024 Polyfrost.
 *   <https://polyfrost.org> <https://github.com/Polyfrost/>
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *   OneConfig is licensed under the terms of version 3 of the GNU Lesser
 * General Public License as published by the Free Software Foundation, AND
 * under the Additional Terms Applicable to OneConfig, as published by Polyfrost,
 * either version 1.0 of the Additional Terms, or (at your option) any later
 * version.
 *
 *   This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 * License.  If not, see <https://www.gnu.org/licenses/>. You should
 * have also received a copy of the Additional Terms Applicable
 * to OneConfig, as published by Polyfrost. If not, see
 * <https://polyfrost.org/legal/oneconfig/additional-terms>
 */

package org.polyfrost.oneconfig.api.event.v1.events;

import org.jetbrains.annotations.ApiStatus;
import org.polyfrost.oneconfig.api.hypixel.v0.HypixelAPI;

/**
 * Event that is fired when the player's location is changed.
 */
@ApiStatus.Experimental
public class HypixelLocationEvent implements Event {
    public static final HypixelLocationEvent INSTANCE = new HypixelLocationEvent();

    private HypixelLocationEvent() {
        // call <clinit> on HypixelAPI
        HypixelAPI.getLocation();
    }

    /**
     * Get the location of the player when the event was fired.
     * @return the same as {@link HypixelAPI#getLocation()}.
     */
    public HypixelAPI.Location getLocation() {
        return HypixelAPI.getLocation();
    }

}