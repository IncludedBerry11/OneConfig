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

package org.polyfrost.oneconfig.test;

import org.polyfrost.oneconfig.api.commands.v1.CommandManager;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.InitializationEvent;
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe;

//#if MC<=11202 && FORGE
@net.minecraftforge.fml.common.Mod(modid = "oneconfig-test-mod", name = "Test Mod", version = "0")
//#endif
public class TestMod_Test
//#if FABRIC
//$$ implements net.fabricmc.api.ClientModInitializer
//#endif
{
    public TestMod_Test() {
        EventManager.INSTANCE.register(this);
    }

    //#if FABRIC
    //$$ @Override
    //$$ public void onInitializeClient()
    //#else
    @Subscribe
    private void init(InitializationEvent e)
    //#endif
    {
        System.err.println("TestMod::init");
        CommandManager.registerCommand(new TestCommand_Test());
        new TestConfig_Test();
    }
}