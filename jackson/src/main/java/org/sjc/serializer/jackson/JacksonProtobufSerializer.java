package org.sjc.serializer.jackson;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchemaLoader;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.dto.DataObject;
import org.sjc.serializer.dto.ProtobufConstants;

import java.io.InputStream;
import java.io.OutputStream;

public class JacksonProtobufSerializer implements SerializeService {

    private ProtobufMapper mapper;
    private ProtobufSchema schemaDataObject;

    public JacksonProtobufSerializer() {
        mapper = new ProtobufMapper();
        try {
            // automatically, generates proto file, but has one "error": "repeated bytes byteArray = 4;"
            // schemaDataObject = mapper.generateSchemaFor(DataObject.class);

            // load schema from String...
            schemaDataObject = ProtobufSchemaLoader.std.parse(ProtobufConstants.PROTO_DATA_OBJECT);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] serialize(Object obj) throws Exception {
        if (obj instanceof DataObject) {
            // todo: thread safe?
            ObjectWriter writer = mapper.writer(schemaDataObject);
            return writer.writeValueAsBytes(obj);
        }
        else {
            throw new RuntimeException("not implemented class: " + obj.getClass());
        }
    }

    @Override
    public void serialize(Object obj, OutputStream os) throws Exception {
        throw new RuntimeException("not supported");
    }

    @Override
    public Object deserialize(byte[] bytes, Class type) throws Exception {
        if (type == DataObject.class) {
            // todo: thread safe?
            ObjectReader r =  mapper.readerFor(DataObject.class).with(schemaDataObject);
            return r.readValue(bytes);
        }
        else {
            throw new RuntimeException("not implemented class: " + type);
        }
    }

    @Override
    public Object deserialize(InputStream is, Class type) throws Exception {
        throw new RuntimeException("not supported");
    }
}
