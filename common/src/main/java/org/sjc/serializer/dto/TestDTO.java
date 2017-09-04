package org.sjc.serializer.dto;

import java.util.Date;

/**
 * test object with different field types
 */
public class TestDTO {
    private int intValue;
    private Date dateValue;
    private String stringValue;
    private byte[] byteArray;

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public static TestDTO getDto(boolean withDate, boolean now, boolean withBytes) {
        TestDTO value = new TestDTO();
        // note. must not write "\\u000a" for "\\r" (also not in comments, so using \\)
        value.setStringValue("Ü\u00DCmlaut / \\ \n \r \u0001"); // note: source code is UTF-8
        if (withDate) {
            if (now) {
                value.setDateValue(new Date());
            }
            else {
                value.setDateValue(new Date(0));  // 1.1.1970 00:00:00 UTC
            }
        }
        if (withBytes) {
            value.setByteArray(new byte[66]);  // long enough to see if there is new line in base64.
        }
        return value;
    }

}