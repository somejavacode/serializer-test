package org.sjc.serializer.dto.protostuff;

import io.protostuff.Tag;
import org.sjc.serializer.tools.Hex;

import java.util.Arrays;

/**
 * NOTE: this is a copy of org.sjc.serializer.dto.DataObject with protostuff Tag annotations added. <br/>
 * field numbers should match
 */

public class DataObject {

    public enum Type {
        @Tag(0)  // does this work as expected? YES!
        T1,
        @Tag(1)
        T2,
        @Tag(2)
        T3,
        @Tag(3)
        T4
    }

    @Tag(1)
    private Type type;
    @Tag(2)
    private long longValue;
    @Tag(3)
    private String stringValue;
    @Tag(4)
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
