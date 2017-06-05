package org.sjc.serializer.johnzon;

import org.apache.johnzon.mapper.Mapper;
import org.apache.johnzon.mapper.MapperBuilder;
import org.junit.Test;
import org.sjc.serializer.dto.TestDTO;
import org.sjc.serializer.tools.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Comparator;

import static org.sjc.serializer.dto.TestDTO.getDto;

/**
 * try to configure johnzon mapper to follow JSON-B (JSR 367)
 */
public class JohnzonJsonbMapperConfigTest {

    private static final Logger LOG = LoggerFactory.getLogger(JohnzonJsonbMapperConfigTest.class);

    private static final String ENCODING = "UTF-8";

    private Mapper getJsonbMapper() {

        // try to configure johnzon mapper for JSON-B (JSR 367)

        Mapper mapper =  new MapperBuilder()
                // JSON-B 3.1
                // declare character encoding of json as UTF-8.
                .setEncoding(ENCODING)

                // set null handling (do not write null values)
                // JSON-B 3.14
                .setSkipNull(true)

                // alphabetic field order (default is: according to class file)
                // JSON-B 3.13
                .setAttributeOrder(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                })

                // set time format to ISO8601 ("yyyy-MM-dd'T'HH:mm:ssZ" with time zone UTC)
                // JSON-B 3.5.1
                // TODO: no easy setter? default format is broken ("19700101010000+0100")

                // set base64 variant for binary fields
                // JSON-B 4.10 choose option "BASE_64" (Why is there no default?)
                // JSON-B 4.4 assume I-JSON not enabled, would require setTreatByteArrayAsBase64URL(true)
                .setTreatByteArrayAsBase64(true).build();

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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        getJsonbMapper().writeObject(dto, baos);
        byte[] encoded = baos.toByteArray();
        LOG.info("json bytes:\n" + Hex.toStringBlock(encoded));
        LOG.info("json: " + new String(encoded, ENCODING));
    }

}
