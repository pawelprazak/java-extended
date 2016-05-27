package com.bluecatcode.common.base;

import com.bluecatcode.common.exceptions.WrappedException;
import com.bluecatcode.common.functions.Block;
import com.bluecatcode.common.functions.CheckedBlock;
import com.bluecatcode.common.functions.CheckedFunction;
import com.bluecatcode.common.functions.Function;
import com.google.common.annotations.Beta;

import java.util.concurrent.Callable;

import static com.bluecatcode.common.contract.Preconditions.require;
import static com.bluecatcode.common.exceptions.WrappedException.wrap;

@Beta
public class Eithers {

    public static <T, R, E extends Exception> Function<T, Either<WrappedException, R>> either(CheckedFunction<T, R, E> function) {
        require(function != null, "Expected non-null function");
        return input -> {
            try {
                R reference = function.apply(input);
                require(reference != null, "Expected function to return non-null reference");
                return Either.valueOf(reference);
            } catch (Exception e) {
                return Either.errorOf(wrap(e));
            }
        };
    }

    public static <T, R> Function<T, Either<WrappedException, R>> either(Function<T, R> function) {
        require(function != null, "Expected non-null function");
        return input -> {
            try {
                R reference = function.apply(input);
                require(reference != null, "Expected function to return non-null reference");
                return Either.valueOf(reference);
            } catch (Exception e) {
                return Either.errorOf(wrap(e));
            }
        };
    }

    public static <R, E extends Exception> Either<WrappedException, R> either(CheckedBlock<R, E> block) {
        require(block != null, "Expected non-null block");
        try {
            R reference = block.execute();
            require(reference != null, "Expected block to return non-null reference");
            return Either.valueOf(reference);
        } catch (Exception e) {
            return Either.errorOf(wrap(e));
        }
    }

    public static <R> Either<WrappedException, R> either(Block<R> block) {
        require(block != null, "Expected non-null block");
        try {
            R reference = block.execute();
            require(reference != null, "Expected block to return non-null reference");
            return Either.valueOf(reference);
        } catch (Exception e) {
            return Either.errorOf(wrap(e));
        }
    }

    public static <R> Either<WrappedException, R> either(Callable<R> callable) {
        require(callable != null, "Expected non-null callable");
        try {
            R reference = callable.call();
            require(reference != null, "Expected callable to return non-null reference");
            return Either.valueOf(reference);
        } catch (Exception e) {
            return Either.errorOf(wrap(e));
        }
    }

    private Eithers() {
        throw new UnsupportedOperationException();
    }
}
