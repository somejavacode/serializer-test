package org.sjc.serializer.protobuf;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.protoc.test.DataObject;


public class ProtobufSerializer implements SerializeService {
    @Override
    public byte[] serialize(Object obj) throws Exception {
        if (obj instanceof DataObject) {
            DataObject dataObject = (DataObject) obj;
            return dataObject.toByteArray();
        }
        else {
            throw new RuntimeException("not implemented class: " + obj.getClass());
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws Exception {
        if (type == DataObject.class) {
            return (T) DataObject.parseFrom(bytes);
        }
        else {
            throw new RuntimeException("not implemented class: " + type);
        }
    }

}
