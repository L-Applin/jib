package ca.applin.jib.utils;

public record Pair<A,B>(A fst, B snd) {

    @Override
    public String toString() {
        return "[%s, %s]".formatted(fst(), snd());
    }
}
