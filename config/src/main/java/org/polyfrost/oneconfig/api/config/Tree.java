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

package org.polyfrost.oneconfig.api.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The Tree class represents a tree structure that contains properties and other trees as children.
 * It provides various methods to access, modify, and compare the tree and its elements.
 */
public class Tree extends Node implements Serializable {
    public static final Logger LOGGER = LoggerFactory.getLogger("OneConfig Config API");
    @NotNull
    public final Map<String, Node> map;

    public Tree(@NotNull String id, @Nullable Map<String, Node> items) {
        super(id);
        if (items != null) {
            map = new HashMap<>(items.size());
            map.putAll(items);
        } else map = new HashMap<>();
    }

    public Tree put(@NotNull Node... nodes) {
        for (Node n : nodes) {
            map.put(n.getID(), n);
        }
        return this;
    }

    public Tree put(Node n) {
        map.put(n.getID(), n);
        return this;
    }

    @Nullable
    public Node get(String name) {
        return map.get(name);
    }

    @Nullable
    public Node get(@NotNull String... name) {
        Tree t = this;
        Node n = null;
        for (String s : name) {
            n = t.get(s);
            if (n instanceof Tree) t = (Tree) n;
            else return n;
        }
        return n;
    }

    @Nullable
    public Tree getChild(@NotNull String... name) {
        Node n = get(name);
        return n instanceof Tree ? (Tree) n : null;
    }

    @Nullable
    public Property<?> getProperty(@NotNull String... name) {
        Node n = get(name);
        return n instanceof Property ? (Property<?>) n : null;
    }

    public void onAll(BiConsumer<String, Node> action) {
        _onAll(this, action);
    }

    public void onAllProperties(BiConsumer<String, Property<?>> action) {
        _onAllProp(this, action);
    }

    private static void _onAll(Tree t, BiConsumer<String, Node> action) {
        for (Map.Entry<String, Node> e : t.map.entrySet()) {
            action.accept(e.getKey(), e.getValue());
        }
    }

    private static void _onAllProp(Tree t, BiConsumer<String, Property<?>> action) {
        for (Map.Entry<String, Node> e : t.map.entrySet()) {
            if (e.getValue() instanceof Property) action.accept(e.getKey(), (Property<?>) e.getValue());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Tree)) return false;
        Tree that = (Tree) obj;
        return this.map.size() == that.map.size() && this.id.equals(that.id);
    }

    /**
     * Check if the tree's contents equals another tree's. This will check the values and children of the tree.
     *
     * @param obj the tree to check against
     * @return whether the trees are equal
     */
    public boolean deepEquals(@Nullable Object obj) {
        if (!equals(obj)) return false;
        Tree that = (Tree) obj;
        for (Map.Entry<String, Node> e : this.map.entrySet()) {
            Node n = that.get(e.getKey());
            if (n == null) {
//                System.err.println("Second tree does not contain " + e);
                return false;
            }
            if (!e.getValue().deepEquals(n)) {
//                System.err.println(e.getValue() + " does not equal " + n);
                return false;
            }
        }
        return true;
    }


    /**
     * Merge two trees into one. This method is very powerful and should be used with care.
     * <br>
     * If the input tree contains values that are not in this tree, it will be added to this tree.
     * <br>
     * This tree should have identical structure to the passed tree when this method is completed.
     *
     * @param tree      the input tree
     * @param overwrite if true, all values that are in both trees will be overwritten from the input tree into this tree.
     * @param copyMeta  the metadata from the input tree will be copied into this tree, and if overwrite is true,
     *                  the metadata from the origin value before overwriting is preserved as well
     * @throws ClassCastException if the types of the properties (with the same name) do not match
     */
    public void merge(Tree tree, boolean overwrite, boolean copyMeta) {
        if (tree == null || tree == this) return;
        _merge(this, tree, overwrite, copyMeta);
    }

    private static void _merge(Tree self, Tree in, boolean overwrite, boolean copyMeta) {
        for (Map.Entry<String, Node> toAdd : in.map.entrySet()) {
            Node that = toAdd.getValue();
            Node _this = self.get(toAdd.getKey());
            boolean thisIsTree = _this instanceof Tree;
            boolean thatIsTree = that instanceof Tree;
            if (_this == null) {
                self.put(that);
                continue;
            }

            if (thisIsTree) {
                if (!thatIsTree) {
                    LOGGER.warn("Overwriting tree {} with property {}, is this intended?", _this.getID(), that.getID());
                    self.put(that);
                } else {
                    _merge((Tree) _this, (Tree) that, overwrite, copyMeta);
                    if (copyMeta) _this.addMetadata(that.getMetadata());
                }
                continue;
            } else if (thatIsTree) {
                LOGGER.warn("Overwriting property {} with tree {}, is this intended?", _this.getID(), that.getID());
                self.put(that);
                continue;
            }
            if (overwrite) {
                self.put(that);
            } else if (copyMeta) {
                _this.addMetadata(that.getMetadata());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        _toString(builder, 0, this);
        return builder.toString();
    }

    private static void _toString(StringBuilder sb, int depth, Tree t) {
        for (int i = 0; i < depth; i++) sb.append('\t');
        sb.append(t.id).append(":\n");
        for (Map.Entry<String, Node> e : t.map.entrySet()) {
            if (e.getValue() instanceof Property) {
                for (int i = 0; i < depth + 1; i++) sb.append('\t');
                sb.append(e.getValue()).append('\n');
            } else {
                _toString(sb, depth + 1, (Tree) e.getValue());
            }
        }
    }

    @Contract("_ -> new")
    public static @NotNull Tree tree(@NotNull String id) {
        return new Tree(id, null);
    }

    /**
     * Create a new builder from the given tree. This will copy all the data from the tree into the builder.
     */
    @Contract("_ -> new")
    public static @NotNull Tree tree(@NotNull Tree src) {
        return new Tree(src.id, src.map);
    }
}



