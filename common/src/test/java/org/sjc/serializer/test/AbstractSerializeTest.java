package org.sjc.serializer.test;

import org.junit.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.dto.DataObject;
import org.sjc.serializer.tools.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public abstract class AbstractSerializeTest {

    protected static Logger LOG = LoggerFactory.getLogger(AbstractSerializeTest.class);  // TODO: how to use derived class?

    protected abstract SerializeService getSerializer();
    protected abstract String getInfo();

    /** override for slower serializers */
    protected int getRepeats() {
        return 50000;
    }

    @Test
    public void simpleObjectTest() throws Exception {
        DataObject object = new DataObject();
        object.setByteArray(new byte[] {1, 2, 3, -1, 33});
        object.setType(DataObject.Type.T1);
        object.setStringValue("whatever 1234556 üöä");  // note: this is utf-8 source code
        object.setLongValue(0x112233445566L);

        testSerializeDeserialize(object, true);
    }

    @Test
    @Ignore("does not work with current API")
    public void simpleStreamTest() throws Exception {
        DataObject object1 = new DataObject();
        object1.setByteArray(new byte[] {1, 2, 3, -1, 33});
        object1.setType(DataObject.Type.T1);
        object1.setStringValue("whatever 1234556 üöä");  // note: this is utf-8 source code
        object1.setLongValue(0x112233445566L);

        DataObject object2 = new DataObject();
        object2.setByteArray(new byte[] {3, 2, 5, -1, 33});
        object2.setType(DataObject.Type.T2);
        object2.setStringValue("what else");
        object2.setLongValue(0x122233445566L);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializeService service = getSerializer();
        service.serialize(object1, baos);
        service.serialize(object2, baos);
        baos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        // Problem: first call "eats" all bytes (second call fails obviously)
        // parser.releaseBuffered() would return already consumed stream but that is cumbersome and breaks API
        DataObject object1d = (DataObject) service.deserialize(bais, DataObject.class);
        Assert.assertEquals(object1, object1d);
        DataObject object2d = (DataObject) service.deserialize(bais, DataObject.class);
        Assert.assertEquals(object2, object2d);
    }

    @Test
    public void speedTest() throws Exception {
        DataObject object = new DataObject();
        object.setByteArray(new byte[128]);
        object.setType(DataObject.Type.T2);
        object.setStringValue("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
        object.setLongValue(0x992233445511L);


        int repeat = getRepeats();
        // warm up
        for (int i = 0; i < repeat; i++) {
            testSerializeDeserialize(object, false);
        }
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

        SerializeService service = getSerializer();

        byte[] objBytes = service.serialize(object);
        if (log) {
            LOG.info("serialized type: " + getInfo() + " input: " + object);
            LOG.info("got bytes with length=" + objBytes.length + " value=" + Hex.toString(objBytes));
        }
        DataObject object2 = (DataObject) service.deserialize(objBytes, DataObject.class);
        Assert.assertEquals(object, object2);
    }

}
