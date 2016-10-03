/*
 * Copyright 2016 openKex. All rights reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.sjc.serializer.api;

import java.io.InputStream;
import java.io.OutputStream;

public interface SerializeService { // SerializeService<T> how to? not at runtime.

    byte[] serialize(Object obj) throws Exception;

    void serialize(Object obj, OutputStream os) throws Exception;

    Object deserialize(byte[] bytes) throws Exception;

    Object deserialize(InputStream is) throws Exception;

}
