package org.sjc.serializer.jackson;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.test.AbstractSerializeTest;

public class JacksonJsonSerializeTest extends AbstractSerializeTest {

    @Override
    protected SerializeService getSerializer() {
        return new JacksonJsonSerializer();
    }

    @Override
    protected String getInfo() {
        return "Jackson-Json";
    }

    @Override
    protected boolean isStringFormat() {
        return true;
    }

    @Override
    protected boolean isListImplemented() {
        return true;
    }

}
