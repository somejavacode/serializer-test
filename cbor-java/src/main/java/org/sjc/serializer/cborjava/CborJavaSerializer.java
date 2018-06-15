package org.sjc.serializer.cborjava;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.MapBuilder;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.dto.DataObject;
import org.sjc.serializer.tools.Validate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class CborJavaSerializer implements SerializeService {

    @Override
    public byte[] serialize(Object obj) throws Exception {

        if (obj instanceof DataObject) {
            DataObject dataObject = (DataObject) obj;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MapBuilder<CborBuilder> builder = new CborBuilder().addMap();
            builder.put("longValue", dataObject.getLongValue());
            if (dataObject.getStringValue() != null) {
                builder.put("stringValue", dataObject.getStringValue());
            }
            if (dataObject.getType() != null) {
                builder.put("type", dataObject.getType().toString());
            }
            if (dataObject.getByteArray() != null) {
                builder.put("byteArray", dataObject.getByteArray());
            }
            new CborEncoder(baos).encode(builder.end().build());

            return baos.toByteArray();
        }
        else {
            throw new RuntimeException("not implemented class: " + obj.getClass());
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws Exception {
        if (type == DataObject.class) {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            List<DataItem> dataItems = new CborDecoder(bais).decode();
            DataObject value = new DataObject();
            Validate.isTrue(dataItems.size() == 1);
            DataItem dataItem = dataItems.get(0);
            // "knowing"
            Validate.isTrue(dataItem instanceof Map);
            Map map = (Map) dataItem;

            DataItem byteString = map.get(new UnicodeString("byteArray"));
            if (byteString != null) {
                Validate.isTrue(byteString instanceof ByteString);
                value.setByteArray(((ByteString) byteString).getBytes());
            }

            DataItem longThing = map.get(new UnicodeString("longValue"));
            Validate.isTrue(longThing instanceof UnsignedInteger);
            value.setLongValue(((UnsignedInteger) longThing).getValue().longValue());

            DataItem stringThing = map.get(new UnicodeString("stringValue"));
            if (stringThing != null) {
                Validate.isTrue(stringThing instanceof UnicodeString);
                value.setStringValue(((UnicodeString) stringThing).getString());
            }

            DataItem typeThing = map.get(new UnicodeString("type"));
            if (typeThing != null) {
                Validate.isTrue(typeThing instanceof UnicodeString);
                value.setType(DataObject.Type.getByName(((UnicodeString) typeThing).getString()));
            }
            return (T) value;
        }
        else {
            throw new RuntimeException("not implemented class: " + type);
        }
    }
}
