package com.bluecatcode.common.base;

import java.util.concurrent.Callable;

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

    public static <T> Callable<T> callable(Block<T> block) {
        return block::execute;
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

    public static Callable<Void> callable(EffectWithException effect) {
        return () -> {
            effect.cause();
            return null;
        };
    }

}
