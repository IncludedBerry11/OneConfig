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

package org.polyfrost.oneconfig.internal.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.HudRenderEvent;
import org.polyfrost.universal.UMatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class GuiIngameMixin {
    // asm: due to versions it is much easier just to arbitrarily shift back than target something specifically
    // the purpose as it puts us inside the push/pop matrix calls
    @Inject(method = "render", at = @At(value = "TAIL", shift = At.Shift.BY, by = -4))
    //#if MC>=12000
    //$$ private void ocfg$renderHudCallback(net.minecraft.client.gui.DrawContext context, float tickDelta, CallbackInfo ci) {
    //$$     EventManager.INSTANCE.post(new HudRenderEvent(new UMatrixStack(context.getMatrices()), tickDelta));
    //$$ }
    //#else
    private void ocfg$renderHudCallback(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        EventManager.INSTANCE.post(new HudRenderEvent(new UMatrixStack(matrices), tickDelta));
    }
    //#endif
}