/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.data.value;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.data.value.Value;

public class CachedCatalogTypeValueConstructor<V extends Value<E>, E extends CatalogType> implements ValueConstructor<V, E> {

    private final ValueConstructor<V, E> original;
    private final LoadingCache<E, V> immutableCache;

    public CachedCatalogTypeValueConstructor(ValueConstructor<V, E> original) {
        this.original = original;
        this.immutableCache = Caffeine.newBuilder().maximumSize(100)
                .build(original::getRawImmutable);
    }

    @Override
    public V getMutable(E element) {
        return this.original.getMutable(element);
    }

    @Override
    public V getRawImmutable(E element) {
        //noinspection ConstantConditions
        return this.immutableCache.get(element);
    }
}
