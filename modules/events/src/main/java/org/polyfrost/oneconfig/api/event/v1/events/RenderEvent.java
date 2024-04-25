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

/**
 * Called when a game tick is started / ended.
 */
public abstract class RenderEvent implements Event {
    /**
     * How much time has elapsed since the last tick, in a fraction of a tick. Used for animations.
     * <b>DO NOT</b> set this value. you will do nothing except potentially interfere with other mods.
     */
    public float deltaTicks;


    public static class Start extends RenderEvent {
        public static final Start INSTANCE = new Start();
        private Start() {}
    }

    public static class End extends RenderEvent {
        public static final End INSTANCE = new End();
        private End() {}
    }

    public float component1() {
        return deltaTicks;
    }
}