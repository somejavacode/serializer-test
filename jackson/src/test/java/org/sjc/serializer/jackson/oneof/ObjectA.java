package org.sjc.serializer.jackson.oneof;

public class ObjectA {

    private String stringValue;

    public ObjectA() {
    }

    public ObjectA(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return "ObjectA{" +
                "stringValue='" + stringValue + '\'' +
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

        ObjectA objectA = (ObjectA) o;

        return stringValue != null ? stringValue.equals(objectA.stringValue) : objectA.stringValue == null;
    }

    @Override
    public int hashCode() {
        return stringValue != null ? stringValue.hashCode() : 0;
    }
}
