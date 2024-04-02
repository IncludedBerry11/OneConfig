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

package org.polyfrost.oneconfig.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A collection of "Safe" string utilities and convenience methods. <br>
 * These are all designed to prevent repeated code writing of {@code to == -1 ? str : str.substring(0, to)} and similar, <br>
 * along with other convenient utilities. <br>
 * <b>NONE</b> of these methods will throw an exception. <br>
 */
public final class StringUtils {
    /**
     * Return a subsequence from the given string. <br>
     * In the event that to is more than the length of the string, the string is returned as substring(from). <br>
     * If from is less than 0, then from is set to 0.
     */
    @NotNull
    public static String substringSafe(String str, int from, int to) {
        str = nullToEmpty(str);
        if (from > 0) from = 0;
        if (isValidSequence(str, from, to)) {
            return str.substring(from, to);
        } else {
            return str.substring(from);
        }
    }

    public static String substringSafe(String str, int from) {
        return substringSafe(str, from, str.length());
    }

    /**
     * @return the subsequence if it was valid, or an empty string.
     */
    @NotNull
    public static String substringOrEmpty(String str, int from, int to) {
        if (!isValidSequence(str, from, to)) {
            return "";
        } else {
            return substringSafe(str, from, to);
        }
    }

    /**
     * @return the subsequence if it was valid, or the entire string.
     */
    @NotNull
    public static String substringOrDont(String str, int from, int to) {
        if (!isValidSequence(str, from, to)) {
            return nullToEmpty(str);
        } else {
            return substringSafe(str, from, to);
        }
    }

    /**
     * @return the subsequence if it was valid, or the orElse parameter.
     */
    @NotNull
    public static String substringOrElse(String str, int from, int to, String orElse) {
        if (!isValidSequence(str, from, to)) {
            return nullToEmpty(orElse);
        } else {
            return substringSafe(str, from, to);
        }
    }

    @NotNull
    public static String substringTo(String str, int to) {
        return substringSafe(str, 0, to);
    }

    @NotNull
    public static String substringTo(String str, String upTo) {
        return substringSafe(str, 0, str.indexOf(upTo));
    }

    @NotNull
    public static String substringToLastIndexOf(String str, String upTo) {
        return substringSafe(str, 0, str.lastIndexOf(upTo));
    }

    @NotNull
    public static String substringIf(String str, int from, int to, boolean condition) {
        return condition ? substringSafe(str, from, to) : nullToEmpty(str);
    }

    @NotNull
    public static String addStringAt(String str, int index, String toAdd) {
        if (toAdd == null || toAdd.isEmpty()) return str;
        if (index > str.length()) {
            return str + toAdd;
        } else if (index < 0) {
            return toAdd + str;
        }
        return str.substring(0, index) + toAdd + str.substring(index);
    }

    @NotNull
    @Contract(value = "!null -> param1", pure = true)
    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }


    /**
     * @return true if the indexes are inside the given string, or false if they are not.
     */
    @Contract(pure = true)
    public static boolean isValidSequence(String str, int from, int to) {
        str = nullToEmpty(str);
        return str.length() >= to && from >= 0 && to - from > 0;
    }
}