package ca.applin.jib.utils;

import static ca.applin.jib.utils.Maybe.just;
import static ca.applin.jib.utils.Maybe.nothing;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Adds 4 method to the {@link Iterator} interface:
 * <ol>
 *     <li> {@code Maybe&lt;T&gt; peek()}
 *          <p>returns the value that would be returned by the next call to {@link Iterator#next()} but without actually adavncing
 *              the interator to the next element. This value is wrapped in a {@link Just} instance, but if the Iterator is empty,
 *              will return {@link Nothing} instead.
 *          </p>
 *     </li>
 *     <li> {@code T current()}
 *     <p> returns the 'current' element, which is equal to the last value returned by the call to {@link Iterator#next()} <em>without</em> advancing the iterator to the next element</p>
 *     </li>
 *     <li> {@code Maybe&lt;T&gt; maybeNext()}
 *         <p> returns {@link Nothing } if the iterator does not have any element left (when {@link Iterator#hasNext()} would returns false</p>
 *     </li>
 *     <li> {@code Maybe&lt;T> nextIf(Predicate&lt;T&gt; predicate)}
 *      <p> Advances the Iterator the the next element, and returns that element wrapped in a {@link Just} instance, only if the predicates  </p>
 *     </li>
 * </ol>
 *
 * A regular {@link Iterator} can be convert to a Peekable by using the static method
 * {@link Peekable#fromIterator(Iterator)}. It does not support the {@link Iterator#remove() remove} operation of Iterators.
 *
 * @param <T> the type of elements returned by this iterator
 */
public interface Peekable<T> extends Iterator<T> {
    /** return the next element or nothing() if the iterator ends */
    Maybe<T> peek();
    T current();
    Maybe<T> maybeNext();
    Maybe<T> nextIf(Predicate<T> predicate);

    static <T> Peekable<T> fromIterator(Iterator<T> iter) {
        return new PeekIterator<>(iter);
    }
}

class PeekIterator<T> implements Peekable<T> {
    private final Iterator<T> iterator;
    private boolean peeked = false;
    private T current_value, peeked_value = null;

    public PeekIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        return iterator.hasNext() || peeked;
    }

    @Override
    public Maybe<T> maybeNext() {
        if (!hasNext()) return nothing();
        final T next = next();
        return next == null
                ? nothing()
                : just(next);
    }

    @Override
    public T next() {
        if (peeked) {
            peeked = false;
            current_value = peeked_value;
            return peeked_value;
        } else {
            current_value = iterator.hasNext()
                    ? iterator.next()
                    : null;
           return current_value;
        }
    }

    public Maybe<T> peek() {
        if (!peeked) {
            peeked = true;
            if (iterator.hasNext()) {
                peeked_value = iterator.next();
            } else {
                peeked_value = null;
                peeked = false;
            }
        }
        return peeked_value == null
                ? nothing()
                : just(peeked_value);
    }

    @Override
    public T current() {
        return current_value;
    }

    public Maybe<T> nextIf(Predicate<T> predicate) {
        return peek().flatMap(elem -> predicate.test(elem)
                                        ? just(next())
                                        : nothing());
    }
}
