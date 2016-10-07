package org.sjc.serializer.protostuff;

import org.junit.Assert;
import org.junit.Test;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.dto.protostuff.DataObject;
import org.sjc.serializer.tools.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// cannot use AbstractSerializeTest as DataObject is different. using "cut and waste" for now.
public class ProtostuffRuntimeProtobufSerializeTest {

    private static Logger LOG = LoggerFactory.getLogger(ProtostuffRuntimeProtobufSerializeTest.class);

    private String getInfo() {
        return "Protostuff-Runtime-Protobuf";
    }

    @Test
    public void simpleObjectTest() throws Exception {
        DataObject object = new DataObject();
        object.setByteArray(new byte[] {1, 2, 3, -1, 33});
        object.setType(DataObject.Type.T1);
        object.setStringValue("whatever 1234556 üöä"); // note: this is utf-8 source code
        object.setLongValue(0x112233445566L);

        testSerializeDeserialize(object, true);
    }

    @Test
    public void speedTest() throws Exception {
        DataObject object = new DataObject();
        object.setByteArray(new byte[128]);
        object.setType(DataObject.Type.T2);
        object.setStringValue("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
        object.setLongValue(0x992233445511L);

        int repeat = 50000;
        long start = System.nanoTime();
        for (int i = 0; i < repeat; i++) {
            testSerializeDeserialize(object, false);
        }
        long time = System.nanoTime() - start;
        LOG.info("serialize/deserialize type: " + getInfo() + ". repeat " + repeat + " times took " + (time / 1000000) +
                "ms. each round took " + (time / 1000 / repeat) + "us");
    }

    @Test
    public void emptyObjectTest() throws Exception {
        // test correct "null" handling
        DataObject object = new DataObject();
        testSerializeDeserialize(object, true);
    }

    @Test
    public void ByteArrayOverheadTest() throws Exception {
        // test binary overhead
        DataObject object = new DataObject();
        object.setByteArray(new byte[150]);
        testSerializeDeserialize(object, true);
        object.setByteArray(new byte[300]);
        testSerializeDeserialize(object, true);
    }

    private void testSerializeDeserialize(Object object, boolean log) throws Exception {

        SerializeService service = new ProtostuffRuntimeProtobufSerializer();

        byte[] objBytes = service.serialize(object);
        if (log) {
            LOG.info("serialized type: " + getInfo() + " input: " + object);
            LOG.info("got bytes with length=" + objBytes.length + " value=" + Hex.toString(objBytes));
        }
        DataObject object2 = (DataObject) service.deserialize(objBytes, DataObject.class);
        Assert.assertEquals(object, object2);
    }

}
