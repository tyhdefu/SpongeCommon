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
package org.spongepowered.common.mixin.invalid.core.block;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockPlanks;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.DataManipulator.Immutable;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableTreeData;
import org.spongepowered.api.data.type.WoodType;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeTreeData;
import org.spongepowered.common.mixin.core.block.BlockMixin;

import java.util.Optional;

@Mixin(BlockPlanks.class)
public abstract class BlockPlanksMixin extends BlockMixin {

    @SuppressWarnings("RedundantTypeArguments") // some java compilers will not calculate this generic correctly
    @Override
    public ImmutableList<Immutable<?, ?>> bridge$getManipulators(final net.minecraft.block.BlockState blockState) {
        return ImmutableList.<Immutable<?, ?>>of(this.impl$getTreeTypeFor(blockState));
    }

    @Override
    public boolean bridge$supports(final Class<? extends Immutable<?, ?>> immutable) {
        return ImmutableTreeData.class.isAssignableFrom(immutable);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Optional<BlockState> bridge$getStateWithData(final net.minecraft.block.BlockState blockState, final Immutable<?, ?> manipulator) {
        if (manipulator instanceof ImmutableTreeData) {
            final BlockPlanks.EnumType treeType = (BlockPlanks.EnumType) (Object) ((ImmutableTreeData) manipulator).type().get();
            return Optional.of((BlockState) blockState.withProperty(BlockPlanks.VARIANT, treeType));
        }
        return super.bridge$getStateWithData(blockState, manipulator);
    }

    @Override
    public <E> Optional<BlockState> bridge$getStateWithValue(final net.minecraft.block.BlockState blockState, final Key<? extends Value<E>> key, final E value) {
        if (key.equals(Keys.TREE_TYPE)) {
            final BlockPlanks.EnumType treeType = (BlockPlanks.EnumType) value;
            return Optional.of((BlockState) blockState.withProperty(BlockPlanks.VARIANT, treeType));
        }
        return super.bridge$getStateWithValue(blockState, key, value);
    }

    @SuppressWarnings("ConstantConditions")
    private ImmutableTreeData impl$getTreeTypeFor(final net.minecraft.block.BlockState blockState) {
        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongeTreeData.class, (WoodType) (Object) blockState.get(BlockPlanks.VARIANT));
    }
}
