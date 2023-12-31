/*
 * This file is part of OneConfig.
 * OneConfig - Next Generation Config Library for Minecraft: Java Edition
 * Copyright (C) 2021~2023 Polyfrost.
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

package org.polyfrost.oneconfig.internal.ui.impl;

import org.polyfrost.oneconfig.ui.LwjglManager;
import org.polyfrost.oneconfig.ui.TinyFD;
import org.polyfrost.oneconfig.ui.impl.TinyFDImpl;
import org.polyfrost.polyui.renderer.Renderer;
import org.polyfrost.polyui.renderer.impl.NVGRenderer;
import org.polyfrost.polyui.unit.Vec2;

@SuppressWarnings("unused")
public class LwjglManagerImpl implements LwjglManager {
    private static final TinyFD impl = new TinyFDImpl();

    @Override
    public Renderer getRenderer(float width, float height) {
        return new NVGRenderer(new Vec2(width, height));
    }

    @Override
    public TinyFD getTinyFD() {
        return impl;
    }
}
