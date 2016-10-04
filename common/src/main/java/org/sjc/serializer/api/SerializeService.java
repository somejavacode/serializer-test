package org.sjc.serializer.api;

import java.io.InputStream;
import java.io.OutputStream;

public interface SerializeService { // SerializeService<T> how to? not at runtime.

    byte[] serialize(Object obj) throws Exception;

    void serialize(Object obj, OutputStream os) throws Exception;

    Object deserialize(byte[] bytes, Class type) throws Exception;

    Object deserialize(InputStream is, Class type) throws Exception;

}
