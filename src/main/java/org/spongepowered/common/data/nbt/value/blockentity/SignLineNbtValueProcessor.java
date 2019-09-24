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
package org.spongepowered.common.data.nbt.value.blockentity;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.text.Text;
import org.spongepowered.common.data.nbt.NbtDataType;
import org.spongepowered.common.data.nbt.value.NbtValueProcessor;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeListValue;
import org.spongepowered.common.text.SpongeTexts;
import org.spongepowered.common.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SignLineNbtValueProcessor implements NbtValueProcessor<List<Text>, ListValue<Text>> {
    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean isCompatible(final NbtDataType type) {
        return false;
    }

    @Override
    public Optional<ListValue<Text>> readFrom(final NBTTagCompound data) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Text>> readValue(final NBTTagCompound data) {
        return Optional.empty();
    }

    @Override
    public DataTransactionResult offer(final NBTTagCompound data, final List<Text> value) {
        ImmutableList.Builder<Text> existing = null;
        if (data.hasKey(Constants.TileEntity.Sign.SIGN_TEXT_1, Constants.NBT.TAG_STRING)) {
            if (existing == null) {
                existing = ImmutableList.builder();
            }
            existing.add(SpongeTexts.fromLegacy(data.getString(Constants.TileEntity.Sign.SIGN_TEXT_1)));
        }
        if (data.hasKey(Constants.TileEntity.Sign.SIGN_TEXT_2, Constants.NBT.TAG_STRING)) {
            if (existing == null) {
                existing = ImmutableList.builder();
            }
            existing.add(SpongeTexts.fromLegacy(data.getString(Constants.TileEntity.Sign.SIGN_TEXT_2)));
        }
        if (data.hasKey(Constants.TileEntity.Sign.SIGN_TEXT_3, Constants.NBT.TAG_STRING)) {
            if (existing == null) {
                existing = ImmutableList.builder();
            }
            existing.add(SpongeTexts.fromLegacy(data.getString(Constants.TileEntity.Sign.SIGN_TEXT_3)));
        }
        if (data.hasKey(Constants.TileEntity.Sign.SIGN_TEXT_4, Constants.NBT.TAG_STRING)) {
            if (existing == null) {
                existing = ImmutableList.builder();
            }
            existing.add(SpongeTexts.fromLegacy(data.getString(Constants.TileEntity.Sign.SIGN_TEXT_4)));
        }
        for (int i = 0; i < 4; i++) {
            final String line = SpongeTexts.toLegacy(value.get(i));
            data.setTag(Constants.TileEntity.Sign.TEXT + (i + 1), new NBTTagString(line));
        }
        if (existing != null) {
            return DataTransactionResult.successReplaceResult(new ImmutableSpongeListValue<>(Keys.SIGN_LINES, ImmutableList.copyOf(value)), new ImmutableSpongeListValue<>(Keys.SIGN_LINES, existing.build()));
        }
        return DataTransactionResult.successResult(new ImmutableSpongeListValue<>(Keys.SIGN_LINES, ImmutableList.copyOf(value)));
    }

    @Override
    public DataTransactionResult remove(final NBTTagCompound data) {
        return null;
    }
}
