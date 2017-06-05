package org.sjc.serializer.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.junit.Test;
import org.sjc.serializer.dto.TestDTO;
import org.sjc.serializer.tools.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.sjc.serializer.dto.TestDTO.getDto;

/**
 * try to configure jackson mapper to follow JSON-B (JSR 367)
 */
public class JacksonJsonbConfigTest {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonJsonbConfigTest.class);
    private static final String ENCODING = "UTF-8";

    private ObjectMapper getJsonbMapper() {

        ObjectMapper mapper = new ObjectMapper();

        // JSON-B 3.5.1
        // set time format to ISO8601 ("yyyy-MM-dd'T'HH:mm:ssZ" with time zone UTC)
        mapper.setDateFormat(new ISO8601DateFormat());
        // todo: check LocalDate, LocalTime etc...

        // set base64 variant for binary fields
        // JSON-B 4.10 choose option "BASE_64" (Why is there no default?)
        // JSON-B 4.4 assume I-JSON not enabled, would require Base64Variants.MODIFIED_FOR_URL
        mapper.setBase64Variant(Base64Variants.MIME_NO_LINEFEEDS); // Just assume this. JSON-B spec does not tell.

        // JSON-B 3.14
        // set null handling (do not write null values)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // JSON-B 3.13
        // alphabetic field order (default is: according to field declaration sequence in class)
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

        // JSON-B 3.1
        // declare character encoding of json as UTF-8.
        // nothing to do. this is default.
        // java doc of writeValueAsBytes() says "Encoding used will be UTF-8."

        return mapper;
    }

    @Test
    public void testDate() throws Exception {
        testDTO(getDto(true, false, false));
    }

    @Test
    public void testDateNow() throws Exception {
        testDTO(getDto(true, true, false));
    }

    @Test
    public void testNull() throws Exception {
        testDTO(getDto(false, false, false));
    }

    @Test
    public void testBinary() throws Exception {
        testDTO(getDto(false, false, true));
    }

    private void testDTO(TestDTO dto) throws Exception {
        ObjectWriter writer = getJsonbMapper().writerFor(TestDTO.class);
        byte[] encoded = writer.writeValueAsBytes(dto);
        LOG.info("json bytes:\n" + Hex.toStringBlock(encoded));
        LOG.info("json: " + new String(encoded, ENCODING));
    }

}
