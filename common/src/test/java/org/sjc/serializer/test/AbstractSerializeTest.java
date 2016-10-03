package org.sjc.serializer.test;

import org.junit.Assert;

import org.junit.Test;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.dto.DataObject;
import org.sjc.serializer.tools.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSerializeTest {

    protected static Logger LOG = LoggerFactory.getLogger(AbstractSerializeTest.class);  // TODO: how to use derived class?

    protected abstract SerializeService getSerializer();
    protected abstract String getInfo();

    @Test
    public void simpleObjectTest() throws Exception {
        DataObject object = new DataObject();
        object.setBytesArray(new byte[] {1, 2, 3, -1, 33});
        object.setType(DataObject.Type.T1);
        object.setStringValue("whatever 1234556 üöä");  // note: this is utf-8 source code
        object.setLongValue(0x112233445566L);

        testSerializeDeserialize(object);
    }

    private void testSerializeDeserialize(Object object) throws Exception {

        LOG.info("serialize type: " + getInfo() + " input: " + object);
        SerializeService service = getSerializer();

        byte[] objBytes = service.serialize(object);
        LOG.info("got bytes with length=" + objBytes.length + " value=" + Hex.toString(objBytes));

        DataObject object2 = (DataObject) service.deserialize(objBytes);

        Assert.assertEquals(object, object2);
    }

}
