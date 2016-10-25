package org.sjc.serializer.protobuf;

import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.protoc.DataObjectOuterClass.DataObject;

import java.io.InputStream;
import java.io.OutputStream;

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
    public void serialize(Object obj, OutputStream os) throws Exception {
        throw new Exception("not supported");
    }

    @Override
    public Object deserialize(byte[] bytes, Class type) throws Exception {
        if (type == DataObject.class) {
            return DataObject.parseFrom(bytes);
        }
        else {
            throw new RuntimeException("not implemented class: " + type);
        }
    }

    @Override
    public Object deserialize(InputStream is, Class type) throws Exception {
        throw new Exception("not supported");
    }
}
