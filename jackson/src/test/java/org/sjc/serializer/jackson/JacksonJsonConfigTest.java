package org.sjc.serializer.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.junit.Test;
import org.sjc.serializer.tools.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JacksonJsonConfigTest {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonJsonConfigTest.class);

    private static class TestDTO {
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
    }

    private TestDTO getDto() {
        TestDTO value = new TestDTO();
        // note. must not write "\\u000a" for "\\r" (also not in comments, so using \\)
        value.setStringValue("Ü\u00DCmlaut / \\ \n \r \u0001"); // note: source code is UTF-8
        // value.setDateValue(new Date(117, 1, 2));  // crappy constructor! using default time zone, setting time to zero.
        value.setDateValue(new Date(0));  // 1.1.1970 00:00:00 UTC
        return value;
    }

    @Test
    public void testDate() throws Exception {

        // try to configure jackson similar to JSON-B (JSR 367)

        ObjectMapper mapper = new ObjectMapper();

        // set time format to ISO8601 ("yyyy-MM-dd'T'HH:mm:ssZ" with time zone UTC)
        // JSON-B 3.5.1
        mapper.setDateFormat(new ISO8601DateFormat());
        // todo: check LocalDate, LocalTime etc...

        // set base64 variant for binary fields
        // JSON-B 4.4 if I-JSON enabled   // TODO?
        mapper.setBase64Variant(Base64Variants.MODIFIED_FOR_URL);

        // set null handling (do not write null values)
        // JSON-B 3.14
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // alphabetic field order (default is: according to class file)
        // JSON-B 3.13
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

        // todo: declare char encoding of json. UTF-8/NoBOM is already default  (there is class JsonEncoding)

        ObjectWriter writer = mapper.writerFor(TestDTO.class);

        byte[] encoded = writer.writeValueAsBytes(getDto());

        LOG.info("json bytes:\n" + Hex.toStringBlock(encoded));
        LOG.info("json: " + new String(encoded, "UTF-8"));
    }

}
