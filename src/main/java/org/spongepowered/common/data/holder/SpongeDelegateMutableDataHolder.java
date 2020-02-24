package org.spongepowered.common.data.holder;

import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.value.CollectionValue;
import org.spongepowered.api.data.value.MapValue;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.common.util.Streams2;

import java.util.Collection;
import java.util.Map;

public interface SpongeDelegateMutableDataHolder extends SpongeDelegateDataHolder, SpongeMutableDataHolder {

    @Override
    default <E> DataTransactionResult offer(Key<? extends Value<E>> key, E value) {
        final DataTransactionResult result = SpongeMutableDataHolder.super.offer(key, value);
        if (result.getType() != DataTransactionResult.Type.FAILURE) {
            return result;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> {
                    if (holder instanceof SpongeMutableDataHolder) {
                        return ((SpongeMutableDataHolder) holder).offer(key, value);
                    }
                    return DataTransactionResult.failNoData();
                })
                .filter(r -> r.getType() != DataTransactionResult.Type.FAILURE)
                .findFirst()
                .orElse(DataTransactionResult.failNoData());
    }

    @Override
    default DataTransactionResult offer(Value<?> value) {
        final DataTransactionResult result = SpongeMutableDataHolder.super.offer(value);
        if (result.getType() != DataTransactionResult.Type.FAILURE) {
            return result;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> {
                    if (holder instanceof SpongeMutableDataHolder) {
                        return ((SpongeMutableDataHolder) holder).offer(value);
                    }
                    return DataTransactionResult.failNoData();
                })
                .filter(r -> r.getType() != DataTransactionResult.Type.FAILURE)
                .findFirst()
                .orElse(DataTransactionResult.failNoData());
    }

    @Override
    default <E> DataTransactionResult offerSingle(Key<? extends CollectionValue<E, ?>> key, E element) {
        final DataTransactionResult result = SpongeMutableDataHolder.super.offerSingle(key, element);
        if (result.getType() != DataTransactionResult.Type.FAILURE) {
            return result;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> {
                    if (holder instanceof SpongeMutableDataHolder) {
                        return ((SpongeMutableDataHolder) holder).offerSingle(key, element);
                    }
                    return DataTransactionResult.failNoData();
                })
                .filter(r -> r.getType() != DataTransactionResult.Type.FAILURE)
                .findFirst()
                .orElse(DataTransactionResult.failNoData());
    }

    @Override
    default <E> DataTransactionResult offerAll(Key<? extends CollectionValue<E, ?>> key, Collection<? extends E> elements) {
        final DataTransactionResult result = SpongeMutableDataHolder.super.offerAll(key, elements);
        if (result.getType() != DataTransactionResult.Type.FAILURE) {
            return result;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> {
                    if (holder instanceof SpongeMutableDataHolder) {
                        return ((SpongeMutableDataHolder) holder).offerAll(key, elements);
                    }
                    return DataTransactionResult.failNoData();
                })
                .filter(r -> r.getType() != DataTransactionResult.Type.FAILURE)
                .findFirst()
                .orElse(DataTransactionResult.failNoData());
    }

    @Override
    default <K, V> DataTransactionResult offerSingle(Key<? extends MapValue<K, V>> key, K valueKey, V value) {
        final DataTransactionResult result = SpongeMutableDataHolder.super.offerSingle(key, valueKey, value);
        if (result.getType() != DataTransactionResult.Type.FAILURE) {
            return result;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> {
                    if (holder instanceof SpongeMutableDataHolder) {
                        return ((SpongeMutableDataHolder) holder).offerSingle(key, valueKey, value);
                    }
                    return DataTransactionResult.failNoData();
                })
                .filter(r -> r.getType() != DataTransactionResult.Type.FAILURE)
                .findFirst()
                .orElse(DataTransactionResult.failNoData());
    }

    @Override
    default <K, V> DataTransactionResult offerAll(Key<? extends MapValue<K, V>> key, Map<? extends K, ? extends V> values) {
        final DataTransactionResult result = SpongeMutableDataHolder.super.offerAll(key, values);
        if (result.getType() != DataTransactionResult.Type.FAILURE) {
            return result;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> {
                    if (holder instanceof SpongeMutableDataHolder) {
                        return ((SpongeMutableDataHolder) holder).offerAll(key, values);
                    }
                    return DataTransactionResult.failNoData();
                })
                .filter(r -> r.getType() != DataTransactionResult.Type.FAILURE)
                .findFirst()
                .orElse(DataTransactionResult.failNoData());
    }

    @Override
    default DataTransactionResult remove(Key<?> key) {
        final DataTransactionResult result = SpongeMutableDataHolder.super.remove(key);
        if (result.getType() != DataTransactionResult.Type.FAILURE) {
            return result;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> {
                    if (holder instanceof SpongeMutableDataHolder) {
                        return ((SpongeMutableDataHolder) holder).remove(key);
                    }
                    return DataTransactionResult.failNoData();
                })
                .filter(r -> r.getType() != DataTransactionResult.Type.FAILURE)
                .findFirst()
                .orElse(DataTransactionResult.failNoData());
    }

    @Override
    default DataTransactionResult remove(Value<?> value) {
        final DataTransactionResult result = SpongeMutableDataHolder.super.remove(value);
        if (result.getType() != DataTransactionResult.Type.FAILURE) {
            return result;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> {
                    if (holder instanceof SpongeMutableDataHolder) {
                        return ((SpongeMutableDataHolder) holder).remove(value);
                    }
                    return DataTransactionResult.failNoData();
                })
                .filter(r -> r.getType() != DataTransactionResult.Type.FAILURE)
                .findFirst()
                .orElse(DataTransactionResult.failNoData());
    }

    @Override
    default <E> DataTransactionResult removeAll(Key<? extends CollectionValue<E, ?>> key, Collection<? extends E> elements) {
        final DataTransactionResult result = SpongeMutableDataHolder.super.removeAll(key, elements);
        if (result.getType() != DataTransactionResult.Type.FAILURE) {
            return result;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> {
                    if (holder instanceof SpongeMutableDataHolder) {
                        return ((SpongeMutableDataHolder) holder).removeAll(key, elements);
                    }
                    return DataTransactionResult.failNoData();
                })
                .filter(r -> r.getType() != DataTransactionResult.Type.FAILURE)
                .findFirst()
                .orElse(DataTransactionResult.failNoData());
    }

    @Override
    default <K, V> DataTransactionResult removeAll(Key<? extends MapValue<K, V>> key, Map<? extends K, ? extends V> values) {
        final DataTransactionResult result = SpongeMutableDataHolder.super.removeAll(key, values);
        if (result.getType() != DataTransactionResult.Type.FAILURE) {
            return result;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> {
                    if (holder instanceof SpongeMutableDataHolder) {
                        return ((SpongeMutableDataHolder) holder).removeAll(key, values);
                    }
                    return DataTransactionResult.failNoData();
                })
                .filter(r -> r.getType() != DataTransactionResult.Type.FAILURE)
                .findFirst()
                .orElse(DataTransactionResult.failNoData());
    }

    @Override
    default <E> DataTransactionResult removeSingle(Key<? extends CollectionValue<E, ?>> key, E element) {
        final DataTransactionResult result = SpongeMutableDataHolder.super.removeSingle(key, element);
        if (result.getType() != DataTransactionResult.Type.FAILURE) {
            return result;
        }
        return Streams2.reverseStream(this.getDelegateDataHolders())
                .map(holder -> {
                    if (holder instanceof SpongeMutableDataHolder) {
                        return ((SpongeMutableDataHolder) holder).removeSingle(key, element);
                    }
                    return DataTransactionResult.failNoData();
                })
                .filter(r -> r.getType() != DataTransactionResult.Type.FAILURE)
                .findFirst()
                .orElse(DataTransactionResult.failNoData());
    }
}
