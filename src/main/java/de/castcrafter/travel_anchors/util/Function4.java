package de.castcrafter.travel_anchors.util;

import java.util.Objects;
import java.util.function.Function;

public interface Function4<A, B, C, D, R> {

    R apply(A a, B b, C c, D d);

    default <V> Function4<A, B, C, D, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (A a, B b, C c, D d) -> after.apply(this.apply(a, b, c, d));
    }
}
