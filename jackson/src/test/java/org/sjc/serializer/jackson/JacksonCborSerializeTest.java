package org.sjc.serializer.jackson;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.test.AbstractSerializeTest;

public class JacksonCborSerializeTest extends AbstractSerializeTest {

    @Override
    protected SerializeService getSerializer() {
        return new JacksonCborSerializer();
    }

    @Override
    protected String getInfo() {
        return "Jackson-CBOR";
    }

    @Override
    protected boolean isListImplemented() {
        return true;
    }
}
