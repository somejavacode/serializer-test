package org.sjc.serializer.api;

import java.io.InputStream;
import java.io.OutputStream;

public interface SerializeService {

    byte[] serialize(Object obj) throws Exception;

    <T> T deserialize(byte[] bytes, Class<T> type) throws Exception;

}
