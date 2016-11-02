package org.sjc.serializer.jackson;

import org.junit.Assert;
import org.junit.Test;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.dto.DataList;

import org.sjc.serializer.dto.DataObject;
import org.sjc.serializer.test.AbstractSerializeTest;
import org.sjc.serializer.tools.Hex;

public class JacksonProtobufSerializeTest extends AbstractSerializeTest {

    @Override
    protected SerializeService getSerializer() {
        return new JacksonProtobufSerializer();
    }

    @Override
    protected String getInfo() {
        return "Jackson-Protobuf";
    }

    // list test only for JacksonProtobuf

    @Test
    public void objectListTest() throws Exception {
        SerializeService service = getSerializer();

        DataList list = new DataList();
        // test empty
        testSerializeDeserializeList(service, list, true);

        DataObject object = new DataObject();
        object.setType(DataObject.Type.T1);
        object.setLongValue(1);
        list.addObject(object);

        // test single element
        testSerializeDeserializeList(service, list, true);

        DataObject object2 = new DataObject();
        object2.setType(DataObject.Type.T4);
        object2.setLongValue(4);
        list.addObject(object2);
        DataObject object3 = new DataObject();
        object3.setType(DataObject.Type.T3);
        object3.setLongValue(3);
        list.addObject(object3);

        // test multiple elements
        testSerializeDeserializeList(service, list, true);
    }

    private void testSerializeDeserializeList(SerializeService service, Object object, boolean log) throws Exception {
        byte[] objBytes = service.serialize(object);
        if (log) {
            LOG.info("serialized type: " + getInfo() + " input: " + object);
            LOG.info("got bytes with length=" + objBytes.length + " value=" + Hex.toString(objBytes));
        }
        DataList object2 = service.deserialize(objBytes, DataList.class);
        Assert.assertEquals(object, object2);
    }

}
