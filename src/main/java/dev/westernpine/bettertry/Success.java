package dev.westernpine.bettertry;

import dev.westernpine.bettertry.functions.TryConsumer;
import dev.westernpine.bettertry.functions.TryFunction;
import dev.westernpine.bettertry.functions.TryRunnable;
import dev.westernpine.bettertry.functions.TrySupplier;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

class Success<V> extends Try<V> {

    private final V value;

    protected Success(V value) {
        this.value = value;
    }

    @Override
    public boolean isSuccessful() {
        return true;
    }

    @Override
    public V get() throws Throwable {
        return value;
    }

    @Override
    public V getUnchecked() {
        return value;
    }

    @Override
    public Try<V> onUnhandledSuccess(TryConsumer<V> consumer) throws Throwable {
        Objects.requireNonNull(consumer);
        consumer.accept(value);
        return this;
    }

    @Override
    public Try<V> onSuccess(TryConsumer<V> consumer) {
        Objects.requireNonNull(consumer);
        try {
            consumer.accept(value);
            return this;
        } catch (Throwable throwable) {
            return Try.failure(throwable);
        }
    }

    @Override
    public Try<V> onUnhandledFailure(TryConsumer<Throwable> consumer) throws Throwable {
        Objects.requireNonNull(consumer);
        return this;
    }

    @Override
    public Try<V> onFailure(TryConsumer<Throwable> consumer) {
        Objects.requireNonNull(consumer);
        return this;
    }

    @Override
    public Try<V> filter(Predicate<V> predicate) {
        Objects.requireNonNull(predicate);
        if (predicate.test(value)) {
            return this;
        } else {
            return Try.failure(new NoSuchElementException("Predicate does not match for " + value));
        }
    }

    @Override
    public <R> Try<R> map(TryFunction<? super V, ? extends R> function) {
        Objects.requireNonNull(function);
        try {
            return new Success<>(function.apply(value));
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    @Override
    public <R> Try<R> flatMap(TryFunction<? super V, Try<R>> function) {
        Objects.requireNonNull(function);
        try {
            return function.apply(value);
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    @Override
    public V recover(Function<? super Throwable, V> function) {
        Objects.requireNonNull(function);
        return value;
    }

    @Override
    public Try<V> recoverWith(TryFunction<? super Throwable, Try<V>> function) {
        Objects.requireNonNull(function);
        return this;
    }

    @Override
    public V orElse(V value) {
        return this.value;
    }

    @Override
    public Try<V> orElseTry(TrySupplier<V> supplier) {
        Objects.requireNonNull(supplier);
        return this;
    }

    @Override
    public Try<V> orElseTry(TryRunnable runnable) {
        Objects.requireNonNull(runnable);
        return this;
    }

    @Override
    public <T extends Throwable> V orElseThrow(Supplier<? extends T> supplier) throws T {
        Objects.requireNonNull(supplier);
        return value;
    }

    @Override
    public Optional<V> toOptional() {
        return Optional.of(value);
    }


    public Optional<V> toNullableOptional() {
        return Optional.ofNullable(value);
    }
}
