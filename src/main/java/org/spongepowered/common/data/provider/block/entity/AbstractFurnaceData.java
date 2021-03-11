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
package org.spongepowered.common.data.provider.block.entity;

import org.spongepowered.api.data.Keys;
import org.spongepowered.common.accessor.world.level.block.entity.AbstractFurnaceBlockEntityAccessor;
import org.spongepowered.common.data.provider.DataProviderRegistrator;
import org.spongepowered.common.util.SpongeTicks;

public final class AbstractFurnaceData {

    private AbstractFurnaceData() {
    }

    // @formatter:off
    public static void register(final DataProviderRegistrator registrator) {
        registrator
                .asMutable(AbstractFurnaceBlockEntityAccessor.class)
                    .create(Keys.FUEL)
                        .get(AbstractFurnaceBlockEntityAccessor::accessor$litTime)
                        .setAnd((h, v) -> {
                            if (v < 0) {
                                return false;
                            }
                            h.accessor$litTime(v);
                            return true;
                        })
                    .create(Keys.MAX_BURN_TIME)
                        .get(x -> new SpongeTicks(x.accessor$litDuration()))
                        .set((h, v) -> h.accessor$litDuration((int) v.ticks()))
                    .create(Keys.MAX_COOK_TIME)
                        .get(x -> new SpongeTicks(x.accessor$cookingTotalTime()))
                        .set((h, v) -> h.accessor$cookingTotalTime((int) v.ticks()))
                    .create(Keys.PASSED_COOK_TIME)
                        .get(x -> new SpongeTicks(x.accessor$cookingProgress()))
                        .set((h, v) -> {
                            final int ticks = (int) v.ticks();
                            if (ticks < h.accessor$cookingTotalTime()) {
                                h.accessor$cookingProgress(ticks);
                            }
                        });
    }
    // @formatter:on
}
