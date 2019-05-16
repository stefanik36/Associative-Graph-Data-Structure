package com.stefanik36.agds.util;

public class NewOrSame<T> {
    private T object;
    private boolean isSame;

    public NewOrSame(T object, boolean isSame) {
        this.object = object;
        this.isSame = isSame;
    }

    public boolean isSame() {
        return isSame;
    }

    public T getObject() {
        return object;
    }
}