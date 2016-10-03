package org.sjc.serializer.jackson;

import org.sjc.serializer.api.SerializeService;

import java.io.InputStream;
import java.io.OutputStream;

public class JacksonSmileSerializer implements SerializeService {
    @Override
    public byte[] serialize(Object obj) throws Exception {
        return new byte[0];
    }

    @Override
    public void serialize(Object obj, OutputStream os) throws Exception {

    }

    @Override
    public Object deserialize(byte[] bytes) throws Exception {
        return null;
    }

    @Override
    public Object deserialize(InputStream is) throws Exception {
        return null;
    }
}
