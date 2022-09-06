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
public record Just<T>(T elem) implements Maybe<T> {

    @Override
    public boolean isNothing() {
        return false;
    }

    T get() {
        return this.elem;
    }

    @Override
    public boolean isJust() {
        return true;
    }

    @Override
    public <E> Maybe<E> map(Function<? super T, ? extends E> f) {
        return new Just<>(f.apply(elem));
    }

    @Override
    public <E> Maybe<E> flatMap(Function<? super T, ? extends Maybe<? extends E>> f) {
        Maybe<E> maybe = (Maybe<E>) f.apply(this.elem);
        return Objects.requireNonNull(maybe);
    }

    @Override
    public T orElse(T other) {
        return this.elem;
    }

    @Override
    public T orElseGet(Supplier<? super T> supplier) {
        return this.elem;
    }

    @Override
    public <E extends RuntimeException> T orElseThrow(E exception) {
        return this.elem;
    }

    @Override
    public Maybe<T> or(Maybe<T> other) {
        return this;
    }

    @Override
    public <E, S> Maybe<Function<E, S>> partial(BiFunction<T, E, S> bif) {
        Objects.requireNonNull(bif, "Maybe#partial BiFunction mus not be null");
        return new Just<>(e -> bif.apply(this.elem, e));
    }

    @Override
    public boolean test(Predicate<T> test) {
        return test.test(elem);
    }

    @Override
    public void ifPresent(MyConsumer<? super T> onJust) {
        Objects.requireNonNull(onJust, "Maybe#ifPresent onJust Consumer must not be null");
        try {
            onJust.accept(this.elem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ifPresentOrElse(MyConsumer<? super T> onJust, MyRunnable onNothing) {
        Objects.requireNonNull(onJust, "Maybe#ifPresentOrElse onJust Consumer must not be null");
        ifPresent(onJust);
    }

    @Override
    public Optional<T> toOptional() {
        return Optional.of(this.elem);
    }

    @Override
    public List<T> toList() {
        return Collections.singletonList(this.elem);
    }

    @Override
    public Stream<T> stream() {
        return Stream.of(this.elem);
    }

    @Override
    public String toString() {
        return "(Just " + elem + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Just<?> just = (Just<?>) o;
        return elem.equals(just.elem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elem);
    }
}
