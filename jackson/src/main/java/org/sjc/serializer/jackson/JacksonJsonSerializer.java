package org.sjc.serializer.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.sjc.serializer.api.SerializeService;

public class JacksonJsonSerializer implements SerializeService {

    private ObjectMapper mapper;

    public JacksonJsonSerializer() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //  mapper.setConfig()
        // mapper.setDateFormat();
        // encoding? UTF-8 seems default.
        // etc...
    }

    @Override
    public byte[] serialize(Object obj) throws Exception {
        ObjectWriter writer = mapper.writerFor(obj.getClass());
        return writer.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws Exception {
        final ObjectReader r = mapper.readerFor(type);
        return r.readValue(bytes);
    }

}
