/*
 * Copyright 2016 openKex. All rights reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.sjc.serializer.dto;

import java.util.List;

public class DataList {

    private List<DataObject> objects;

    public List<DataObject> getObjects() {
        return objects;
    }

    public void setObjects(List<DataObject> objects) {
        this.objects = objects;
    }
}
