package org.sjc.serializer.dto;

import org.sjc.serializer.tools.Hex;

import java.util.Arrays;

public class DataObject {

    public enum Type {
        T1,
        T2,
        T3,
        T4
    }

    private Type type;

    private long longValue;
    private String stringValue;
    private byte[] bytesArray;

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

    public byte[] getBytesArray() {
        return bytesArray;
    }

    public void setBytesArray(byte[] bytesArray) {
        this.bytesArray = bytesArray;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    // intellij generated methods below

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
        return Arrays.equals(bytesArray, that.bytesArray);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (int) (longValue ^ (longValue >>> 32));
        result = 31 * result + (stringValue != null ? stringValue.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(bytesArray);
        return result;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "type=" + type +
                ", longValue=" + longValue +
                ", stringValue='" + stringValue + '\'' +
                ", bytesArray=" + Hex.toString(bytesArray) +
                '}';
    }
}
