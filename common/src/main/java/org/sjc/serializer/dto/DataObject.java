package org.sjc.serializer.dto;

import org.sjc.serializer.tools.Hex;

import java.util.Arrays;

public class DataObject {

    public enum Type {
        T1(0),
        T2(1),
        T3(2),
        T4(3);

        private int id;

        Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Type getById(int id) {
            for (Type t : values()) {
                if (t.getId() == id) {
                    return t;
                }
            }
            throw new RuntimeException("unknown id: " + id);
        }
    }

    private Type type;
    private long longValue;
    private String stringValue;
    private byte[] byteArray;

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    // intellij generated methods below, patched Hex.toString()

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataObject that = (DataObject) o;

        if (longValue != that.longValue) {
            return false;
        }
        if (type != that.type) {
            return false;
        }
        if (stringValue != null ? !stringValue.equals(that.stringValue) : that.stringValue != null) {
            return false;
        }
        return Arrays.equals(byteArray, that.byteArray);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (int) (longValue ^ (longValue >>> 32));
        result = 31 * result + (stringValue != null ? stringValue.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(byteArray);
        return result;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "type=" + type +
                ", longValue=" + longValue +
                ", stringValue='" + stringValue + '\'' +
                ", byteArray=" + Hex.toString(byteArray) +
                '}';
    }
}
