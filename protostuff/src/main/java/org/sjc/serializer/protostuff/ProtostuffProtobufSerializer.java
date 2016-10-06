package org.sjc.serializer.protostuff;

import io.protostuff.CodedInput;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufOutput;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.dto.DataObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ProtostuffProtobufSerializer implements SerializeService {
    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serialize(obj, out);
        return out.toByteArray();
    }

    @Override
    public void serialize(Object obj, OutputStream os) throws Exception {

        LinkedBuffer lb = LinkedBuffer.allocate(256);  // any rules for this magic number?
        // WriteSession session = new WriteSession(lb); where would this write?
        // WriteSession session = new WriteSession(lb, os);
        // ProtobufOutput output = new ProtobufOutput(lb, os);  not existing?
        ProtobufOutput output = new ProtobufOutput(lb);

        if (obj instanceof DataObject) {
            DataObject data = (DataObject) obj;
            if (data.getType() != null) {
                // manual field numbering here...
                output.writeUInt32(1, data.getType().ordinal(), false);
            }
            output.writeSInt64(2, data.getLongValue(), false);
            if (data.getStringValue() != null) {
                output.writeString(3, data.getStringValue(), false);
            }
            if (data.getByteArray() != null) {
                output.writeByteArray(4, data.getByteArray(), false);
            }
            byte[] bytes = output.toByteArray();
            os.write(bytes);
        }
        else {
            throw new RuntimeException("not implemented class: " + obj.getClass());
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class type) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return deserialize(in, type);
    }

    @Override
    public Object deserialize(InputStream is, Class type) throws Exception {

        CodedInput input = CodedInput.newInstance(is); // why is there no ProtobufInput?
        if (type == DataObject.class) {
            DataObject ret = new DataObject();
            while (!input.isAtEnd()) {
                int i = input.readTag();
                int fieldId = i >>> 3;
//                int fieldType = i & 7;
                switch (fieldId) {
                    case 1:
                        ret.setType(DataObject.Type.byOrdinal(input.readInt32()));
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
                        throw new RuntimeException("unsupported field nr: " + i);
                }
            }
            return ret;
        }
        else {
            throw new RuntimeException("not implemented class: " + type);
        }
    }
}
