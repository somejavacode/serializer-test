package org.sjc.serializer.protobuf;

import com.google.protobuf.ByteString;
import org.junit.Assert;
import org.junit.Test;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.protoc.DataObjectOuterClass;
import org.sjc.serializer.tools.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// cannot use AbstractSerializeTest as DataObject is different. using "cut and waste" for now.
public class ProtobufSerializeTest {

    private static Logger LOG = LoggerFactory.getLogger(ProtobufSerializeTest.class);

    private String getInfo() {
        return "Protobuf";
    }

    @Test
    public void simpleObjectTest() throws Exception {
        SerializeService service = new ProtobufSerializer();
        DataObjectOuterClass.DataObject object = DataObjectOuterClass.DataObject.newBuilder()
            .setByteArray(ByteString.copyFrom(new byte[] {1, 2, 3, -1, 33}))
            .setType(DataObjectOuterClass.DataObject.Type.T1)
            .setStringValue("whatever 1234556 üöä") // note: this is utf-8 source code
            .setLongValue(0x112233445566L).build();

        testSerializeDeserialize(service, object, true);
    }

    @Test
    public void speedTest() throws Exception {
        DataObjectOuterClass.DataObject object = DataObjectOuterClass.DataObject.newBuilder()
                .setByteArray(ByteString.copyFrom(new byte[128]))
                .setType(DataObjectOuterClass.DataObject.Type.T2)
                .setStringValue("Lorem ipsum dolor sit amet, consectetur adipiscing elit")
                .setLongValue(0x992233445511L).build();

        SerializeService service = new ProtobufSerializer();
        int repeat = 250000;
        // warm up
        for (int i = 0; i < repeat; i++) {
            testSerializeDeserialize(service, object, false);
        }
        long start = System.nanoTime();
        for (int i = 0; i < repeat; i++) {
            testSerializeDeserialize(service, object, false);
        }
        long time = System.nanoTime() - start;
        LOG.info("serialize/deserialize type: " + getInfo() + ". repeat " + repeat + " times took " + (time / 1000000) +
                "ms. each round took " + (time / repeat) + "ns");
    }

    @Test
    public void emptyObjectTest() throws Exception {
        SerializeService service = new ProtobufSerializer();
        // test correct "null" handling
        DataObjectOuterClass.DataObject object = DataObjectOuterClass.DataObject.newBuilder().build();
        testSerializeDeserialize(service, object, true);
    }

    @Test
    public void ByteArrayOverheadTest() throws Exception {
        SerializeService service = new ProtobufSerializer();
        // test binary overhead
        DataObjectOuterClass.DataObject object = DataObjectOuterClass.DataObject.newBuilder()
                .setByteArray(ByteString.copyFrom(new byte[150])).build();

        testSerializeDeserialize(service, object, true);

        object = DataObjectOuterClass.DataObject.newBuilder()
                .setByteArray(ByteString.copyFrom(new byte[300])).build();
        testSerializeDeserialize(service, object, true);
    }

    private void testSerializeDeserialize(SerializeService service, Object object, boolean log) throws Exception {

        byte[] objBytes = service.serialize(object);
        if (log) {
            LOG.info("serialized type: " + getInfo() + " input: " + object);
            LOG.info("got bytes with length=" + objBytes.length + " value=" + Hex.toString(objBytes));
        }
        DataObjectOuterClass.DataObject object2 = service.deserialize(objBytes, DataObjectOuterClass.DataObject.class);
        Assert.assertEquals(object, object2);
    }

}
