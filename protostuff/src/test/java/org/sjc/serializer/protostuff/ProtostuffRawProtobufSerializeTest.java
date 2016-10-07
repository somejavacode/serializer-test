package org.sjc.serializer.protostuff;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.test.AbstractSerializeTest;

public class ProtostuffRawProtobufSerializeTest extends AbstractSerializeTest {

    @Override
    protected SerializeService getSerializer() {
        return new ProtostuffRawProtobufSerializer();
    }

    @Override
    protected String getInfo() {
        return "Protostuff-Raw-Protobuf";
    }
}
