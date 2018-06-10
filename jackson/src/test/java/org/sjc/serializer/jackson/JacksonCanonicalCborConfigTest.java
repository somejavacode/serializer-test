package org.sjc.serializer.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import org.junit.Assert;
import org.junit.Test;
import org.sjc.serializer.dto.TestDTO;
import org.sjc.serializer.tools.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * try to configure jackson CBOR mapper to follow RFC 7049 section 3.9 "Canonical CBOR"
 * <p>
 * see https://tools.ietf.org/html/rfc7049#section-3.9
 * <p>
 * §1 "Integers must be as small as possible."
 * <p>
 * §2 "The expression of lengths in major types 2 through 5 must be as short as possible."
 * <p>
 * §3 "The keys in every map must be sorted lowest value to highest."
 * <p>
 * §4 "Indefinite-length items must be made into definite-length items."
 * <p>
 * Current status: FAILED
 * <p>
 * $3 fails, cannot change sorting $4 fails: see no option to fix this.
 */
public class JacksonCanonicalCborConfigTest {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonCanonicalCborConfigTest.class);

    private ObjectMapper getCborMapper() {

        ObjectMapper mapper = new ObjectMapper(new CBORFactory());

        // this is not defined, so omitting empty fields
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // try to configure §3: alphabetic field order is NOT SUFFICIENT
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

        return mapper;
    }

    /**
     * Test §1 "Integers must be as small as possible."
     */
    @Test
    public void testIntegerSize() throws Exception {
        // there is com.fasterxml.jackson.dataformat.cbor.CBORGenerator.Feature
        // WRITE_MINIMAL_INTS: it is "true" by default

        ObjectWriter writer = getCborMapper().writerFor(TestDTO.class);

        // 0-23
        testIntSize(writer, 0, 12);     // BF 68 69 6E 74 56 61 6C 75 65 00 FF
        testIntSize(writer, 23, 12);    // BF 68 69 6E 74 56 61 6C 75 65 17 FF

        // 24-255
        testIntSize(writer, 24, 13);    // BF 68 69 6E 74 56 61 6C 75 65 18 18 FF
        testIntSize(writer, 255, 13);   // BF 68 69 6E 74 56 61 6C 75 65 18 FF FF

        // 256-65535
        testIntSize(writer, 256, 14);   // BF 68 69 6E 74 56 61 6C 75 65 19 01 00 FF
        testIntSize(writer, 65535, 14); // BF 68 69 6E 74 56 61 6C 75 65 19 FF FF FF

        // 65536-4294967295
        testIntSize(writer, 65536, 16); // BF 68 69 6E 74 56 61 6C 75 65 1A 00 01 00 00 FF
        testIntSize(writer, Integer.MAX_VALUE, 16);  // 4294967295 in spec is strange
    }

    private void testIntSize(ObjectWriter writer, int testInt, int expectedSize) throws Exception {
        TestDTO dto = new TestDTO();
        dto.setIntValue(testInt);
        byte[] encoded = writer.writeValueAsBytes(dto);
        LOG.info("CBOR bytes:\n" + Hex.toStringBlock(encoded));
        Assert.assertEquals(expectedSize, encoded.length);
    }
    /**
     * Test §2 "The expression of lengths in major types 2 through 5 must be as short as possible."
     * <p>
     * Note: Testing only type 2 "byte string" here, assume types 3-5 are processed identical
     */
    @Test
    public void testByteArraySize() throws Exception {
        ObjectWriter writer = getCborMapper().writerFor(TestDTO.class);

        // 0-23
        testByteArraySize(writer, 0, 23);
        testByteArraySize(writer, 23, 46);

        // 24-255
        testByteArraySize(writer, 24, 48);
        testByteArraySize(writer, 255, 279);

        // 256-65535
        testByteArraySize(writer, 256, 281);
        testByteArraySize(writer, 65535, 65560);

        // 65536-4294967295
        testByteArraySize(writer, 65536, 65563);
    }

    private void testByteArraySize(ObjectWriter writer, int byteSize, int expectedSize) throws Exception {
        TestDTO dto = new TestDTO();
        dto.setByteArray(new byte[byteSize]);
        byte[] encoded = writer.writeValueAsBytes(dto);
//        LOG.info("CBOR bytes:\n" + Hex.toStringBlock(encoded));
        Assert.assertEquals(expectedSize, encoded.length);
    }

    /**
     * Test §3 "The keys in every map must be sorted lowest value to highest."
     */
    @Test
    public void testFieldOrder() throws Exception {
        ObjectWriter writer = getCborMapper().writerFor(TestSortDTO.class);
        // in chapter 3.7.: keys probably should be limited to UTF-8 strings only
        // assuming UTF-8 keys seems OK.

        TestSortDTO dto = new TestSortDTO(2, 1, 3);
        byte[] encoded = writer.writeValueAsBytes(dto);
        LOG.info("CBOR bytes:\n" + Hex.toStringBlock(encoded));
        // sort order is aTestInt, bTestInteger, zTestInt
        // Assert.fail();
    }

    private static class TestSortDTO {
        private int bTestInteger; // 3rd
        private int zTestInt;  // 2nd
        private int aTestInt;  // 1st

        public TestSortDTO(int zTestInt, int aTestInt, int bTestInteger) {
            this.zTestInt = zTestInt;
            this.aTestInt = aTestInt;
            this.bTestInteger = bTestInteger;
        }

        public int getzTestInt() {
            return zTestInt;
        }

        public int getaTestInt() {
            return aTestInt;
        }

        public int getbTestInteger() {
            return bTestInteger;
        }
    }

    /**
     * Test §4 "Indefinite-length items must be made into definite-length items."
     */
    @Test
    public void testIndefiniteLength() {
        // Assert.fail();
        // currently mapper converts objects to maps with "Indefinite-Length"
        // e.g. BF 68 69 6E 74 56 61 6C 75 65 00 FF
    }

}
