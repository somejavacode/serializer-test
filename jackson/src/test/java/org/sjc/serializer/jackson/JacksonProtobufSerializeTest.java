package org.sjc.serializer.jackson;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.test.AbstractSerializeTest;

public class JacksonProtobufSerializeTest extends AbstractSerializeTest {

    @Override
    protected SerializeService getSerializer() {
        return new JacksonProtobufSerializer();
    }

    @Override
    protected String getInfo() {
        return "Jackson-Protobuf";
    }

}
