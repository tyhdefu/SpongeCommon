package org.spongepowered.common.data.holder;

import org.spongepowered.api.data.DataHolder;

public interface SpongeDelegateImmutableDataHolder<I extends DataHolder.Immutable<I>> extends SpongeDelegateDataHolder, SpongeImmutableDataHolder<I> {

}
