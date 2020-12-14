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
package org.spongepowered.common.mixin.core.entity.monster;

import net.minecraft.block.BlockState;
import net.minecraft.block.SilverfishBlock;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.SilverfishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.bridge.entity.GrieferBridge;

@Mixin(targets = "net/minecraft/entity/monster/SilverfishEntity$HideInStoneGoal")
public abstract class SilverfishEntity_HideInStoneGoalMixin extends RandomWalkingGoal {

    public SilverfishEntity_HideInStoneGoalMixin(CreatureEntity creatureIn, double speedIn) { // Ignored
        super(creatureIn, speedIn);
    }

    @Redirect(method = "canUse()Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SilverfishBlock;isCompatibleHostBlock(Lnet/minecraft/block/BlockState;)Z"))
    private boolean impl$onCanGrief(final BlockState blockState) {
        return SilverfishBlock.isCompatibleHostBlock(blockState) && ((GrieferBridge) this.mob).bridge$canGrief();
    }
}
