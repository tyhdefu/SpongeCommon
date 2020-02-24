package org.spongepowered.common.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Streams2 {

    public static <T> Stream<T> reverseStream(Iterable<? extends T> iterable) {
        if (iterable instanceof List) {
            //noinspection unchecked
            return reverseStream((List<? extends T>) iterable);
        }
        return reverseStream(Lists.newArrayList(iterable));
    }

    public static <T> Stream<T> reverseStream(List<? extends T> list) {
        final ListIterator<? extends T> it = list.listIterator(list.size());
        return StreamSupport.stream(
                new Spliterators.AbstractSpliterator<T>(list.size(), Spliterator.ORDERED) {
                    @Override
                    public boolean tryAdvance(Consumer<? super T> action) {
                        if (it.hasPrevious()) {
                            action.accept(it.previous());
                            return true;
                        } else {
                            return false;
                        }
                    }
                },
                false);
    }

    private Streams2() {
    }
}
