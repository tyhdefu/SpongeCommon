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
package org.spongepowered.common.data.provider.entity.horse;

import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.util.registry.Registry;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HorseColor;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.data.provider.GenericMutableDataProvider;
import org.spongepowered.common.data.type.SpongeHorseColor;
import org.spongepowered.common.util.lazy.Lazy;

import java.util.Optional;

public class HorseEntityHorseColorProvider extends GenericMutableDataProvider<HorseEntity, HorseColor> {

    private final Lazy<Registry<HorseColor>> registry = Lazy.unsafeOf(
            () -> SpongeImpl.getRegistry().getCatalogRegistry().requireRegistry(HorseColor.class));

    public HorseEntityHorseColorProvider() {
        super(Keys.HORSE_COLOR);
    }

    @Override
    protected Optional<HorseColor> getFrom(HorseEntity dataHolder) {
        final int metadata = dataHolder.getHorseVariant() & 0xff;
        return Optional.ofNullable(this.registry.get().getByValue(metadata));
    }

    @Override
    protected boolean set(HorseEntity dataHolder, HorseColor value) {
        final int variant = dataHolder.getHorseVariant();
        final int metadata = ((SpongeHorseColor) value).getMetadata();
        dataHolder.setHorseVariant((variant & ~0xff) | metadata);
        return true;
    }
}
