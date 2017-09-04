package org.sjc.serializer.jackson;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchemaLoader;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.dto.DataList;
import org.sjc.serializer.dto.DataObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class JacksonProtobufSerializer implements SerializeService {

    private ProtobufMapper mapper;
    private HashMap<Class, ProtobufSchema> schemaMap;

    public JacksonProtobufSerializer() {
        schemaMap = new HashMap<Class, ProtobufSchema>();
        mapper = new ProtobufMapper();

        // need one schema per root level message
        try {
            schemaMap.put(DataObject.class,
                    loadSchema(new Class[] { DataObject.class }));

            // need to merge two schemas
            schemaMap.put(DataList.class,
                    // sequence is important!
                    loadSchema(new Class[] { DataList.class, DataObject.class }));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ProtobufSchema loadSchema(Class[] classes) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // merge schema from all Files.
        for (Class clazz: classes) {
            String path = "/proto/" + clazz.getSimpleName() + ".proto";
            InputStream is = this.getClass().getResourceAsStream(path);
            byte[] buffer = new byte[256];
            int bytes = 0;
            while ((bytes = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytes);
            }
        }
        byte[] fileData = baos.toByteArray();
        String schemaString = new String(fileData, "utf-8");
        return ProtobufSchemaLoader.std.parse(schemaString);
        // String path = "common/src/main/resources/proto/" + classes[0].getSimpleName() + ".proto";
        // return ProtobufSchemaLoader.std.load(new File(path));

    }
    @Override
    public byte[] serialize(Object obj) throws Exception {
        ProtobufSchema schema = schemaMap.get(obj.getClass());
        if (schema == null) {
            throw new RuntimeException("not implemented class: " + obj.getClass());
        }
        // todo: thread safe?
        ObjectWriter writer = mapper.writer(schema);
        return writer.writeValueAsBytes(obj);
    }


    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws Exception {
        ProtobufSchema schema = schemaMap.get(type);
        if (schema == null) {
            throw new RuntimeException("not implemented class: " + type);

        }
        // todo: thread safe?
        ObjectReader r = mapper.readerFor(type).with(schema);
        return r.readValue(bytes);
    }

}
