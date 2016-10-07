package org.sjc.serializer.jackson;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;
import com.fasterxml.jackson.dataformat.smile.SmileParser;
import org.sjc.serializer.api.SerializeService;
import org.sjc.serializer.dto.DataObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class JacksonSmileSerializer implements SerializeService {
    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serialize(obj, out);
        return out.toByteArray();
    }

    @Override
    public void serialize(Object obj, OutputStream os) throws Exception {
        SmileFactory f = new SmileFactory();
        // need to disable 7 bit "feature": result size for 300 bytes: 353 -> 310 bytes
        f.configure(SmileGenerator.Feature.ENCODE_BINARY_AS_7BIT, false);

        SmileGenerator gen = f.createGenerator(os);
        if (obj instanceof DataObject) {
            DataObject data = (DataObject) obj;
            if (data.getType() == null) {
                gen.writeNull();
            }
            else {
                gen.writeNumber(data.getType().getId());
            }
            gen.writeNumber(data.getLongValue());
            if (data.getStringValue() == null) {
                gen.writeNull();
            }
            else {
                gen.writeString(data.getStringValue());
            }
            if (data.getByteArray() == null) {
                gen.writeNull();
            }
            else {
                gen.writeBinary(data.getByteArray(), 0, data.getByteArray().length);
            }
            gen.flush();
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
        SmileFactory f = new SmileFactory();
        SmileParser parser = f.createParser(is);
        if (type == DataObject.class) {
            DataObject ret = new DataObject();
            if (!parser.nextToken().equals(JsonToken.VALUE_NULL)) {
                ret.setType(DataObject.Type.getById(parser.getIntValue()));
            }
            // could validate type "a little bit" (e.g. "numeric")
            parser.nextToken();
            ret.setLongValue(parser.getLongValue());
            if (!parser.nextToken().equals(JsonToken.VALUE_NULL)) {
                ret.setStringValue(parser.getText());
            }
            if (!parser.nextToken().equals(JsonToken.VALUE_NULL)) {
                ret.setByteArray(parser.getBinaryValue());
            }
            return ret;
        }
        else {
            throw new RuntimeException("not implemented class: " + type);
        }
    }
}
