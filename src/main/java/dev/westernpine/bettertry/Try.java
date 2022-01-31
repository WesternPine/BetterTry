package dev.westernpine.bettertry;

import dev.westernpine.bettertry.functions.TryConsumer;
import dev.westernpine.bettertry.functions.TryFunction;
import dev.westernpine.bettertry.functions.TryRunnable;
import dev.westernpine.bettertry.functions.TrySupplier;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A better, more stream-lined implementation of exception handling in Java.
 * @param <V> The value this class wraps as a result of a Try or a Try method.
 */
public abstract class Try<V> {

    protected Try() {
    }

    /**
     * Creates a new Try wrapper for the result of the given supplier.
     * If the supplier is successful, a successful implementation wrapper of the result will be provided.
     * If the supplier fails, a failure implementation wrapper of the throwable (Exception, Error) will be provided.
     * @param supplier The supplier to test.
     * @param <V> The result type returned from the supplier.
     * @return A new Try wrapper for the result of the given supplier.
     */
    public static <V> Try<V> to(TrySupplier<V> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return Try.successful(supplier.get());
        } catch (Throwable throwable) {
            return Try.failure(throwable);
        }
    }

    /**
     * Creates a new Try wrapper for the result of null or a throwable from the given runnable.
     * If the runnable is successful, a successful implementation wrapper of null will be provided.
     * If the runnable fails, a failure implementation wrapper of the throwable (Exception, Error) will be provided.
     * @param runnable The runnable to test.
     * @return A new Try wrapper for the result of the given runnable.
     */
    public static Try<? extends Void> to(TryRunnable runnable) {
        Objects.requireNonNull(runnable);
        try {
            runnable.run();
            return Try.successful(null);
        } catch (Throwable throwable) {
            return Try.failure(throwable);
        }
    }

    /**
     * Returns a new instance of Try in the form of a successful completion with the specified value.
     * @param value The new value as a result of a successful try.
     * @param <V> The Type of the value.
     * @return A new successful Try object wrapping the given value.
     */
    public static <V> Try<V> successful(V value) {
        return new Success<>(value);
    }

    /**
     * Returns a new instance of Try in the form of a failed completion with the thrown throwable.
     * @param throwable The throwable thrown during execution.
     * @param <V> The Type of the original value.
     * @return A new failure Try object wrapping the given value.
     */
    public static <V> Try<V> failure(Throwable throwable) {
        return new Failure<>(throwable);
    }

    /**
     *
     * @return true if the outcome of all previous actions are successful.
     */
    public abstract boolean isSuccessful();

    /**
     * Get value V on success or throw the cause of the failure.
     * @return V value.
     * @throws Throwable The failure that occurred during this attempt.
     */
    public abstract V get() throws Throwable;

    /**
     * Get value V on success or throw the cause of the failure.
     * @return Value V.
     * @throws RuntimeException The failure that occurred during this attempt.
     */
    public abstract V getUnchecked();

    /**
     * Get the cause of the failure if any.
     * @return The cause of the failure if any, otherwise null;
     */
    public abstract Throwable getFailureCause();

    /**
     * Performs the provided action, when successful.
     * @param consumer action to run.
     * @return current Try.
     * @throws Throwable if the action throws an exception.
     */
    public abstract Try<V> onUnhandledSuccess(TryConsumer<V> consumer) throws Throwable;

    /**
     * Performs the provided action, when successful.
     * @param consumer action to run.
     * @return current Try if successful, new failure try if failed.
     */
    public abstract Try<V> onSuccess(TryConsumer<V> consumer);

    /**
     * Performs the provided action, when failed.
     * @param consumer action to run.
     * @return current Try.
     * @throws Throwable if the action throws an exception.
     */
    public abstract Try<V> onUnhandledFailure(TryConsumer<Throwable> consumer) throws Throwable;

    /**
     * Performs the provided action, when failed.
     * @param consumer action to run.
     * @return current Try if successful, new failure try if failed.
     */
    public abstract Try<V> onFailure(TryConsumer<Throwable> consumer);

    /**
     * Performs the provided action, regardless of success or failure.
     * @param runnable action to run.
     * @return current Try if successful, new failure try if failed.
     */
    public Try<V> then(TryRunnable runnable) {
        try {
            runnable.run();
            return this;
        } catch (Throwable e) {
            return failure(e);
        }
    }

    /**
     * If a Try is a Success and the predicate holds true, the Success is passed further.
     * Otherwise (Failure or predicate doesn't hold), pass Failure.
     * @param predicate predicate applied to the value held by Try.
     * @return For Success, the same success if predicate holds true, otherwise Failure.
     */
    public abstract Try<V> filter(Predicate<V> predicate);

    /**
     * Transform success or pass on failure.
     * Takes an optional type parameter of the new type.
     * You need to be specific about the new type if changing the type.
     * @param function function to apply to successful value.
     * @param <R> new type. (optional)
     * @return new composed Try.
     */
    public abstract <R> Try<R> map(TryFunction<? super V, ? extends R> function);

    /**
     * Transform success or pass on failure, taking a Try&lt;U&gt; as the result.
     * Takes an optional type parameter of the new type.
     * You need to be specific about the new type if changing the type.
     * @param function function to apply to successful value.
     * @param <R> new type. (optional)
     * @return new composed Try.
     */
    public abstract <R> Try<R> flatMap(TryFunction<? super V, Try<R>> function);

    /**
     * Specifies a result to use in case of failure.
     * Gives access to the exception which can be pattern matched on.
     * @param function function to execute on successful result.
     * @return new composed Try.
     */
    public abstract V recover(Function<? super Throwable, V> function);

    /**
     * Try applying function(V value) on the case of failure.
     * @param function function that takes throwable and returns result.
     * @return a new Try in the case of failure, or the current Success.
     */
    public abstract Try<V> recoverWith(TryFunction<? super Throwable, Try<V>> function);

    /**
     * Return a value in the case of a failure.
     * This is similar to recover but does not expose the exception type.
     * @param value return the try's value or else the value specified.
     * @return new composed Try.
     */
    public abstract V orElse(V value);

    /**
     * Return another try in the case of failure.
     * Like recoverWith but without exposing the exception.
     * @param supplier return the value or the value from the new try.
     * @return new composed Try.
     */
    public abstract Try<V> orElseTry(TrySupplier<V> supplier);

    /**
     * Return another try in the case of failure.
     * Like recoverWith but without exposing the exception.
     * @param runnable another task to try.
     * @return new composed Try.
     */
    public abstract Try<?> orElseTry(TryRunnable runnable);

    /**
     * Gets the value V on Success or throws the cause of the failure.
     * @return V value.
     * @throws Throwable produced by the supplier function argument.
     */
    public abstract <T extends Throwable> V orElseThrow(Supplier<? extends T> supplier) throws T;

    /**
     * Try contents wrapped in a standard Optional.
     * @return Optional of T, if Success, Empty if Failure.
     */
    public abstract Optional<V> toOptional();

    /**
     * Try contents wrapped in a nullable Optional.
     * @return Optional of T, if Success, Empty if Failure or null value.
     */
    public abstract Optional<V> toNullableOptional();


}
