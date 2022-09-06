package ca.applin.jib.utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class Nothing<T> implements Maybe<T> {

    static final Maybe<?> INSTANCE = new Nothing<>();

    @Override
    public boolean isJust() {
        return false;
    }

    @Override
    public boolean isNothing() {
        return true;
    }

    @Override
    public <E> Maybe<E> map(Function<? super T, ? extends E> f) {
        return (Nothing<E>) INSTANCE;
    }

    @Override
    public <E> Maybe<E> flatMap(Function<? super T, ? extends Maybe<? extends E>> f) {
        return (Nothing<E>) INSTANCE;
    }

    @Override
    public T orElse(T other) {
        return other;
    }

    @Override
    public T orElseGet(Supplier<? super T> supplier) {
        Objects.requireNonNull(supplier, "Maybe#orElseGet supplier must not be null");
        return (T) supplier.get();
    }

    @Override
    public <E extends RuntimeException> T orElseThrow(E exception) {
        throw exception;
    }

    @Override
    public <E, S> Maybe<Function<E, S>> partial(BiFunction<T, E, S> f) {
        return (Nothing<Function<E, S>>) INSTANCE;
    }

    @Override
    public Maybe<T> or(Maybe<T> other) {
        if (null == other) {
            return (Nothing<T>) INSTANCE;
        }
        return other;
    }

    @Override
    public boolean test(Predicate<T> test) {
        return false;
    }

    @Override
    public void ifPresent(MyConsumer<? super T> onJust) { /* do nothing */ }

    @Override
    public void ifPresentOrElse(MyConsumer<? super T> onJust, MyRunnable onNothing) {
        Objects.requireNonNull(onNothing,
                "Maybe#ifPresentOrElse onNothing Runnable must not be null");
        try {
            onNothing.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<T> toOptional() {
        return Optional.empty();
    }

    @Override
    public List<T> toList() {
        return Collections.emptyList();
    }

    @Override
    public Stream<T> stream() {
        return Stream.empty();
    }

    @Override
    public String toString() {
        return "(Nothing)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o != null && getClass() == o.getClass();
    }
}
