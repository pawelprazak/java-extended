package com.bluecatcode.common.io;

import java.io.*;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of
 * objects. Objects are first serialized and then deserialized. Error
 * checking is fairly minimal in this implementation. If an object is
 * encountered that cannot be serialized (or that references an object
 * that cannot be serialized) an exception is thrown.
 */
public class SimpleDeepCopy {

    /**
     * Returns a copy of the object.
     */
    public static <T extends Serializable> T copy(T original) {
        //noinspection unchecked
        return (T) deserialize(serialize(original));
    }

    /**
     * 
     * @param object the object to serialize
     * @param outputStream the output stream to use
     * @return the output stream with serialized representation of the object
     * @throws IllegalArgumentException if OutputStream is null
     * @throws IllegalStateException is an error occurs during serialization
     */
    public static OutputStream serialize(Serializable object, OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("Expected non-null OutputStream");
        }

        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(outputStream);
            out.writeObject(object);
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // Empty
            }
        }
        return outputStream;
    }

    /**
     * @param object the object to serialize
     * @return the byte[] with serialized representation of the object
     * @throws IllegalArgumentException if OutputStream is null
     * @throws IllegalStateException is an error occurs during serialization
     */
    public static byte[] serialize(Serializable object) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream(512);
        serialize(object, byteOut);
        return byteOut.toByteArray();
    }

    /**
     * @param inputStream the input stream to deserialize
     * @return the deserialized object
     * @throws IllegalArgumentException if input stream is null
     * @throws IllegalStateException is an error occurs during deserialization
     */
    public static Object deserialize(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Expected non-null InputStream");
        }
        
        ObjectInputStream input = null;
        Object object;
        try {
            input = new ObjectInputStream(inputStream);
            object = input.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                // Empty
            }
        }
        return object;
    }

    /**
     * @param objectData the byte array to deserialize
     * @return the deserialized object
     * @throws IllegalArgumentException if byte[] is null
     * @throws IllegalStateException is an error occurs during deserialization
     */
    public static Object deserialize(byte[] objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("Expected non-null byte[]");
        }
        ByteArrayInputStream byteIn = new ByteArrayInputStream(objectData);
        return deserialize(byteIn);
    }

}
