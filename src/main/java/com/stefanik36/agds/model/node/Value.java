package com.stefanik36.agds.model.node;


import java.util.Objects;

public class Value<T> {
    private T val;
    private Class<T> type;

    public static Value of(Object val) {
        if (val instanceof String) {
            return new Value<>((String) val, String.class);
        } else if (val instanceof Double) {
            return new Value<>((Double) val, Double.class);
        } else {
            throw new RuntimeException("Not supported.");
        }
    }

    private Value(T val, Class<T> type) {
        this.val = val;
        this.type = type;
    }

    public T getVal() {
        return val;
    }

    public Class<T> getType() {
        return type;
    }

    public int compareTo(Value value) {
        if (!this.type.equals(value.getType())) {
            throw new RuntimeException("Another type of objects.");
        }
        if (this.type.equals(Double.class)) {
            return Double.compare((Double) this.val, (Double) value.getVal());
        } else if (this.type.equals(String.class)) {
            return ((String) this.getVal()).compareTo((String) value.getVal());
        } else {
            throw new RuntimeException("Not supported.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value<?> value = (Value<?>) o;
        return Objects.equals(val, value.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }
}
