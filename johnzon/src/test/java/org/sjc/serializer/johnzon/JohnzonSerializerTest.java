package org.sjc.serializer.johnzon;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.test.AbstractSerializeTest;

public class JohnzonSerializerTest extends AbstractSerializeTest {

    @Override
    protected SerializeService getSerializer() {
        return new JohnzonSerializer();
    }

    @Override
    protected String getInfo() {
        return "Johnzon";
    }

    @Override
    protected boolean isStringFormat() {
        return true;
    }

    /** johnzon 1.2+ is 1000 times slower, reduce repeats */
    protected int getRepeats() {
        return 250;
    }

    @Override
    protected boolean isListImplemented() {
        return true;
    }

}
