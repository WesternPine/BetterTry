package dev.westernpine.bettertry.functions;

/**
 * This is a copy of the {@link java.util.function.Supplier} interface in Java, with the only exception being a throws throwable declaration in order to be used in lambda expressions.
 * @param <T> Any type of object.
 */
@FunctionalInterface
public interface TrySupplier<T> {
    T get() throws Throwable;
}
