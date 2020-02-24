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
package org.spongepowered.common.data.holder;

import static org.spongepowered.common.util.Predicates.distinctBy;

import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.common.data.util.EnsureMutable;
import org.spongepowered.common.util.Streams2;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface SpongeDelegateDataHolder extends SpongeDataHolder {

    List<? extends SpongeDataHolder> getDelegateDataHolders();

    @Override
    default <E> Optional<E> get(Key<? extends Value<E>> key) {
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> holder.get(key))
                .filter(Optional::isPresent)
                .findFirst()
                .orElseGet(() -> SpongeDataHolder.super.get(key));
    }

    @Override
    default <E, V extends Value<E>> Optional<V> getValue(Key<V> key) {
        final Optional<V> optional = SpongeDataHolder.super.getValue(key);
        if (optional.isPresent()) {
            return optional;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> holder.getValue(key))
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }

    @Override
    default boolean supports(Key<?> key) {
        return SpongeDataHolder.super.supports(key) ||
                this.getDelegateDataHolders().stream().anyMatch(holder -> holder.supports(key));
    }

    @Override
    default boolean supports(Value<?> value) {
        return SpongeDataHolder.super.supports(value) ||
                this.getDelegateDataHolders().stream().anyMatch(holder -> holder.supports(value));
    }

    @Override
    default Set<Key<?>> getKeys() {
        return this.getDelegateDataHolders().stream()
                .flatMap(holder -> holder.getKeys().stream())
                .collect(Collectors.toSet());
    }

    @Override
    default Set<Value.Immutable<?>> getValues() {
        return this.getDelegateDataHolders().stream()
                .flatMap(holder -> holder.getValues().stream())
                .filter(distinctBy(Value::getKey))
                .collect(Collectors.toSet());
    }

    @Override
    default Map<Key<?>, Object> getMappedValues() {
        final Collection<? extends SpongeDataHolder> holders = this.getDelegateDataHolders();
        if (holders.isEmpty()) {
            return SpongeDataHolder.super.getMappedValues();
        }
        final Iterator<? extends SpongeDataHolder> it = holders.iterator();
        final Map<Key<?>, Object> mappedValues = EnsureMutable.ensureMutable(it.next().getMappedValues());
        it.forEachRemaining(holder -> mappedValues.putAll(it.next().getMappedValues()));
        mappedValues.putAll(SpongeDataHolder.super.getMappedValues());
        return mappedValues;
    }
}
