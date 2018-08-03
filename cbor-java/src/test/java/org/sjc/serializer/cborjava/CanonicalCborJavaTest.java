package org.sjc.serializer.cborjava;


import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.MapBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.sjc.serializer.tools.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

/**
 * show how to use cbor-java to follow RFC 7049 section 3.9 "Canonical CBOR"
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
 * Current status: OK. cbor-java output seems to be canonical per default (tests could be more precise)
 * <p>
 */
public class CanonicalCborJavaTest {

    private static final Logger LOG = LoggerFactory.getLogger(CanonicalCborJavaTest.class);

    /**
     * Test §1 "Integers must be as small as possible."
     */
    @Test
    public void testIntegerSize() throws Exception {

        // 0-23
        testIntSize(0, 11);     // A168696E7456616C756500
        testIntSize(23, 11);    // A168696E7456616C756517

        // 24-255
        testIntSize(24, 12);    // A168696E7456616C75651818
        testIntSize(255, 12);   // A168696E7456616C756518FF

        // 256-65535
        testIntSize(256, 13);   // A168696E7456616C7565190100
        testIntSize(65535, 13); // A168696E7456616C756519FFFF

        // 65536-4294967295
        testIntSize(65536, 15);
        testIntSize(Integer.MAX_VALUE, 15);  // 4294967295 in spec is strange
    }

    private void testIntSize(int testInt, int expectedSize) throws Exception {
        MapBuilder<CborBuilder> builder = new CborBuilder().addMap();
        builder.put("intValue", testInt);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new CborEncoder(baos).encode(builder.end().build());
        byte[] encoded = baos.toByteArray();
        LOG.info("CBOR bytes: " + Hex.toString(encoded));
        Assert.assertEquals(expectedSize, encoded.length);
    }
    /**
     * Test §2 "The expression of lengths in major types 2 through 5 must be as short as possible."
     * <p>
     * Note: Testing only type 2 "byte string" here, assume types 3-5 are processed the same way
     */
    @Test
    public void testByteArraySize() throws Exception {

        // 0-23
        testByteArraySize(0, 12);
        testByteArraySize(23, 35);

        // 24-255
        testByteArraySize(24, 37);
        testByteArraySize(255, 268);

        // 256-65535
        testByteArraySize(256, 270);
        testByteArraySize(65535, 65549);

        // 65536-4294967295
        testByteArraySize(65536, 65552);
    }

    private void testByteArraySize(int byteSize, int expectedSize) throws Exception {
        MapBuilder<CborBuilder> builder = new CborBuilder().addMap();
        builder.put("byteArray", new byte[byteSize]);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new CborEncoder(baos).encode(builder.end().build());
        byte[] encoded = baos.toByteArray();
//        LOG.info("CBOR bytes:\n" + Hex.toStringBlock(encoded));
        Assert.assertEquals(expectedSize, encoded.length);
    }

    /**
     * Test §3 "The keys in every map must be sorted lowest value to highest."
     * <p>
     * Detail sorting rules:
     *  If two keys have different lengths, the shorter one sorts earlier;
     *  If two keys have the same length, the one with the lower value in (byte-wise) lexical order sorts earlier.
     */
    @Test
    public void testFieldOrder() throws Exception {

        // in chapter 3.7.: keys probably should be limited to UTF-8 strings only
        // assuming UTF-8 keys seems OK.

        MapBuilder<CborBuilder> builder = new CborBuilder().addMap();
        builder.put("bTe", 1);
        builder.put("zT", 7);
        builder.put("aT", 13);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new CborEncoder(baos).encode(builder.end().build());
        byte[] encoded = baos.toByteArray();


        LOG.info("CBOR bytes:\n" + Hex.toStringBlock(encoded));

        byte[] expected = Hex.fromString( "A36261540D627A54076362546501");
        //                                     a T     z T     b T e
        Assert.assertArrayEquals(expected, encoded);

        // field order is OK: aT, zT, bTe, order of MapBuilder.put() calls is irrelevant
        // canonical sorting order is implemented in co.nstant.in.cbor.encoder.MapEncoder, this is enabled by default.
    }

    /**
     * Test §4 "Indefinite-length items must be made into definite-length items."
     */
    @Test
    public void testIndefiniteLength() {
        // OK for Maps by example: see testIntegerSize "A168696E7456616C756500" A1 .. map with 1 element.
        // assume OK for Arrays
        // OK for Byte String: see testByteArraySize
        // assume OK for Text Strings
    }

}
