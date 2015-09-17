package com.bluecatcode.common.base;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @see <a href="http://docs.oracle.com/javase/8/docs/api/java/lang/FunctionalInterface.html">Functional Interface</a>
 * @see com.bluecatcode.common.base.Predicates
 * @see com.google.common.base.Predicates
 * @see com.google.common.base.Functions
 * @see com.google.common.base.Suppliers
 * @see com.bluecatcode.common.base.Block
 * @see com.bluecatcode.common.base.Consumer
 * @see com.bluecatcode.common.base.Effect
 * @see com.google.common.base.Predicate
 * @see com.google.common.base.Function
 * @see com.google.common.base.Supplier
 * @see java.util.concurrent.Callable
 */
public class FunctionalInterfaces {

    public static <T> T safeCall(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T safeCall(BlockWithException<T> block) {
        try {
            return block.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Block<Void> block(Effect effect) {
        return () -> {
            effect.cause();
            return null;
        };
    }

    public static <T> Callable<T> callable(BlockWithException<T> block) {
        return block::execute;
    }

    public static Callable<Void> callable(Effect effect) {
        return () -> {
            effect.cause();
            return null;
        };
    }

    public static <T> Function<Void, T> function(Callable<T> callable) {
        return v -> safeCall(callable);
    }

    public static <T> Function<Void, T> function(BlockWithException<T> block) {
        return v -> safeCall(block);
    }

    public static <T> Function<Void, T> function(Block<T> block) {
        return v -> block.execute();
    }


    public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) {
        return Functions.compose(g, f);
    }

    public static Function<Void, Void> forEffect(Effect effect) {
        return new EffectFunction(effect);
    }

    /**
     * @see #forEffect(Effect)
     */
    private static class EffectFunction implements Function<Void, Void>, Serializable {

        private final Effect effect;

        public EffectFunction(Effect effect) {
            this.effect = effect;
        }

        @Override
        public Void apply(@Nullable Void input) {
            effect.cause();
            return null;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof EffectFunction) {
                EffectFunction that = (EffectFunction) obj;
                return this.effect.equals(that.effect);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return effect.hashCode();
        }

        @Override
        public String toString() {
            return "forEffect(" + effect + ")";
        }

        private static final long serialVersionUID = 0;
    }

    public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
        return Functions.forPredicate(predicate);
    }

    /**
     * @see Functions#forSupplier(Supplier)
     */
    public static <T> Function<Void, T> forSupplier(Supplier<T> supplier) {
        return new SupplierFunction<>(supplier);
    }

    /**
     * @see #forSupplier
     */
    private static class SupplierFunction<T> implements Function<Void, T>, Serializable {

        private final Supplier<T> supplier;

        private SupplierFunction(Supplier<T> supplier) {
            this.supplier = checkNotNull(supplier);
        }

        @Override
        public T apply(@Nullable Void input) {
            return supplier.get();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof SupplierFunction) {
                SupplierFunction<?> that = (SupplierFunction<?>) obj;
                return this.supplier.equals(that.supplier);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return supplier.hashCode();
        }

        @Override
        public String toString() {
            return "forSupplier(" + supplier + ")";
        }

        private static final long serialVersionUID = 0;
    }

}
