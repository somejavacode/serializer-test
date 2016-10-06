package org.sjc.serializer.protostuff;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.test.AbstractSerializeTest;

public class ProtostuffProtobufSerializeTest extends AbstractSerializeTest {

    @Override
    protected SerializeService getSerializer() {
        return new ProtostuffProtobufSerializer();
    }

    @Override
    protected String getInfo() {
        return "Protostuff-Protobuf";
    }
}
