package org.sjc.serializer.protostuff;

import io.protostuff.CodedInput;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufOutput;
import io.protostuff.WireFormat;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.dto.DataObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ProtostuffRawProtobufSerializer implements SerializeService {

    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkedBuffer lb = LinkedBuffer.allocate(256);  // any rules for this magic number?
        // WriteSession session = new WriteSession(lb); where would this write?
        // WriteSession session = new WriteSession(lb, os);
        // ProtobufOutput output = new ProtobufOutput(lb, os);  not existing?
        ProtobufOutput output = new ProtobufOutput(lb);

        if (obj instanceof DataObject) {
            // hardcoded according to ProtobufConstants.PROTO_DATA_OBJECT
            DataObject data = (DataObject) obj;
            if (data.getType() != null) {
                output.writeUInt32(1, data.getType().getId(), false);
            }
            output.writeSInt64(2, data.getLongValue(), false);
            if (data.getStringValue() != null) {
                output.writeString(3, data.getStringValue(), false);
            }
            if (data.getByteArray() != null) {
                output.writeByteArray(4, data.getByteArray(), false);
            }
            return output.toByteArray();
        }
        else {
            throw new RuntimeException("not implemented class: " + obj.getClass());
        }
    }

    public void serialize(Object obj, OutputStream os) throws Exception {

    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        // CodedInput is "implicit" protocol buffer specific.
        // why is there no ProtobufInput (as ProtobufOutput exists)?
        // other "Inputs" look very similar (i.e. have cut and waste code): ByteArrayInput, ByteBufferInput
        CodedInput input = new CodedInput(in, false);
        if (type == DataObject.class) {
            // hardcoded according to ProtobufConstants.PROTO_DATA_OBJECT
            DataObject ret = new DataObject();
            while (!input.isAtEnd()) {
                int tag = input.readTag();
//                int fieldType = WireFormat.getTagWireType(tag);
                int fieldNr = WireFormat.getTagFieldNumber(tag);
                switch (fieldNr) {
                    case 1:
                        ret.setType(DataObject.Type.getById(input.readInt32()));
                        break;
                    case 2:
                        ret.setLongValue(input.readSInt64());
                        break;
                    case 3:
                        ret.setStringValue(input.readString());
                        break;
                    case 4:
                        ret.setByteArray(input.readByteArray());
                        break;
                    default:
                        throw new RuntimeException("unsupported field nr: " + fieldNr + " tag: " + tag);
                }
            }
            return (T) ret;
        }
        else {
            throw new RuntimeException("not implemented class: " + type);
        }
    }

}
