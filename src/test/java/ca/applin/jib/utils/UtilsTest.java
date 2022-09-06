package ca.applin.jib.utils;

import static ca.applin.jib.utils.Utils.__LINE__;
import static ca.applin.jib.utils.Utils.__FULL_METHOD_NAME__;
import static ca.applin.jib.utils.Utils.__METHOD__;
import static ca.applin.jib.utils.Utils.todo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.applin.jib.utils.Utils.NotYetImplemented;
import org.junit.jupiter.api.Test;

class UtilsTest {

    int    line = __LINE__();
    String name = __METHOD__();

    @Test
    public void helloFromTheTest() {
        assertEquals("helloFromTheTest", __METHOD__());
        assertEquals("ca.applin.jib.utils.UtilsTest.helloFromTheTest(UtilsTest.java:22)", __FULL_METHOD_NAME__());
        assertEquals(23, __LINE__());
        assertEquals(24, __LINE__());
        assertEquals(16, line);
        assertEquals("<init>", name);
        // quite bad that the test depends opn the actuall lines number they are written on...
    }

    @Test
    public void todoTest() {
        NotYetImplemented e = assertThrows(NotYetImplemented.class, () -> todo());
        System.out.println(e.getMessage());
        assertTrue(e.getMessage().contains("ca.applin.jib.utils.UtilsTest.lambda$todoTest$0(UtilsTest.java:32)"));


        e = assertThrows(NotYetImplemented.class, () -> todo("complete this test"));
        System.out.println(e.getMessage());
        assertTrue(e.getMessage().contains("ca.applin.jib.utils.UtilsTest.lambda$todoTest$1(UtilsTest.java:37)"));
    }

}