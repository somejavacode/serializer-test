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
    public <T> T deserialize(byte[] bytes, Class<T> type) throws Exception {
        if (type == DataObject.class) {
            Schema<DataObject> schema = RuntimeSchema.getSchema(DataObject.class);
            DataObject ret = schema.newMessage();
            ProtobufIOUtil.mergeFrom(bytes, ret, schema);
            return (T) ret;
        }
        else {
            throw new RuntimeException("not implemented class: " + type);
        }
    }

}
