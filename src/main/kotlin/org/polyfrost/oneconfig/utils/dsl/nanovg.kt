/*
 * This file is part of OneConfig.
 * OneConfig - Next Generation Config Library for Minecraft: Java Edition
 * Copyright (C) 2021~2023 Polyfrost.
 *   <https://polyfrost.cc> <https://github.com/Polyfrost/>
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
 * <https://polyfrost.cc/legal/oneconfig/additional-terms>
 */

package org.polyfrost.oneconfig.utils.dsl

import org.polyfrost.oneconfig.renderer.NanoVGHelper
import org.polyfrost.oneconfig.renderer.asset.Image
import org.polyfrost.oneconfig.renderer.asset.SVG
import org.polyfrost.oneconfig.renderer.font.Font
import org.polyfrost.oneconfig.utils.InputHandler

val nanoVGHelper: NanoVGHelper
    get() = NanoVGHelper.INSTANCE

/**
 * Wrapper for a NanoVG instance.
 * @see nanoVG
 * @see NanoVGHelper.INSTANCE
 */
@JvmInline
value class VG(val instance: Long)

/**
 * Sets up rendering, calls the block with the NanoVG instance, and then cleans up.
 *
 * To start, call this method.
 * ```kt
 * nanoVG {
 *     // Do stuff with the NanoVG instance
 * }
 * ```
 * From there, you can use the passed [VG] object to draw things. For example...
 * ```kt
 * nanoVG {
 *     drawRect(1, 1, 100, 100, Color.RED.rgb)
 *     drawText("Hello, world!", 10, 10, Color.BLACK.rgb, 9, Fonts.BOLD)
 * }
 * ```
 * You can also set the [mcScaling] parameter to true to scale the NanoVG instance to match the Minecraft GUI scale.
 *
 * @param mcScaling Whether to scale the NanoVG instance to match the Minecraft GUI scale.
 * @param block The block to run.
 */
fun nanoVG(mcScaling: Boolean = false, block: VG.() -> Unit) = nanoVGHelper.setupAndDraw(mcScaling) {
    block.invoke(
        VG(it)
    )
}

fun nanoVG(context: Long, block: VG.() -> Unit) = block.invoke(VG(context))

fun Long.drawRect(x: Number, y: Number, width: Number, height: Number, color: Int, bypassOneConfig: Boolean = false) =
    if (bypassOneConfig) {
        nanoVGHelper.drawRect(this, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color)
    } else {
        nanoVGHelper.drawRect(this, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color)
    }

fun VG.drawRect(x: Number, y: Number, width: Number, height: Number, color: Int, bypassOneConfig: Boolean = false) =
    instance.drawRect(x, y, width, height, color, bypassOneConfig)

fun Long.drawRoundedRect(x: Number, y: Number, width: Number, height: Number, radius: Number, color: Int) =
    nanoVGHelper.drawRoundedRect(
        this, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color, radius.toFloat()
    )

fun VG.drawRoundedRect(x: Number, y: Number, width: Number, height: Number, radius: Number, color: Int) =
    instance.drawRoundedRect(x, y, width, height, radius, color)

fun Long.drawHollowRoundedRect(
    x: Number, y: Number, width: Number, height: Number, radius: Number, color: Int, thickness: Number
) = nanoVGHelper.drawHollowRoundRect(
    this, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color, radius.toFloat(), thickness.toFloat()
)

fun VG.drawHollowRoundedRect(
    x: Number, y: Number, width: Number, height: Number, radius: Number, color: Int, thickness: Number
) = instance.drawHollowRoundedRect(x, y, width, height, radius, color, thickness)

fun Long.drawRoundedRectVaried(
    x: Number,
    y: Number,
    width: Number,
    height: Number,
    color: Int,
    radiusTL: Number,
    radiusTR: Number,
    radiusBR: Number,
    radiusBL: Number
) = nanoVGHelper.drawRoundedRectVaried(
    this,
    x.toFloat(),
    y.toFloat(),
    width.toFloat(),
    height.toFloat(),
    color,
    radiusTL.toFloat(),
    radiusTR.toFloat(),
    radiusBR.toFloat(),
    radiusBL.toFloat()
)

fun VG.drawRoundedRectVaried(
    x: Number,
    y: Number,
    width: Number,
    height: Number,
    color: Int,
    radiusTL: Number,
    radiusTR: Number,
    radiusBR: Number,
    radiusBL: Number
) = instance.drawRoundedRectVaried(x, y, width, height, color, radiusTL, radiusTR, radiusBR, radiusBL)

fun Long.drawGradientRect(x: Number, y: Number, width: Number, height: Number, color1: Int, color2: Int) =
    nanoVGHelper.drawGradientRect(this, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color1, color2)

fun VG.drawGradientRect(x: Number, y: Number, width: Number, height: Number, color1: Int, color2: Int) =
    instance.drawGradientRect(x, y, width, height, color1, color2)

fun Long.drawGradientRoundedRect(
    x: Number, y: Number, width: Number, height: Number, color: Int, color2: Int, radius: Number
) = nanoVGHelper.drawGradientRoundedRect(
    this, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color, color2, radius.toFloat()
)

fun VG.drawGradientRoundedRect(
    x: Number, y: Number, width: Number, height: Number, color: Int, color2: Int, radius: Number
) = instance.drawGradientRoundedRect(x, y, width, height, color, color2, radius)


fun Long.drawCircle(x: Number, y: Number, radius: Number, color: Int) =
    nanoVGHelper.drawCircle(this, x.toFloat(), y.toFloat(), radius.toFloat(), color)

fun VG.drawCircle(x: Number, y: Number, radius: Number, color: Int) = instance.drawCircle(x, y, radius, color)


fun Long.drawText(text: String, x: Number, y: Number, color: Int, size: Number, font: Font) =
    nanoVGHelper.drawText(this, text, x.toFloat(), y.toFloat(), color, size.toFloat(), font)

fun VG.drawText(text: String, x: Number, y: Number, color: Int, size: Number, font: Font) =
    instance.drawText(text, x, y, color, size, font)

fun Long.drawWrappedString(text: String, x: Number, y: Number, width: Number, color: Int, size: Number, lineHeight: Number, font: Font) =
    nanoVGHelper.drawWrappedString(this, text, x.toFloat(), y.toFloat(), width.toFloat(), color, size.toFloat(), lineHeight.toFloat(), font)

fun VG.drawWrappedString(text: String, x: Number, y: Number, width: Number, color: Int, size: Number, lineHeight: Number, font: Font) =
    instance.drawWrappedString(text, x, y, width, color, size, lineHeight, font)

fun Long.drawURL(url: String, x: Number, y: Number, color: Int, size: Number, font: Font, inputHandler: InputHandler) =
    nanoVGHelper.drawURL(this, url, x.toFloat(), y.toFloat(), color, size.toFloat(), font, inputHandler)

fun VG.drawURL(url: String, x: Number, y: Number, color: Int, size: Number, font: Font, inputHandler: InputHandler) =
    instance.drawURL(url, x, y, color, size, font, inputHandler)


@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawImage(filePath, x, y, width, height, javaClass)"))
fun Long.drawImage(filePath: String, x: Number, y: Number, width: Number, height: Number) =
    nanoVGHelper.drawImage(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawImage(filePath, x, y, width, height, javaClass)"))
fun VG.drawImage(filePath: String, x: Number, y: Number, width: Number, height: Number) =
    instance.drawImage(filePath, x, y, width, height)

fun Long.drawImage(filePath: String, x: Number, y: Number, width: Number, height: Number, clazz: Class<*>) =
    nanoVGHelper.drawImage(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), clazz)

fun VG.drawImage(filePath: String, x: Number, y: Number, width: Number, height: Number, clazz: Class<*>) =
    instance.drawImage(filePath, x, y, width, height, clazz)

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawImage(filePath, x, y, width, height, color, javaClass)"))
fun Long.drawImage(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int) =
    nanoVGHelper.drawImage(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color)

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawImage(filePath, x, y, width, height, color, javaClass)"))
fun VG.drawImage(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int) =
    instance.drawImage(filePath, x, y, width, height, color)

fun Long.drawImage(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int, clazz: Class<*>) =
    nanoVGHelper.drawImage(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color, clazz)

fun VG.drawImage(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int, clazz: Class<*>) =
    instance.drawImage(filePath, x, y, width, height, color, clazz)

fun Long.drawImage(image: Image, x: Number, y: Number, width: Number, height: Number) =
    nanoVGHelper.drawImage(this, image, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())

fun VG.drawImage(image: Image, x: Number, y: Number, width: Number, height: Number) =
    instance.drawImage(image, x, y, width, height)

fun Long.drawImage(image: Image, x: Number, y: Number, width: Number, height: Number, color: Int) =
    nanoVGHelper.drawImage(this, image, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color)

fun VG.drawImage(image: Image, x: Number, y: Number, width: Number, height: Number, color: Int) =
    instance.drawImage(image, x, y, width, height, color)


@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawImage(filePath, x, y, width, height, radius, javaClass)"))
fun Long.drawRoundedImage(filePath: String, x: Number, y: Number, width: Number, height: Number, radius: Number) =
    nanoVGHelper.drawRoundImage(
        this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), radius.toFloat()
    )

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawImage(filePath, x, y, width, height, radius, javaClass)"))
fun VG.drawRoundedImage(filePath: String, x: Number, y: Number, width: Number, height: Number, radius: Number) =
    instance.drawRoundedImage(filePath, x, y, width, height, radius)

fun Long.drawRoundedImage(filePath: String, x: Number, y: Number, width: Number, height: Number, radius: Number, clazz: Class<*>) =
    nanoVGHelper.drawRoundImage(
        this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), radius.toFloat(), clazz
    )

fun VG.drawRoundedImage(filePath: String, x: Number, y: Number, width: Number, height: Number, radius: Number, clazz: Class<*>) =
    instance.drawRoundedImage(filePath, x, y, width, height, radius, clazz)

fun Long.drawRoundedImage(image: Image, x: Number, y: Number, width: Number, height: Number, radius: Number) =
    nanoVGHelper.drawRoundImage(
        this, image, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), radius.toFloat()
    )

fun VG.drawRoundedImage(image: Image, x: Number, y: Number, width: Number, height: Number, radius: Number) =
    instance.drawRoundedImage(image, x, y, width, height, radius)


fun Long.getTextWidth(text: String, size: Number, font: Font) =
    nanoVGHelper.getTextWidth(this, text, size.toFloat(), font)

fun VG.getTextWidth(text: String, size: Number, font: Font) = instance.getTextWidth(text, size, font)


fun Long.drawLine(x1: Number, y1: Number, x2: Number, y2: Number, width: Number, color: Int) =
    nanoVGHelper.drawLine(this, x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), width.toFloat(), color)

fun VG.drawLine(x1: Number, y1: Number, x2: Number, y2: Number, width: Number, color: Int) =
    instance.drawLine(x1, y1, x2, y2, width, color)


fun Long.drawDropShadow(
    x: Number, y: Number, w: Number, h: Number, blur: Number, spread: Number, cornerRadius: Number
) = nanoVGHelper.drawDropShadow(
    this, x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat(), blur.toFloat(), spread.toFloat(), cornerRadius.toFloat()
)

fun VG.drawDropShadow(x: Number, y: Number, w: Number, h: Number, blur: Number, spread: Number, cornerRadius: Number) =
    instance.drawDropShadow(x, y, w, h, blur, spread, cornerRadius)

fun Long.scale(x: Float, y: Float) = nanoVGHelper.scale(this, x, y)
fun VG.scale(x: Float, y: Float) = instance.scale(x, y)

fun Long.translate(x: Float, y: Float) = nanoVGHelper.translate(this, x, y)
fun VG.translate(x: Float, y: Float) = instance.translate(x, y)

fun Long.resetTransform() = nanoVGHelper.resetTransform(this)
fun VG.resetTransform() = instance.resetTransform()

fun Long.setAlpha(alpha: Float) = nanoVGHelper.setAlpha(this, alpha)
fun VG.setAlpha(alpha: Float) = instance.setAlpha(alpha)

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawSVG(filePath, x, y, width, height, scale, javaClass)"))
fun Long.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, scale: Number) =
    nanoVGHelper.drawSvg(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), scale.toFloat())

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawSVG(filePath, x, y, width, height, scale, javaClass)"))
fun VG.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, scale: Number) =
    instance.drawSVG(filePath, x, y, width, height, scale)

fun Long.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, scale: Number, clazz: Class<*>) =
    nanoVGHelper.drawSvg(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), scale.toFloat(), clazz)

fun VG.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, scale: Number, clazz: Class<*>) =
    instance.drawSVG(filePath, x, y, width, height, scale, clazz)

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawSVG(filePath, x, y, width, height, javaClass)"))
fun Long.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number) =
    nanoVGHelper.drawSvg(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawSVG(filePath, x, y, width, height, javaClass)"))
fun VG.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number) =
    instance.drawSVG(filePath, x, y, width, height)

fun Long.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, clazz: Class<*>) =
    nanoVGHelper.drawSvg(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), clazz)

fun VG.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, clazz: Class<*>) =
    instance.drawSVG(filePath, x, y, width, height, clazz)

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawSVG(filePath, x, y, width, height, color, scale, javaClass)"))
fun Long.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int, scale: Number) =
    nanoVGHelper.drawSvg(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color, scale.toFloat())

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawSVG(filePath, x, y, width, height, color, scale, javaClass)"))
fun VG.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int, scale: Number) =
    instance.drawSVG(filePath, x, y, width, height, color, scale.toFloat())

fun Long.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int, scale: Number, clazz: Class<*>) =
    nanoVGHelper.drawSvg(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color, scale.toFloat(), clazz)

fun VG.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int, scale: Number, clazz: Class<*>) =
    instance.drawSVG(filePath, x, y, width, height, color, scale.toFloat(), clazz)

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawSVG(filePath, x, y, width, height, color, javaClass)"))
fun Long.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int) =
    nanoVGHelper.drawSvg(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color)

@Deprecated(message = "Doesn't pass class to load resource with", replaceWith = ReplaceWith("drawSVG(filePath, x, y, width, height, color, javaClass)"))
fun VG.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int) =
    instance.drawSVG(filePath, x, y, width, height, color)

fun Long.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int, clazz: Class<*>) =
    nanoVGHelper.drawSvg(this, filePath, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color, clazz)

fun VG.drawSVG(filePath: String, x: Number, y: Number, width: Number, height: Number, color: Int, clazz: Class<*>) =
    instance.drawSVG(filePath, x, y, width, height, color, clazz)

fun Long.drawSVG(svg: SVG, x: Number, y: Number, width: Number, height: Number, scale: Number) =
    nanoVGHelper.drawSvg(this, svg, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), scale.toFloat())

fun VG.drawSVG(svg: SVG, x: Number, y: Number, width: Number, height: Number, scale: Number) =
    instance.drawSVG(svg, x, y, width, height, scale)

fun Long.drawSVG(svg: SVG, x: Number, y: Number, width: Number, height: Number) =
    nanoVGHelper.drawSvg(this, svg, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())

fun VG.drawSVG(svg: SVG, x: Number, y: Number, width: Number, height: Number) =
    instance.drawSVG(svg, x, y, width, height)

fun Long.drawSVG(svg: SVG, x: Number, y: Number, width: Number, height: Number, color: Int, scale: Number) =
    nanoVGHelper.drawSvg(this, svg, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color, scale.toFloat())

fun VG.drawSVG(svg: SVG, x: Number, y: Number, width: Number, height: Number, color: Int, scale: Number) =
    instance.drawSVG(svg, x, y, width, height, color, scale)

fun Long.drawSVG(svg: SVG, x: Number, y: Number, width: Number, height: Number, color: Int) =
    nanoVGHelper.drawSvg(this, svg, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color)

fun VG.drawSVG(svg: SVG, x: Number, y: Number, width: Number, height: Number, color: Int) =
    instance.drawSVG(svg, x, y, width, height, color)