package org.sjc.serializer.jackson.oneof;

public class ObjectB {

    private long longValue;

    public ObjectB() {
    }

    public ObjectB(long longValue) {
        this.longValue = longValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    @Override
    public String toString() {
        return "ObjectB{" +
                "longValue=" + longValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ObjectB objectB = (ObjectB) o;

        return longValue == objectB.longValue;
    }

    @Override
    public int hashCode() {
        return (int) (longValue ^ (longValue >>> 32));
    }
}
