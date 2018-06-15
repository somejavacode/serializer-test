package org.sjc.serializer.cborjava;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.test.AbstractSerializeTest;

public class CborJavaSerializerTest extends AbstractSerializeTest {

    @Override
    protected SerializeService getSerializer() {
        return new CborJavaSerializer();
    }

    @Override
    protected String getInfo() {
        return "CborJava";
    }

    @Override
    protected boolean isStringFormat() {
        return false;
    }

    @Override
    protected boolean isListImplemented() {
        return false;
    }

    @Override
    protected int getRepeats() {
        return 50000; // too slow, reduce repeat count.
    }

}
