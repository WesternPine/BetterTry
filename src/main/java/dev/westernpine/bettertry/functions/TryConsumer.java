package dev.westernpine.bettertry.functions;

/**
 * This is a copy of the {@link java.util.function.Consumer} interface in Java, with the only exception being a throws throwable declaration in order to be used in lambda expressions.
 * @param <V> Any type of object.
 */
@FunctionalInterface
public interface TryConsumer<V> {
    void accept(V value) throws Throwable;
}
