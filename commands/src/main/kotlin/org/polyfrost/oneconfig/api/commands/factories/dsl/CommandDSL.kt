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

package org.polyfrost.oneconfig.api.commands.factories.dsl

import org.polyfrost.oneconfig.api.commands.arguments.ArgumentParser
import org.polyfrost.oneconfig.api.commands.exceptions.CommandCreationException
import org.polyfrost.oneconfig.api.commands.factories.dsl.CommandDSL.Companion.meta
import org.polyfrost.oneconfig.api.commands.factories.dsl.CommandDSL.ParamData
import org.polyfrost.oneconfig.api.commands.internal.CommandTree
import org.polyfrost.oneconfig.api.commands.internal.Executable
import org.polyfrost.oneconfig.api.commands.internal.Executable.Param
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import java.util.function.Function

/**
 * Command DSL for Kotlin.
 *
 * Uses some 'interesting' hacks to get the method handle of the function passed to the DSL. Unfortunately, at the moment, lambda parameters cannot be annotated,
 * so the [ParamData] class and [meta] function are used to provide metadata for the parameters.
 */
@Suppress("unused")
class CommandDSL @JvmOverloads constructor(private val parsers: List<ArgumentParser<*>>, vararg name: String, description: String? = null) {
    internal val tree = CommandTree(name, description)
    var description: String?
        get() = tree.description
        set(value) {
            tree.description = value
        }

    fun command(
        vararg aliases: String,
        description: String? = null,
        greedy: Boolean = false,
        metadata: List<ParamData> = listOf(),
        func: kotlin.Function<*>
    ) {
        // asm: kotlin compiler produces two methods: public synthetic bridge invoke(Object): Object
        // public final invoke(Object...): Object which is what we want
        val method = func.javaClass.declaredMethods[1]
        val m: MethodHandle
        try {
            if (!method.isAccessible) method.isAccessible = true
            m = MethodHandles.lookup().unreflect(method).bindTo(func)
        } catch (e: Exception) {
            throw CommandCreationException("Error while creating command!", e)
        }
        tree.put(
            Executable(
                aliases,
                description,
                mapParams(method, metadata, parsers),
                greedy,
                Function { return@Function m.invokeWithArguments(*it) })
        )
    }

    fun cmd(
        vararg aliases: String,
        description: String? = null,
        greedy: Boolean = false,
        metadata: List<ParamData> = listOf(),
        func: kotlin.Function<*>
    ) =
        command(*aliases, description = description, greedy = greedy, metadata = metadata, func = func)

    fun subcommand(vararg aliases: String, func: CommandDSL.() -> Unit) {
        tree.put(CommandDSL(parsers, *aliases).apply(func).tree)
    }

    fun subcmd(vararg aliases: String, func: CommandDSL.() -> Unit) = subcommand(*aliases, func = func)

    data class ParamData(val index: Int, val name: String, val description: String? = null, val arity: Int = 1)

    companion object {
        @JvmStatic
        fun command(parsers: List<ArgumentParser<*>>, vararg name: String, description: String? = null, func: CommandDSL.() -> Unit) = CommandDSL(
            parsers, *name, description = description
        ).apply(func)

        @JvmStatic
        fun meta(index: Int, name: String, description: String? = null, arity: Int = 1) =
            ParamData(index, name, description, arity)

        @JvmStatic
        private fun mapParams(method: Method, metadata: List<ParamData>, parsers: List<ArgumentParser<*>>): Array<Param> {
            val params = method.parameters
            return Array(method.parameterCount) {
                val m = metadata.find { data -> data.index == it }
                val type = params[it].type
                Param.create(m?.name ?: type.simpleName, m?.description, type, m?.arity ?: 1, parsers)
            }
        }
    }
}



