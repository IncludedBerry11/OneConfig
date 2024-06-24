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

package org.polyfrost.oneconfig.utils.v1;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.ReplaceWith;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class MHUtilsTest {
    private static final MethodHandle STATIC_MH = MHUtils.getStaticMethodHandle(String.class, "valueOf", String.class, char[].class, int.class, int.class).getOrThrow();
    private static final MethodHandle INSTANCE_MH = MHUtils.getMethodHandle(String.class, "substring", String.class, int.class, int.class).getOrThrow();
    private static final MethodHandle CTOR = MHUtils.getConstructorHandle(String.class, char[].class).getOrThrow();


    @Test
    void works() {
        try {
            assertEquals("abc", (String) STATIC_MH.invokeExact(new char[]{'a', 'b', 'c'}, 0, 3));
            assertEquals("hello", INSTANCE_MH.invoke("hello, world", 0, 5));
        } catch (Throwable e) {
            fail(e);
        }
    }

    @Test
    void instantiateWorks() {
        try {
            assertEquals("hi2", (String) CTOR.invokeExact(new char[]{'h', 'i', '2'}));
        } catch (Throwable e) {
            fail(e);
        }
    }

    @Test
    @Deprecated(message = "test", replaceWith = @ReplaceWith(expression = "void test", imports = {"import"}), level = DeprecationLevel.HIDDEN)
    void annotationThingWorks() {
        try {
            Deprecated d = this.getClass().getDeclaredMethod("annotationThingWorks").getAnnotation(Deprecated.class);
            Map<String, Object> m = MHUtils.getAnnotationValues(d).getOrThrow();
            assertEquals("test", m.get("message"));
            assertEquals(DeprecationLevel.HIDDEN, m.get("level"));
            ReplaceWith r = (ReplaceWith) m.get("replaceWith");
            assertEquals("void test", r.expression());
            assertEquals("import", r.imports()[0]);
        } catch (Throwable e) {
            fail(e);
        }
    }

    @Test
    void removingFiltersWorks() {
        try {
            MHUtils.removeReflectionFilters();
            // this method will fail if the filters are still in place
            AccessibleObject.class.getDeclaredField("override");
        } catch (Throwable e) {
            fail(e);
        }
    }

    @Test
    void lmfao() {
        Function<char[], String> valueOf = MHUtils.getFunctionHandle(String.class, "valueOf", String.class, char[].class).getOrThrow();
        assertEquals("abc", valueOf.apply("abc".toCharArray()));
        Consumer<Integer> consumes = MHUtils.getConsumerHandle(MHUtilsTest.class, "consumes", Integer.class).getOrThrow();
        consumes.accept(231);
        Predicate<String> isEmpty = MHUtils.getPredicateHandle(MHUtilsTest.class, "isEmpty", String.class).getOrThrow();
        assertFalse(isEmpty.test("no"));
        assertTrue(isEmpty.test(""));
    }

    static void consumes(Integer a) {
        if(a != 231) fail();
    }

    static boolean isEmpty(String s) {
        return s.isEmpty();
    }

    @Test
    void unreflectWorks() {
        try {
            Method m = String.class.getDeclaredMethod("substring", int.class, int.class);
            MethodHandle mh = MHUtils.getMethodHandle(m, "hello, world").getOrThrow();
            assertEquals(mh.invoke(0, 5), "hello");

            String s = "abc123";
            Field f = String.class.getDeclaredField("value");
            MethodHandle getter = MHUtils.getFieldGetter(f, s).getOrThrow();
            assertArrayEquals((byte[]) getter.invoke(), "abc123".getBytes());

            MethodHandle setter = MHUtils.getFieldSetter(f, s).getOrThrow();
            setter.invoke("p2".getBytes());
            assertArrayEquals(new byte[]{'p', '2'}, (byte[]) getter.invoke());

            Constructor<String> ctor = String.class.getDeclaredConstructor(char[].class);
            MethodHandle ctorHandle = MHUtils.getConstructorHandle(ctor).getOrThrow();
            assertEquals(ctorHandle.invoke("abc1234".toCharArray()), "abc1234");

        } catch (Throwable e) {
            fail(e);
        }
    }
}
