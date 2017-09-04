package org.sjc.serializer.jackson;

import org.junit.Assert;
import org.junit.Test;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.jackson.oneof.ObjectA;
import org.sjc.serializer.jackson.oneof.ObjectB;
import org.sjc.serializer.jackson.oneof.OneofData;
import org.sjc.serializer.test.AbstractSerializeTest;
import org.sjc.serializer.tools.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonProtobufOneofTest {

    private static Logger LOG = LoggerFactory.getLogger(AbstractSerializeTest.class);

    private SerializeService getSerializer() {
        return new JacksonProtobufSerializer();
    }

    private String getInfo() {
        return "Jackson-Protobuf";
    }

    private boolean isStringFormat() {
        return false;
    }

    @Test
    public void oneOfTest() throws Exception {
        SerializeService service = getSerializer();

        OneofData object = new OneofData();
        testSerializeDeserialize(service,  object, true);

        object.setObjA(new ObjectA("StringTest1"));
        testSerializeDeserialize(service,  object, true);

        object.setObjB(new ObjectB(222));
        testSerializeDeserialize(service,  object, true);
    }


    private void testSerializeDeserialize(SerializeService service, Object object, boolean log) throws Exception {
        byte[] objBytes = service.serialize(object);
        if (log) {
            LOG.info("serialized type: " + getInfo() + " input: " + object);
            // encoding hard coded for now.
            String value = isStringFormat() ? new String(objBytes, "UTF-8") : Hex.toString(objBytes);
            LOG.info("got bytes with length=" + objBytes.length + " value=" + value);
        }
        Object object2 = service.deserialize(objBytes, object.getClass());
        Assert.assertEquals(object, object2);
    }
}
