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

package org.polyfrost.oneconfig.internal.ui.pages

import org.polyfrost.oneconfig.api.config.v1.Tree
import org.polyfrost.oneconfig.api.config.v1.internal.ConfigVisualizer
import org.polyfrost.oneconfig.api.ui.v1.notifications.Notification
import org.polyfrost.oneconfig.internal.ui.OneConfigUI
import org.polyfrost.polyui.PolyUI
import org.polyfrost.polyui.animate.Animations
import org.polyfrost.polyui.component.*
import org.polyfrost.polyui.component.impl.Block
import org.polyfrost.polyui.component.impl.Group
import org.polyfrost.polyui.component.impl.Image
import org.polyfrost.polyui.component.impl.Text
import org.polyfrost.polyui.event.Event
import org.polyfrost.polyui.operations.Fade
import org.polyfrost.polyui.renderer.data.PolyImage
import org.polyfrost.polyui.unit.Align
import org.polyfrost.polyui.unit.Vec2
import org.polyfrost.polyui.unit.seconds
import org.polyfrost.polyui.utils.image
import org.polyfrost.polyui.utils.mapToArray
import org.polyfrost.polyui.utils.radii
import org.polyfrost.polyui.utils.translated

private val heart = PolyImage("assets/oneconfig/ico/plus.svg")
private val defaultModImage = "assets/oneconfig/ico/default_mod.svg".image()
private val modBoxTopRad = radii(8f, 8f, 0f, 0f)
private val modBoxBotRad = radii(0f, 0f, 8f, 8f)
private val modBoxAlign = Align(cross = Align.Cross.Start, mode = Align.Mode.Vertical, pad = Vec2.ZERO)
private val imageAlign = Align(main = Align.Main.Center, pad = Vec2.ZERO)
private val barAlign = Align(pad = Vec2(14f, 6f), main = Align.Main.SpaceBetween)

fun ModsPage(trees: Collection<Tree>): Drawable {
    if (trees.isEmpty()) {
        return Group(
            Text("oneconfig.mods.none", fontSize = 24f).setFont { medium },
            Text("oneconfig.mods.none.desc", fontSize = 14f),
            size = Vec2(1130f, 635f),
            alignment = Align(main = Align.Main.Center, pad = Vec2(18f, 18f), maxRowSize = 1),
        ).namedId("EmptyModsPage")
    }
    // todo add categories
    return Group(
        children = trees.mapNotNull {
            if (it.getMetadata<Any?>("frontendIgnore") != null) return@mapNotNull null
            Group(
                Block(
                    Image(it.getMetadata<String>("icon")?.image() ?: defaultModImage),
                    radii = modBoxTopRad,
                    alignment = imageAlign,
                    size = Vec2(256f, 104f),
                ).withStates(),
                Block(
                    Text(it.title, fontSize = 14f).setFont { medium },
                    Image(heart),
                    radii = modBoxBotRad,
                    alignment = barAlign,
                    size = Vec2(256f, 36f),
                ).setPalette { brand.fg },
                alignment = modBoxAlign,
            ).onClick { _ ->
                OneConfigUI.openPage(ConfigVisualizer.INSTANCE.get(it), (this[1][0] as Text).text)
            }.namedId("ModCard")
        }.toTypedArray(),
        size = Vec2(1130f, 0f),
        visibleSize = Vec2(1130f, 635f),
        alignment = Align(cross = Align.Cross.Start, pad = Vec2(18f, 18f)),
    ).namedId("ModsPage")
}

fun ThemesPage(): Drawable {
    return Group()
}

fun FeedbackPage(): Drawable {
    return Group(
        Image(PolyImage("assets/oneconfig/brand/polyfrost.png")).onInit { image.size.min(298f, 50f) },
        Text("oneconfig.feedback.title", fontSize = 24f).setFont { medium },
        Text("oneconfig.feedback.credits", fontSize = 14f),
        Text("oneconfig.feedback.bugreport", fontSize = 24f).setFont { medium },
        Text("oneconfig.feedback.joindiscord", fontSize = 14f),
        size = Vec2(1130f, 0f),
        visibleSize = Vec2(1130f, 635f),
        alignment = Align(cross = Align.Cross.Start, mode = Align.Mode.Vertical, pad = Vec2(18f, 18f)),
    )
}

fun ProfilesPage(): Drawable {
    return Group()
}

fun ChangelogPage(news: Collection<News>): Drawable {
    return Group(
        size = Vec2(1130f, 0f),
        visibleSize = Vec2(1130f, 635f),
        alignment = Align(cross = Align.Cross.Center, pad = Vec2(60f, 20f)),
        children = news.mapToArray {
            Group(
                if (it.image != null) Image(it.image).onInit { image.size.max(325f, 111f) } else null,
                Group(
                    Text(it.title, fontSize = 16f).setFont { medium },
                    Text(it.summary, visibleSize = Vec2(612f, 166f)),
                    Group(
                        Text(it.dateString),
                        Text("oneconfig.readmore").withStates().onClick { _ ->
                            val page =
                                Group(
                                    if (it.image != null) Image(it.image).onInit { image.size.max(325f, 111f) } else null,
                                    Group(
                                        Text(it.title, fontSize = 24f).setFont { medium },
                                        Text("oneconfig.writtenby".translated(it.author)),
                                        Text(it.dateString),
                                    ),
                                    Text(it.content, fontSize = 14f, visibleSize = Vec2(1100f, 0f)),
                                    alignment = Align(cross = Align.Cross.Start),
                                    size = Vec2(1130f, 0f),
                                    visibleSize = Vec2(1130f, 635f),
                                )
                            OneConfigUI.openPage(page, it.title)
                            // todo switch
                        },
                        size = Vec2(612f, 12f),
                        alignment = Align(main = Align.Main.SpaceBetween),
                    ),
                    alignment = Align(mode = Align.Mode.Vertical),
                ),
            )
        },
    )
}

fun NotificationsPopup(polyUI: PolyUI, notifications: List<Notification>) {
    val it = Block(
        Text("oneconfig.notifications", fontSize = 16f).setFont { medium },
        Group(
            children = notifications.mapToArray {
                Group(
                    Group(
                        Image(it.icon).onInit { image.size.resize(24f, 24f) },
                        Group(
                            Group(
                                Text(it.title, fontSize = 14f).setFont { medium },
                                Text(it.timeString).setPalette { text.secondary },
                            ),
                            Text(it.description).setPalette { text.secondary },
                            alignment = Align(mode = Align.Mode.Vertical),
                        ),
                    ),
                    *it.extras,
                )
            },
        ),
        Group(
            Group(
                Image("assets/oneconfig/ico/close.svg".image()),
                Text("oneconfig.clearall"),
            ),
            Image("assets/oneconfig/ico/cog.svg".image()),
        ),
        focusable = true,
        visibleSize = Vec2(368f, 500f),
        size = Vec2(300f, 0f),
    ).events {
        Event.Focused.Lost then { _ ->
            Fade(this, 0f, false, Animations.EaseInOutQuad.create(0.2.seconds)) {
                parent.removeChild(this)
            }.add()
        }
    }
    it.setup(polyUI)
    it.x = polyUI.mouseX - it.width / 2f
    it.y = polyUI.mouseY + 10f
    it.alpha = 0f
    Fade(it, 1f, false, Animations.EaseInOutQuad.create(0.1.seconds)).add()
    polyUI.master.addChild(it)
    polyUI.focus(it)
}