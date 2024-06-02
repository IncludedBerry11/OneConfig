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

package org.polyfrost.oneconfig.api.platform.v1;

import org.polyfrost.universal.UMatrixStack;

public interface GLPlatform {
    void drawRect(UMatrixStack stack, double x, double y, double x2, double y2, int color);

    float drawText(UMatrixStack stack, String text, float x, float y, int color, boolean shadow);

    int getStringWidth(String text);

    /**
     * Return the given function address for the specified function with the given name.
     * @param addr the name of the function
     * @return the function address, or NULL (0L) if the function could not be found.
     * @implNote delegates to package-private {@code GLContext.getFunctionAddress(addr)} on legacy and {@code GLFW.getProcAddress(addr)} on modern.
     */
    long getFunctionAddress(String addr);
}
