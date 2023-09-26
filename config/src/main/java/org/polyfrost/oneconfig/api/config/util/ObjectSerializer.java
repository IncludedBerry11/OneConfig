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

package org.polyfrost.oneconfig.api.config.util;

import org.jetbrains.annotations.NotNull;
import org.polyfrost.oneconfig.api.config.adapter.Adapter;
import org.polyfrost.oneconfig.api.config.adapter.impl.ColorAdapter;
import org.polyfrost.oneconfig.api.config.backend.Backend;
import org.polyfrost.oneconfig.api.config.exceptions.SerializationException;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static org.polyfrost.oneconfig.api.config.Tree.LOGGER;

public class ObjectSerializer {
    public static final ObjectSerializer INSTANCE = new ObjectSerializer();
    private final Set<Adapter<?>> adapters = new HashSet<>();
    private static final Unsafe theUnsafe = getUnsafe();

    static {
        INSTANCE.registerTypeAdapter(new ColorAdapter());
    }

    public void registerTypeAdapter(Adapter<?> adapter) {
        if (!this.adapters.add(adapter)) {
            LOGGER.warn("Failed to register type adapter: An adapter for type {} is already registered", adapter.getTargetClass());
        }
    }

    public void registerTypeAdapter(Adapter<?>... adapters) {
        for (Adapter<?> a : adapters) {
            registerTypeAdapter(a);
        }
    }

    @SuppressWarnings("unchecked")
    public Object serialize(Object in) {
        if (in == null) return null;
        Class<?> cls = in.getClass();
        if (isSimpleObject(in)) {
            return in;
        }
        if (in instanceof Collection) {
            Collection<?> c = (Collection<?>) in;
            if (c.isEmpty()) return new Object[]{};
            Object first = c.iterator().next();
            if (isSimpleObject(first)) {
                return c;
            }
            return c.stream().map(this::serialize).collect(Collectors.toList());
        }
        if (in instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) in;
            if (m.isEmpty()) return Collections.emptyList();
            Iterator<? extends Map.Entry<?, ?>> iter = m.entrySet().iterator();
            Map.Entry<?, ?> first = iter.next();
            if ((isSimpleObject(first.getKey()) || isPrimitiveArray(first.getKey().getClass())) && (isSimpleObject(first.getValue()) || isPrimitiveArray(first.getValue().getClass()))) {
                return m;
            }
            Map<Object, Object> out = new HashMap<>();
            out.put(serialize(first.getKey()), serialize(first.getValue()));
            while (iter.hasNext()) {
                Map.Entry<?, ?> e = iter.next();
                out.put(serialize(e.getKey()), serialize(e.getValue()));
            }
            return out;
        }
        if (in instanceof Object[]) {
            return serialize(Arrays.asList((Object[]) in));
        }

        for (Adapter<?> a : adapters) {
            if (a.getTargetClass().equals(cls)) {
                Adapter<Object> ad = (Adapter<Object>) a;
                Object out = ad.serialize(in);
                boolean isMap = out instanceof Map;
                // ClassCastException when the Map does not have String keys (the doc explains required types)
                Map<String, Object> outMap = isMap ? (Map<String, Object>) out : new HashMap<>(2);
                outMap.put("classType", ad.getTargetClass().getName());
                if (!isMap) {
                    outMap.put("value", out);
                }
                return outMap;
            }
        }
        // we have a complex type with no adapter, amazing.
        Map<String, Object> cfg = new HashMap<>();
        cfg.put("classType", cls.getName());
        _serialize(cls, in, cfg);
        return cfg;
    }

    private void _serialize(Class<?> cls, Object value, Map<String, Object> cfg) {
        for (Field f : cls.getDeclaredFields()) {
            if (f.isSynthetic() || Modifier.isTransient(f.getModifiers()) || Modifier.isStatic(f.getModifiers()))
                continue;
            f.setAccessible(true);
            try {
                Object o = f.get(value);
                // skip self references
                if (o == value) continue;
                if (o != null) {
                    if (!isSimpleObject(o)) {
                        cfg.put(f.getName(), serialize(o));
                    } else cfg.put(f.getName(), o);
                }
            } catch (Exception e) {
                throw new SerializationException("Failed to serialize object " + value, e);
            }
        }
        for (Class<?> c : cls.getInterfaces()) {
            _serialize(c, value, cfg);
        }
        if (cls.getSuperclass() != null) {
            _serialize(cls.getSuperclass(), value, cfg);
        }
    }

    public Object deserialize(Map<String, Object> in) {
        if (in == null) return null;
        String clsName = (String) in.get("classType");
        if (clsName == null) {
            System.err.println("Offending map: " + in);
            mapToString(in);
            throw new SerializationException("Cannot deserialize object: missing classType field!");
        }
        for (Adapter<?> a : adapters) {
            if (a.getTargetClass().getName().equals(clsName)) {
                Adapter<Object> ad = (Adapter<Object>) a;
                Object value = in.get("value");
                if (value == null) {
                    value = in;
                }
                return ad.deserialize(value);
            }
        }
        Class<?> cls;
        try {
            cls = Class.forName(clsName);
        } catch (Exception e) {
            throw new SerializationException("Failed to deserialize object: Target class not found", e);
        }
        return _deserialize(in, cls);
    }

    @SuppressWarnings("unchecked")
    private Object _deserialize(Map<String, Object> in, Class<?> cls) {
        Object o;
        try {
            Constructor<?> ctor = cls.getDeclaredConstructor();
            ctor.setAccessible(true);
            o = ctor.newInstance();
        } catch (NoSuchMethodException ignored) {
            if (theUnsafe != null) {
                try {
                    if (cls.isArray()) {
                        o = new ArrayList<>();
                    } else {
                        o = theUnsafe.allocateInstance(cls);
                    }
                } catch (Exception e) {
                    throw new SerializationException("Failed to allocate deserializing object", e);
                }
            } else {
                throw new SerializationException("Failed to deserialize object: no no-args constructor found!");
            }
        } catch (Exception e) {
            throw new SerializationException("Failed to allocate deserializing object", e);
        }
        for (Map.Entry<String, Object> e : in.entrySet()) {
            if (e.getKey().equals("classType")) continue;
            try {
                Field f = getDeclaredField(o.getClass(), e.getKey());
                if (f == null) continue;
                f.setAccessible(true);
                if (f.getType().isEnum()) {
                    f.set(o, Enum.valueOf((Class) f.getType(), (String) e.getValue()));
                } else if (e.getValue() instanceof Map) {
                    Map<String, Object> m = (Map<String, Object>) e.getValue();
                    f.set(o, _deserialize(m, m.getClass()));
                } else {
                    Object out = unbox(e.getValue(), f.getType());
                    f.set(o, out);
                }
            } catch (Exception ex) {
                throw new SerializationException("Failed to deserialize object", ex);
            }
        }
        return o;
    }

    public static Field getDeclaredField(Class<?> cls, String name) {
        for (Field f : cls.getDeclaredFields()) {
            if (f.getName().equals(name)) return f;
        }
        for (Class<?> c : cls.getInterfaces()) {
            Field f = getDeclaredField(c, name);
            if (f != null) return f;
        }
        if (cls.getSuperclass() != null) {
            return getDeclaredField(cls.getSuperclass(), name);
        }
        return null;
    }

    public static Object unbox(@NotNull Object in, Class<?> target) {
        if(target == null) return in;
        if (in instanceof List && target.isArray()) {
            List<?> list = (List<?>) in;
            if (list.isEmpty()) throw new IllegalStateException("Cannot unbox empty list to array https://docs.polyfrost.org/oneconfig/config/unbox-empty-list");
            if(target.getComponentType().isPrimitive()) {
                Class<?> cType = target.getComponentType();
                Object array = Array.newInstance(cType, list.size());
                for (int i = 0; i < list.size(); i++) {
                    Array.set(array, i, unbox(list.get(i), cType));
                }
                return array;
            }
        }
        if(in instanceof Number) {
            Number n = (Number) in;
            if(target == float.class || target == Float.class) return n.floatValue();
            if(target == double.class || target == Double.class) return n.doubleValue();
            if(target == byte.class || target == Byte.class) return n.byteValue();
            if(target == short.class || target == Short.class) return n.shortValue();
            if(target == int.class || target == Integer.class) return n.intValue();
            if(target == long.class || target == Long.class) return n.longValue();
        }
        return in;
    }


    public static void mapToString(Map<String, Object> map) {
        for (Map.Entry<String, Object> e : map.entrySet()) {
            System.err.println("  " + e.getKey() + ": " + e.getValue());
        }
    }


    public static boolean isSimpleObject(Object o) {
        if (o == null) return true;
        Class<?> cls = o.getClass();
        return isPrimitiveArray(cls) || cls.isEnum() || isPrimitiveWrapper(o) || o instanceof CharSequence;
    }

    public static boolean isPrimitiveWrapper(Object o) {
        return o instanceof Number || o instanceof Boolean || o instanceof Character;
    }

    public static boolean isPrimitiveArray(Class<?> cls) {
        return cls.isArray() && cls.getComponentType().isPrimitive();
    }

    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            Backend.LOGGER.warn("Failed to get unsafe instance, classes without no-args constructors will fail to deserialize!");
            return null;
        }
    }
}