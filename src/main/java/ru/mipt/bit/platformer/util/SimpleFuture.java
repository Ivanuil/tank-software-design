package ru.mipt.bit.platformer.util;

public class SimpleFuture<T> {

    private T value = null;

    public void put(T value) {
        this.value = value;
    }

    public T get() {
        if (value == null) {
            throw new RuntimeException("SimpleFuture is not yet filled or filled with null value");
        }
        return value;
    }

}
