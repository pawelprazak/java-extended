package com.bluecatcode.common.concurrent;

import com.bluecatcode.common.base.Either;
import com.bluecatcode.common.base.Eithers;
import com.bluecatcode.common.contract.errors.ImpossibleViolation;
import com.bluecatcode.common.exceptions.WrappedException;
import com.bluecatcode.common.functions.CheckedFunction;
import com.bluecatcode.common.functions.Effect;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;

import javax.annotation.CheckReturnValue;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.bluecatcode.common.base.Eithers.either;
import static com.bluecatcode.common.contract.Postconditions.ensure;
import static com.bluecatcode.common.contract.Preconditions.require;
import static com.bluecatcode.common.exceptions.Exceptions.uncheckedException;
import static com.bluecatcode.common.exceptions.Exceptions.unwrapToUncheckedException;

@CheckReturnValue
public class Try {

    public static <T> T tryWith(Callable<T> callable) {
        return tryWith(callable, unwrapToUncheckedException());
    }

    public static <T> T tryWith(Callable<T> callable, Function<WrappedException, RuntimeException> exceptionFunction) {
        return either(callable).orThrow(exceptionFunction);
    }

    public static void tryWith(Effect effect) {
        try {
            effect.cause();
        } catch (Exception e) {
            throw uncheckedException().apply(e);
        }
    }

    public static <T> T tryWith(long timeoutDuration, TimeUnit timeoutUnit, Callable<T> callable) {
        try {
            TimeLimiter limiter = new SimpleTimeLimiter();
            //noinspection unchecked
            Callable<T> proxy = limiter.newProxy(callable, Callable.class, timeoutDuration, timeoutUnit);
            return proxy.call();
        } catch (Exception e) {
            throw uncheckedException().apply(e);
        }
    }

    public static void tryWith(long timeoutDuration, TimeUnit timeoutUnit, Effect effect) {
        try {
            TimeLimiter limiter = new SimpleTimeLimiter();
            //noinspection unchecked
            Effect proxy = limiter.newProxy(effect, Effect.class, timeoutDuration, timeoutUnit);
            proxy.cause();
        } catch (Exception e) {
            throw uncheckedException().apply(e);
        }
    }

    public interface Decorator<T> extends Function<T, T> {
    }

    public interface Limiter<T, R> extends Decorator<Function<T, R>> {
    }

    public interface ThrowsStep<T, R, ER> {
        <E extends Exception> WithReference<T, R, ER, E> handle(Function<WrappedException, E> handler);

        <E extends Exception> WithReference<T, R, ER, E> rethrow(Class<E> rethrowType);

        WithReference<T, R, ER, Exception> wrap();

        WithReference<T, R, ER, RuntimeException> swallow();
    }

    public interface LimitStep<R> {
        R limit(long timeoutDuration, TimeUnit timeoutUnit);
    }

    public interface TryStep<T, R, ER, E extends Exception> {
        R tryA(T function) throws E;
        ER either(T function);
    }

    public interface EitherFunction<T, R> extends Function<T, Either<WrappedException, R>> {
    }

    public interface LimitCheckedFunction<T, R, ER, E extends Exception> extends LimitStep<TryCheckedFunction<T, R, ER, E>> {
    }

    public interface TryCheckedFunction<T, R, ER, E extends Exception> extends
            TryStep<CheckedFunction<T, R, E>, R, ER, E> {
    }

    public static abstract class WithReference<T, R, ER, E extends Exception>
            implements LimitCheckedFunction<T, R, ER, E>, TryCheckedFunction<T, R, ER, E> {

        protected final Supplier<T> supplier;

        protected Limiter<CheckedFunction<T, R, E>, ER> limiter;

        protected Function<WrappedException, E> exceptionHandler;

        public WithReference(Supplier<T> supplier, Function<WrappedException, E> exceptionHandler) {
            require(supplier != null);
            require(exceptionHandler != null);
            this.supplier = supplier;
            this.exceptionHandler = exceptionHandler;
            this.limiter = f -> f;
        }

        protected abstract Function<CheckedFunction<T, R, E>, ER> limiteeFunction();

        @Override
        public TryCheckedFunction<T, R, ER, E> limit(long timeoutDuration, TimeUnit timeoutUnit) {
            this.limiter = function -> {
                try {
                    TimeLimiter limiter = new SimpleTimeLimiter();
                    //noinspection unchecked
                    Function result = limiter.newProxy(function, Function.class, timeoutDuration, timeoutUnit);
                    ensure(result != null);
                    return result;
                } catch (Exception e) {
                    throw new ImpossibleViolation(e);
                }
            };
            return this;
        }
    }

    public static class WithCloseable<R, E extends Exception> extends WithReference<Closeable, R, Either<WrappedException, R>, E> {

        public WithCloseable(Supplier<Closeable> supplier,
                             Function<WrappedException, E> exceptionHandler) {
            super(supplier, exceptionHandler);
        }

        @Override
        public Function<CheckedFunction<Closeable, R, E>, Either<WrappedException, R>> limiteeFunction() {
            return f -> {
                try (Closeable c = supplier.get()) {
                    require(c != null);
                    Either<WrappedException, R> result = Eithers.either(f).apply(c);
                    ensure(result != null);
                    return result;
                } catch (IOException e) {
                    throw uncheckedException().apply(e);
                }
            };
        }

        @Override
        public R tryA(CheckedFunction<Closeable, R, E> function) throws E {
            return either(function).orThrow(exceptionHandler);
        }

        @Override
        public Either<WrappedException, R> either(CheckedFunction<Closeable, R, E> function) {
            return limiter.apply(limiteeFunction()).apply(function);
        }
    }

    public static class WithCloseableBuilder<R> implements ThrowsStep<Closeable, R, Either<WrappedException, R>> {

        private final Supplier<Closeable> supplier;

        public WithCloseableBuilder(Supplier<Closeable> supplier) {
            require(supplier != null);
            this.supplier = supplier;
        }

        @Override
        public <E extends Exception> WithReference<Closeable, R, Either<WrappedException, R>, E> handle(Function<WrappedException, E> handler) {
            require(handler != null);
            return new WithCloseable<>(supplier, handler);
        }

        @Override
        public <E extends Exception> WithReference<Closeable, R, Either<WrappedException, R>, E> rethrow(Class<E> throwsType) {
            require(throwsType != null);
            return handle(e -> e.unwrapAs(throwsType));
        }

        @Override
        public WithReference<Closeable, R, Either<WrappedException, R>, Exception> wrap() {
            return handle(e -> e);
        }

        @Override
        public WithReference<Closeable, R, Either<WrappedException, R>, RuntimeException> swallow() {
            return handle(e -> null);
        }
    }

    public static <R> WithCloseableBuilder<R> with(Closeable closeable, Class<R> returnType) {
        require(closeable != null);
        require(returnType != null);
        return new WithCloseableBuilder<>(() -> closeable);
    }

    private Try() {
        throw new UnsupportedOperationException();
    }
}
