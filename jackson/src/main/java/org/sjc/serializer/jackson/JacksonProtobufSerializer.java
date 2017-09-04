package org.sjc.serializer.jackson;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchemaLoader;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.tools.Validate;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JacksonProtobufSerializer implements SerializeService {

    private static final String PROTO_FILE_ENCODING = "utf-8";

    private ProtobufMapper mapper;
    private HashMap<Class, ProtobufSchema> schemaMap;

    public JacksonProtobufSerializer() {
        schemaMap = new HashMap<Class, ProtobufSchema>();
        mapper = new ProtobufMapper();
    }

    private ProtobufSchema loadSchema(Class clazz) throws Exception {
        String schemaString = loadRecursive(clazz.getSimpleName() + ".proto", new StringBuilder());
        return ProtobufSchemaLoader.std.parse(schemaString);
    }

    // check for import statement and resolve imports recursively to build
    // concatenated "full schema" that is understood by com.squareup.protoparser
    // current assumption is: "import" is not implemented in com.squareup.protoparser
    private String loadRecursive(String name, StringBuilder fullSchema) throws Exception {
        String schemaString = loadProtoFile(name);
        // previous.append(schemaString);
        StringBuilder cleanSchema = new StringBuilder();
        List<String> imports = parseImports(name, schemaString, cleanSchema);
        fullSchema.append(cleanSchema);
        if (imports.size() > 0) {
            for (String importStr : imports) {
                loadRecursive(importStr, fullSchema);
            }
        }
        return fullSchema.toString();
    }

    private List<String> parseImports(String schemaName, String schemaString, StringBuilder schemaWithoutImport) throws Exception {
        ArrayList<String> imports = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new StringReader(schemaString));
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith("syntax")) {
                // check to be proto2
                if (!line.contains("\"proto2\"")) {
                    throw new Exception("proto file " + schemaName + " has not proto2 syntax. this is not supported");
                }
                // skip syntax statement to avoid nested syntax statements
                continue;
            }
            if (line.trim().startsWith("option ")) { // blank to skip "optional", should this be whitespace?
                // skip options as com.squareup.protoparser does not use this
                continue;
            }
            if (line.trim().startsWith("package")) {
                // skip package as com.squareup.protoparser does not use this
                continue;
            }
            // this is sloppy, yes
            if (line.trim().startsWith("import")) {
                int start = line.indexOf('"');
                int stop = line.lastIndexOf('"');
                imports.add(line.substring(start + 1, stop));
            }
            else {
                schemaWithoutImport.append(line).append("\n");
            }
        }
        return imports;
    }

    // loads proto file: currently from class path
    private String loadProtoFile(String name) throws Exception {
        // load as file. did not fix "Import".
        // String path = "common/src/main/resources/proto/" + classes[0].getSimpleName() + ".proto";
        // return ProtobufSchemaLoader.std.load(new File(path));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String path = "/proto/" + name;
        InputStream is = this.getClass().getResourceAsStream(path);
        Validate.notNull(is, "found no resource with path: " + path);
        byte[] buffer = new byte[256];
        int bytes;
        while ((bytes = is.read(buffer)) != -1) {
            baos.write(buffer, 0, bytes);
        }
        return new String(baos.toByteArray(), PROTO_FILE_ENCODING);
    }

    private ProtobufSchema getSchema(Class clazz) throws Exception {
        ProtobufSchema schema = schemaMap.get(clazz);
        if (schema == null) {
            schema = loadSchema(clazz);
            schemaMap.put(clazz, schema);
        }
        return schema;
    }

    @Override
    public byte[] serialize(Object obj) throws Exception {
        ProtobufSchema schema = getSchema(obj.getClass());
        ObjectWriter writer = mapper.writer(schema);
        return writer.writeValueAsBytes(obj);
    }


    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws Exception {
        ProtobufSchema schema = getSchema(type);
        ObjectReader r = mapper.readerFor(type).with(schema);
        return r.readValue(bytes);
    }

}
