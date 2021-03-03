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
package org.spongepowered.common.mixin.core.world.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.bridge.world.storage.MapItemSavedDataBridge;
import org.spongepowered.common.util.Constants;

import java.io.File;

@Mixin(SavedData.class)
public abstract class SavedDataMixin {

    @Inject(method = "save(Ljava/io/File;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/SavedData;save(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void impl$writeAdditionalMapNBT(File file, final CallbackInfo cir,
                                           CompoundTag compound) {
        if ((Object)this instanceof MapItemSavedData) {
            CompoundTag spongeData = compound.getCompound(Constants.Sponge.Data.V2.SPONGE_DATA);
            ((MapItemSavedDataBridge) this).bridge$writeSpongeData(spongeData);
            if (!spongeData.isEmpty()) {
                compound.put(Constants.Sponge.Data.V2.SPONGE_DATA, spongeData);
            }
        }
    }
}
