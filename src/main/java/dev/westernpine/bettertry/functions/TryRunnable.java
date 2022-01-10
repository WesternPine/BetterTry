package dev.westernpine.bettertry.functions;

/**
 * This is a copy of the {@link Runnable} interface in Java, with the only exception being a throws throwable declaration in order to be used in lambda expressions.
 */
@FunctionalInterface
public interface TryRunnable {
    void run() throws Throwable;
}
