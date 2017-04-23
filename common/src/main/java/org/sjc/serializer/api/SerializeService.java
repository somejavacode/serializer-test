package org.sjc.serializer.api;

public interface SerializeService {

    byte[] serialize(Object obj) throws Exception;

    <T> T deserialize(byte[] bytes, Class<T> type) throws Exception;

}
