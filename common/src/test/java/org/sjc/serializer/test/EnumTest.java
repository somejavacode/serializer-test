/*
 * Copyright 2016 openKex. All rights reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.sjc.serializer.test;

import org.junit.Assert;
import org.junit.Test;
import org.sjc.serializer.dto.DataObject;

import java.util.HashSet;

public class EnumTest {

    @Test
    public void dataObjectTypeTest() throws Exception {
        // test that enum values are unique
        HashSet<Integer> ids = new HashSet<Integer>();
        DataObject.Type[] types = DataObject.Type.values();
        for (DataObject.Type type : types) {
            ids.add(type.getId());
        }
        // test if ids are unique
        Assert.assertEquals("Type ids are not unique.", types.length, ids.size());
    }

}
