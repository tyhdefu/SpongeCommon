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
package org.spongepowered.common.util.lazy;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

public abstract class Lazy<T> implements Supplier<T> {

    /**
     * Constructs a lazy. This lazy can be accessed safely
     * from multiple threads.
     *
     * @param supplier The supplier
     * @param <T> The value type
     * @return The lazy
     */
    public static <T> Lazy<T> of(Supplier<T> supplier) {
        requireNonNull(supplier, "supplier");
        if (supplier instanceof SynchronizedLazy ||
                supplier instanceof InitializedLazy) {
            return (Lazy<T>) supplier;
        }
        return new SynchronizedLazy<>(supplier);
    }

    /**
     * Constructs a lazy from the given value.
     *
     * @param value The value
     * @param <T> The value type
     * @return The lazy
     */
    public static <T> Lazy<T> of(T value) {
        return new InitializedLazy<>(value);
    }

    /**
     * Constructs an unsafe lazy. If this lazy is accessed
     * concurrently, there's no guarantee that the lazy will
     * return the first initialized value. The initializer
     * may be called multiple times.
     *
     * @param supplier The supplier
     * @param <T> The value type
     * @return The lazy
     */
    public static <T> Lazy<T> unsafeOf(Supplier<T> supplier) {
        requireNonNull(supplier, "supplier");
        if (supplier instanceof Lazy) {
            return (Lazy<T>) supplier;
        }
        return new UnsafeLazy<>(supplier);
    }

    Lazy() {
    }
}
