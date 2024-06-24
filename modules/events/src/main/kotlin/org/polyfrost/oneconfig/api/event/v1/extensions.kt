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

@file:JvmSynthetic
@file:OptIn(ExperimentalTypeInference::class)

package org.polyfrost.oneconfig.api.event.v1

import org.polyfrost.oneconfig.api.event.v1.events.Event
import org.polyfrost.oneconfig.api.event.v1.invoke.EventHandler
import kotlin.experimental.ExperimentalTypeInference

/**
 * Kotlin specific API for registering of event handlers. Intended usage:
 *
 * ```
 * eventHandler { event: KeyInputEvent ->
 *     println("Key event: $event")
 * }.register()
 * ```
 */
@OverloadResolutionByLambdaReturnType
@JvmName("eventHandlerZ")
@EventHandlerKt
inline fun <reified E : Event> eventHandler(crossinline handler: (E) -> Boolean) = object : EventHandler<E>() {
    override fun handle(event: E) = handler(event)

    override fun getEventClass() = E::class.java
}

/**
 * Kotlin specific API for registering of event handlers. Intended usage:
 *
 * ```
 * eventHandler { event: KeyInputEvent ->
 *     println("Key event: $event")
 * }.register()
 * ```
 */
@OverloadResolutionByLambdaReturnType
@EventHandlerKt
inline fun <reified E : Event> eventHandler(crossinline handler: (E) -> Unit) = object : EventHandler<E>() {
    override fun handle(event: E): Boolean {
        handler(event)
        return false
    }

    override fun getEventClass() = E::class.java
}

/** makes code colored!! */
@DslMarker
private annotation class EventHandlerKt