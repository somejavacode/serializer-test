package org.sjc.serializer.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.dto.protostuff.DataObject;

import java.io.InputStream;
import java.io.OutputStream;

public class ProtostuffRuntimeProtobufSerializer implements SerializeService {
    @Override
    public byte[] serialize(Object obj) throws Exception {
        if (obj instanceof DataObject) {
            DataObject dataObject = (DataObject) obj;
            Schema<DataObject> schema = RuntimeSchema.getSchema(DataObject.class);
            LinkedBuffer buffer = LinkedBuffer.allocate(256);
            return ProtobufIOUtil.toByteArray(dataObject, schema, buffer);
            // get same bytes with this code...
            // return ProtostuffIOUtil.toByteArray(dataObject, schema, buffer);
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
            Schema<DataObject> schema = RuntimeSchema.getSchema(DataObject.class);
            DataObject ret = schema.newMessage();
            ProtobufIOUtil.mergeFrom(bytes, ret, schema);
            return ret;
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
