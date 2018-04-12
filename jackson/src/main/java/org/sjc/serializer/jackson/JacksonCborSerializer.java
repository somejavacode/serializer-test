package org.sjc.serializer.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import org.sjc.serializer.api.SerializeService;

/**
 * https://tools.ietf.org/html/rfc7049 Concise Binary Object Representation (CBOR), 2013-10
 */
public class JacksonCborSerializer implements SerializeService {

    private ObjectMapper mapper;

    public JacksonCborSerializer() {
        // as easy as that...
        mapper = new ObjectMapper(new CBORFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
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
