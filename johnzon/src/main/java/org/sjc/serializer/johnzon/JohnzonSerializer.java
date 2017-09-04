package org.sjc.serializer.johnzon;

import org.apache.johnzon.mapper.Mapper;
import org.apache.johnzon.mapper.MapperBuilder;
import org.sjc.serializer.api.SerializeService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class JohnzonSerializer implements SerializeService {

    private Mapper mapper;

    public JohnzonSerializer() {

        // "each round took 8594ns"
        mapper =  new MapperBuilder().setEncoding("UTF-8").setTreatByteArrayAsBase64(true).build();

        // SLOW! "each round took 45703ns"
        // mapper =  new MapperBuilder().setEncoding("UTF-8").build();

    }

    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mapper.writeObject(obj, baos);
        return baos.toByteArray();
        // return mapper.writeObjectAsString(obj).getBytes("UTF-8");  // slow
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return mapper.readObject(bais, type);
        // return mapper.readObject(new String(bytes, "UTF-8"), type); // slow
    }
}
