package ca.applin.jib.utils;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

/** Use this instead of {@code null} references! */
public sealed interface Maybe<T> extends Serializable
        permits Just, Nothing {
    long serialVersionUID = 42L;

    /* ***************** */
    /* Type constructors */
    /* ***************** */

    @SuppressWarnings("unchecked")
    static <T> Maybe<T> just(T elem) {
        if (null == elem)
            return (Maybe<T>) Nothing.INSTANCE;
        return new Just<>(elem);
    }

    @SuppressWarnings("unchecked")
    static <T> Maybe<T> nothing() {
        return (Maybe<T>) Nothing.INSTANCE;
    }

    /* ******************************* */
    /* static function implementations */
    /* ******************************* */

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <T> Maybe<T> fromOptional(Optional<T> optional) {
        Objects.requireNonNull(optional, "java.util.Optional instance must not be null!");
        return optional.map(t -> (Maybe<T>) new Just<>(t))
                .orElseGet(Nothing::new);
    }

    static <T> Maybe<T> fromNullable(T nullable) {
        return just(nullable);
    }

    // Applicative
    /** Applies a BiFunction 'inside' the {@code Maybe}.
     * If any of the first or second Maybes are {@code nothing}, returns {@code nothing}. */
    static <T, E, S> Maybe<S> lift(BiFunction<T, E, S> bif, Maybe<T> first, Maybe<E> second) {
        Maybe<Function<E, S>> f1 = first.partial(bif);
        return second.flatMap(s -> f1.map(f -> f.apply(s)));
    }

    /** Returns {@code Nothing} on an empty or {@code null} list. Returns {@code Just a} where a is the first element of the list. */
    static <T> Maybe<T> listToMaybe(List<T> list) {
        if (isNull(list) || list.isEmpty())
            return nothing();
        return just(list.get(0));
    }

    /** The catMaybes function takes a list of {@code Maybes} and returns an unmodifiable list of all the {@code Just} values. */
    static <T> List<T> catMaybe(List<Maybe<T>> maybes) {
        List<T> justList = new ArrayList<>();
        for (Maybe<T> maybe : maybes)
            maybe.ifPresent(justList::add);
        return Collections.unmodifiableList(justList);
    }

    /** The mapMaybe function is a version of {@link Maybe#map} which can throw out elements.
     * In particular, the functional argument returns something of type {@code Maybe b}.
     * If this is {@code Nothing}, no element is added on to the result list.
     * If it is {@code Just b}, then b is included in the result list. The returned list in unmodifiable. */
    static <T, E> List<E> mapMaybe(Function<T, Maybe<E>> f, List<T> list) {
        List<E> result = new ArrayList<>();
        for (T t : list)
            f.apply(t).ifPresent(result::add);
        return result;
    }

    static <T> Comparator<Maybe<T>> comparator(Comparator<T> comparator) {
        return (m1, m2) -> {
            if (m1.isNothing() && m2.isNothing())
                return 0;
            return m1.map(i -> m2.map(j -> comparator.compare(i, j)).orElse(1)).orElse(-1);
        };
    }

    /* ********** */
    /* public API */
    /* ********** */

    boolean isJust();
    boolean isNothing();
    T orElse(T elem);
    T orElseGet(Supplier<? super T> supplier);
    <E extends RuntimeException> T orElseThrow(E exception);
    void ifPresent(MyConsumer<? super T> onJust);
    void ifPresentOrElse(MyConsumer<? super T> onJust, MyRunnable onNothing);
    boolean test(Predicate<T> test);
    Optional<T> toOptional();
    List<T> toList();
    Stream<T> stream();
    <E, S> Maybe<Function<E, S>> partial(BiFunction<T, E, S> f);                    // Applicative
    Maybe<T> or(Maybe<T> other);                                                    // Alternative
    <E> Maybe<E> map(Function<? super T, ? extends E> f);                           // Functor
    <E> Maybe<E> flatMap(Function<? super T, ? extends Maybe<? extends E>> f);      // Monad

    default boolean eq(T other) {
        return test(e -> e.equals(other));
    }

    // helper interfaces to allow catching checked expection and throwing them as unchecked

    interface MyConsumer<T> {
        void accept(T t) throws Exception;
        static <T> Consumer<T> from(MyConsumer<T> consumer) {
            return t -> {
                try {
                    consumer.accept(t);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            };
        }
        static <T> MyConsumer<T> to(Consumer<T> consumer) {
            return consumer::accept;
        }
    }

    interface MyRunnable {
        void run() throws Exception;
        static Runnable from(MyRunnable runnable) {
            return () -> {
                try {
                    runnable.run();
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            };
        }
    }
    static MyRunnable to(Runnable runnable) {
        return runnable::run;
    }

}


