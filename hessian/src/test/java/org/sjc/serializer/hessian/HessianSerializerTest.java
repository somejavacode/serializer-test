package org.sjc.serializer.hessian;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.test.AbstractSerializeTest;

public class HessianSerializerTest extends AbstractSerializeTest {

    @Override
    protected SerializeService getSerializer() {
        return new HessianSerializer();
    }

    @Override
    protected String getInfo() {
        return "Hessian";
    }

    @Override
    protected int getRepeats() {
        return 50000; // too slow, reduce repeat count.
    }

    @Override
    protected boolean isListImplemented() {
        return true;
    }

}
