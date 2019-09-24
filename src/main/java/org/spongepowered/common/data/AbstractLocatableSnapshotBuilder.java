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
package org.spongepowered.common.data;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.api.data.LocatableSnapshot;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.common.data.util.DataUtil;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeValue;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unchecked")
public abstract class AbstractLocatableSnapshotBuilder<B extends LocatableSnapshot.Builder<T, B>, T extends LocatableSnapshot<T>> extends AbstractDataBuilder<T> implements LocatableSnapshot.Builder<T, B> {

    @Nullable UUID worldId;
    @Nullable UUID creatorUuid;
    @Nullable UUID notifierUuid;

    @Nullable List<ImmutableDataManipulator<?, ?>> manipulators;
    @Nullable NBTTagCompound compound;
    @Nullable List<ImmutableValue<?>> values;

    protected AbstractLocatableSnapshotBuilder(final Class<T> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    public B world(final WorldProperties worldProperties) {
        this.worldId = checkNotNull(worldProperties).getUniqueId();
        return (B) this;
    }

    @Override
    public B creator(UUID uuid) {
        this.creatorUuid = uuid;
        return (B) this;
    }

    @Override
    public B notifier(UUID uuid) {
        this.notifierUuid = uuid;
        return (B) this;
    }

    public B worldId(final UUID worldUuid) {
        this.worldId = checkNotNull(worldUuid);
        return (B) this;
    }


    @Override
    public B add(final DataManipulator<?, ?> manipulator) {
        return add(manipulator.asImmutable());
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public B add(final ImmutableDataManipulator<?, ?> manipulator) {
        final Optional<DataProcessor<?, ?>> optional = DataUtil.getImmutableProcessor((Class) manipulator.getClass());
        if (optional.isPresent()) {
            addManipulator(manipulator);
        }
        return (B) this;
    }

    @Override
    public <V> B add(final Key<? extends BaseValue<V>> key, final V value) {
        checkNotNull(key, "key");
        if (this.values == null) {
            this.values = Lists.newArrayList();
        }
        this.values.add(new ImmutableSpongeValue<>(key, value));
        return (B) this;
    }

    protected void addManipulator(final ImmutableDataManipulator<?, ?> manipulator) {
        if (this.manipulators == null) {
            this.manipulators = Lists.newArrayList();
        }
        int replaceIndex = -1;
        for (final ImmutableDataManipulator<?, ?> existing : this.manipulators) {
            replaceIndex++;
            if (existing.getClass().equals(manipulator.getClass())) {
                break;
            }
        }
        if (replaceIndex != -1) {
            this.manipulators.remove(replaceIndex);
        }
        this.manipulators.add(manipulator);
    }

}
