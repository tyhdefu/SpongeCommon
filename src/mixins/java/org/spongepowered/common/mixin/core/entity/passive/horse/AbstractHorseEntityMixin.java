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
package org.spongepowered.common.mixin.core.entity.passive.horse;

import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.bridge.entity.passive.horse.AbstractHorseEntityBridge;
import org.spongepowered.common.mixin.core.entity.AgeableEntityMixin;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AgeableEntityMixin implements AbstractHorseEntityBridge {

    // @formatter:off
    @Shadow protected Inventory inventory;
    @Shadow public abstract void shadow$equipSaddle(@Nullable SoundCategory sound);
    @Shadow public abstract boolean shadow$isSaddled();
    // @formatter:on

    @Override
    public boolean bridge$isSaddled() {
        return this.shadow$isSaddled();
    }

    @Override
    public void bridge$setSaddled(boolean saddled) {
        if (!this.shadow$isSaddled() && saddled) {
            this.shadow$equipSaddle(null);
        } else if (this.shadow$isSaddled() && !saddled){
            this.inventory.setItem(0, ItemStack.EMPTY);
        }
    }
}
