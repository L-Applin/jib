package ca.applin.jib.utils;

import static ca.applin.jib.utils.Maybe.just;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MaybeTest {

    @Test
    public void testMap() {
        Maybe<Integer> mInt = just(4);
        Maybe<Integer> m42 = mInt.map(i -> i + 38);
        assertTrue(mInt.isJust());
        assertEquals(42, m42.orElseThrow(new RuntimeException(new AssertionError())));
        assertEquals(4, mInt.orElseThrow(new RuntimeException(new AssertionError())));
    }

}