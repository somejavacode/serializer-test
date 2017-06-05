package org.sjc.serializer.johnzon;

import org.junit.Test;
import org.sjc.serializer.dto.TestDTO;
import org.sjc.serializer.tools.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbConfig;
import javax.json.bind.spi.JsonbProvider;

import static org.sjc.serializer.dto.TestDTO.getDto;

/**
 * test configuration of jsonb API (using johnzon). Spec JSON-B (JSR 367)
 */
public class JohnzonJsonbApiConfigTest {

    private static final Logger LOG = LoggerFactory.getLogger(JohnzonJsonbApiConfigTest.class);

    private static final String ENCODING = "UTF-8";

    private Jsonb getJsonbMapper() {

        // try to configure JsonbBuilder (JSR 367)

        JsonbConfig config = new JsonbConfig();

        // set base64 variant for binary fields
        // JSON-B 4.10 choose option "BASE_64" (Why is there no default?)
        // JSON-B 4.4 assume I-JSON not enabled, would require setTreatByteArrayAsBase64URL(true)
        config.withBinaryDataStrategy("BASE_64");
//        config.withBinaryDataStrategy("BYTE"); // working
//        config.withBinaryDataStrategy("BASE_58"); // fails with exception

        // set time format to ISO8601 ("yyyy-MM-dd'T'HH:mm:ssZ" with time zone UTC)
        // JSON-B 3.5.1

        // default is rather broken. "2017-06-05T14:38:40.216"

        // DateTimeFormatter.ISO_DATE_TIME, referenced in spec but API need String NOT formatter.
        // throws exception in johnzon. "java.lang.IllegalArgumentException: Unknown pattern letter: I"
        // config.withDateFormat("ISO_DATE_TIME", Locale.ENGLISH);
        // no error but has NO effect?
        // config.withDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

        // set null handling (do not write null values)
        // JSON-B 3.14
        config.withNullValues(false);
//        config.withNullValues(true); working

        // JSON-B 3.1
        // declare character encoding of json as UTF-8.
        // Note: this parameter is "funny" as Jsonb.toJson() returns a String (not bytes)
        config.withEncoding(ENCODING);

        // alphabetic field order (default is: according to class file)
        // JSON-B 3.13
        config.withPropertyOrderStrategy("LEXICOGRAPHICAL");  // default
//        config.withPropertyOrderStrategy("REVERSE"); // working
//        config.withPropertyOrderStrategy("WRONG");  // "gracefully" does nothing...

        return JsonbProvider.provider().create().withConfig(config).build();
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
        String json = getJsonbMapper().toJson(dto);
        LOG.info("json bytes:\n" + Hex.toStringBlock(json.getBytes(ENCODING)));
        LOG.info("json: " + json);
    }

}
