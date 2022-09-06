package ca.applin.jib.utils;

import java.util.AbstractList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static final int LINE_NOT_FOUND = -1;

    private Utils() { }

    public static <T> T todo() {
        throw new NotYetImplemented(__FULL_METHOD_NAME__(2) + " not yet implemented!");
    }

    public static <T> T todo(String msg) {
        throw new NotYetImplemented(msg + " @ " + __FULL_METHOD_NAME__(2));
    }

    public static class NotYetImplemented extends RuntimeException {
        public NotYetImplemented() { this(__FULL_METHOD_NAME__(2)); }
        public NotYetImplemented(String message) { super(message, null, true, false); }
    }

    public static List<Character> characters(final String string) {
        return new AbstractList<>() {
            @Override
            public Character get(int index) {
                return string.charAt(index);
            }

            @Override
            public int size() {
                return string.length();
            }
        };
    }



    public static String __METHOD__ (int depth) {
        StackWalker walker = StackWalker.getInstance();
        return walker.walk(frames -> frames
                .limit(depth + 1)
                .skip(depth)
                .map(StackWalker.StackFrame::getMethodName)
                .collect(Collectors.joining()));
    }

    public static String __METHOD__ () {
        return __METHOD__(2);
    }

    public static String __FULL_METHOD_NAME__ () {
        return __FULL_METHOD_NAME__(2);
    }

    public static String __FULL_METHOD_NAME__ (int depth) {
        StackWalker walker = StackWalker.getInstance();
        return walker.walk(frames -> frames
                .limit(depth + 1)
                .skip(depth)
                .map(Object::toString)
                .collect(Collectors.joining()));
    }

    public static int __LINE__ () {
        StackWalker walker = StackWalker.getInstance();
        return walker.walk(frames -> frames
                .limit(2)
                .skip(1)
                .map(StackWalker.StackFrame::getLineNumber)
                .findFirst()
                .orElse(LINE_NOT_FOUND));
    }
}
