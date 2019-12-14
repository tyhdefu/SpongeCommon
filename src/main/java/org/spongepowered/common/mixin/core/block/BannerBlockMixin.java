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
package org.spongepowered.common.mixin.core.block;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.DataManipulator.Immutable;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableDirectionalData;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeDirectionalData;

import java.util.Optional;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BannerBlock.BlockBannerStanding;

@Mixin(BannerBlock.class)
public abstract class BannerBlockMixin extends AbstractBannerBlockMixin {

    private ImmutableDirectionalData impl$getDirectionalData(final net.minecraft.block.BlockState blockState) {
        final int intDir = blockState.get(BannerBlock.ROTATION);
        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongeDirectionalData.class, Direction.values()[(intDir + 8) % 16]);
    }

    @Override
    public boolean bridge$supports(final Class<? extends Immutable<?, ?>> immutable) {
        return super.bridge$supports(immutable) || ImmutableDirectionalData.class.isAssignableFrom(immutable);
    }

    @Override
    public Optional<BlockState> bridge$getStateWithData(final net.minecraft.block.BlockState blockState, final Immutable<?, ?> manipulator) {
        if (manipulator instanceof ImmutableDirectionalData) {
            final Direction direction = ((ImmutableDirectionalData) manipulator).direction().get();
            final int intDirection = (direction.ordinal() + 8) % 16;
            return Optional.of((BlockState) blockState.withProperty(BannerBlock.ROTATION, intDirection));
        }
        return super.bridge$getStateWithData(blockState, manipulator);
    }

    @Override
    public <E> Optional<BlockState> bridge$getStateWithValue(final net.minecraft.block.BlockState blockState, final Key<? extends Value<E>> key, final E value) {
        if (key.equals(Keys.DIRECTION)) {
            final Direction direction = (Direction) value;
            final int intDirection = (direction.ordinal() + 8) % 16;
            return Optional.of((BlockState) blockState.withProperty(BannerBlock.ROTATION, intDirection));
        }
        return super.bridge$getStateWithValue(blockState, key, value);
    }

    @Override
    public ImmutableList<Immutable<?, ?>> bridge$getManipulators(final net.minecraft.block.BlockState blockState) {
        return ImmutableList.<Immutable<?, ?>>builder()
                .addAll(super.bridge$getManipulators(blockState))
                .add(this.impl$getDirectionalData(blockState))
                .build();
    }
}
