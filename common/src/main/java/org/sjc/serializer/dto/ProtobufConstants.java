package org.sjc.serializer.dto;


public class ProtobufConstants {

    public static final String PROTO_DATA_OBJECT =
                    "// package org.sjc.serializer.dto;\n" +
                    "// docs message DataObject\n" +
                    "message DataObject {\n" +
                    "    // docs enum Type\n" +
                    "    enum Type {\n" +
                    "        T1 = 0;\n" +
                    "        // docs enum value T2\n" +
                    "        T2 = 1;\n" +
                    "        T3 = 2;\n" +
                    "        T4 = 3;\n" +
                    "    }\n" +
                    "    // docs field type\n" +
                    "    optional Type type = 1;\n" +
                    "    // docs field longValue\n" +
                    "    optional int64 longValue = 2;\n" +
                    "    optional string stringValue = 3;\n" +
                    "    optional bytes byteArray = 4;\n" +
                    "}";
}
