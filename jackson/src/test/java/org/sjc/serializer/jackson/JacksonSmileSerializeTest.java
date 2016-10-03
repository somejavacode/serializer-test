package org.sjc.serializer.jackson;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.test.AbstractSerializeTest;

public class JacksonSmileSerializeTest extends AbstractSerializeTest {

    @Override
    protected SerializeService getSerializer() {
        return new JacksonSmileSerializer();
    }

    @Override
    protected String getInfo() {
        return "Jackson-SMILE";
    }
}
