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

class Failure<V> extends Try<V> {

    private final Throwable throwable;

    protected Failure(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }

    @Override
    public V get() throws Throwable {
        throw throwable;
    }

    @Override
    public V getUnchecked() {
        throw new RuntimeException(throwable);
    }

    @Override
    public Try<V> onUnhandledSuccess(TryConsumer<V> consumer) throws Throwable {
        Objects.requireNonNull(consumer);
        return this;
    }

    @Override
    public Try<V> onSuccess(TryConsumer<V> consumer) {
        Objects.requireNonNull(consumer);
        return this;
    }

    @Override
    public Try<V> onUnhandledFailure(TryConsumer<Throwable> consumer) throws Throwable {
        Objects.requireNonNull(consumer);
        consumer.accept(throwable);
        return this;
    }

    @Override
    public Try<V> onFailure(TryConsumer<Throwable> consumer) {
        Objects.requireNonNull(consumer);
        try {
            consumer.accept(throwable);
            return this;
        } catch (Throwable throwable) {
            return Try.failure(throwable);
        }
    }

    @Override
    public Try<V> filter(Predicate<V> predicate) {
        return this;
    }

    @Override
    public <R> Try<R> map(TryFunction<? super V, ? extends R> function) {
        Objects.requireNonNull(function);
        return Try.failure(throwable);
    }

    @Override
    public <R> Try<R> flatMap(TryFunction<? super V, Try<R>> function) {
        Objects.requireNonNull(function);
        return Try.failure(throwable);
    }

    @Override
    public V recover(Function<? super Throwable, V> function) {
        Objects.requireNonNull(function);
        return function.apply(throwable);
    }

    @Override
    public Try<V> recoverWith(TryFunction<? super Throwable, Try<V>> function) {
        Objects.requireNonNull(function);
        try{
            return function.apply(throwable);
        }catch(Throwable throwable){
            return Try.failure(throwable);
        }
    }

    @Override
    public V orElse(V value) {
        return value;
    }

    @Override
    public Try<V> orElseTry(TrySupplier<V> supplier) {
        Objects.requireNonNull(supplier);
        return Try.to(supplier);
    }

    @Override
    public Try<? extends Void> orElseTry(TryRunnable runnable) {
        Objects.requireNonNull(runnable);
        return Try.to(runnable);
    }

    @Override
    public <T extends Throwable> V orElseThrow(Supplier<? extends T> supplier) throws T {
        throw supplier.get();
    }

    @Override
    public Optional<V> toOptional() {
        return Optional.empty();
    }

    @Override
    public Optional<V> toNullableOptional() {
        return Optional.empty();
    }
}
