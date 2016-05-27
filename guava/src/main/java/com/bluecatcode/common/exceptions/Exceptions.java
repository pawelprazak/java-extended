package com.bluecatcode.common.exceptions;

import com.bluecatcode.common.contract.errors.RequireViolation;
import com.bluecatcode.common.functions.CheckedFunction;
import com.google.common.base.Function;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static com.bluecatcode.common.contract.Preconditions.require;

public class Exceptions {

    private Exceptions() {
        throw new UnsupportedOperationException();
    }

    public static Function<Exception, RuntimeException> uncheckedException() {
        return e -> e instanceof RuntimeException ? (RuntimeException) e : new UncheckedException(e);
    }

    public static Function<WrappedException, RuntimeException> unwrapToUncheckedException() {
        return e -> uncheckedException().apply(e.unwrap());
    }

    public static <E extends Exception> E exception(Class<E> throwableType) {
        return throwable(throwableType);
    }

    public static <E extends Throwable> E throwable(Class<E> throwableType) {
        return throwable(throwableType, parameters(), arguments());
    }

    public static <E extends Exception> E exception(Class<E> throwableType,
                                                    CheckedFunction<Class<E>, Constructor<E>, ReflectiveOperationException> constructorSupplier,
                                                    CheckedFunction<Constructor<E>, E, ReflectiveOperationException> instanceSupplier) {
        return throwable(throwableType, constructorSupplier, instanceSupplier);
    }

    public static <E extends Throwable> E throwable(Class<E> throwableType,
                                                    CheckedFunction<Class<E>, Constructor<E>, ReflectiveOperationException> constructorSupplier,
                                                    CheckedFunction<Constructor<E>, E, ReflectiveOperationException> instanceSupplier) {
        require(throwableType != null, "Expected non-null throwableType");
        require(constructorSupplier != null, "Expected non-null constructorSupplier");
        require(instanceSupplier != null, "Expected non-null instanceSupplier");
        require(!Modifier.isAbstract(throwableType.getModifiers()),
                "Expected non-abstract throwable type, got: '%s'", throwableType.getCanonicalName());
        final Constructor<E> constructor;
        try {
            constructor = constructorSupplier.apply(throwableType);
            require(constructor != null, "Expected constructor supplier to return non-null reference");
        } catch (ReflectiveOperationException e) {
            throw new RequireViolation("Expected a throwable with (String) constructor", e);
        }
        try {
            E instance = instanceSupplier.apply(constructor);
            require(instance != null, "Expected instance supplier to return non-null reference");
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RequireViolation("Expected an instantiable throwable.", e);
        }
    }

    public static <E> CheckedFunction<Class<E>, Constructor<E>, ReflectiveOperationException> parameters(Class<?> parameterType) {
        return type -> type.getConstructor(parameterType);
    }

    public static <E> CheckedFunction<Class<E>, Constructor<E>, ReflectiveOperationException> parameters(Class<?>... parameterTypes) {
        return type -> type.getConstructor(parameterTypes);
    }

    public static <E> CheckedFunction<Constructor<E>, E, ReflectiveOperationException> arguments(Object arg) {
        return constructor -> constructor.newInstance(arg);
    }

    public static <E> CheckedFunction<Constructor<E>, E, ReflectiveOperationException> arguments(Object... args) {
        return constructor -> constructor.newInstance(args);
    }
}
