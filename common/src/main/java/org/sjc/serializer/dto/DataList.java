package org.sjc.serializer.dto;

import java.util.ArrayList;
import java.util.List;

public class DataList {

    private List<DataObject> objects;

    public List<DataObject> getObjects() {
        return objects;
    }

    public void setObjects(List<DataObject> objects) {
        this.objects = objects;
    }

    public DataList() {
        this.objects = new ArrayList<DataObject>();
    }

    public void addObject(DataObject object) {
        this.objects.add(object);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataList dataList = (DataList) o;

        return objects != null ? objects.equals(dataList.objects) : dataList.objects == null;
    }

    @Override
    public int hashCode() {
        return objects != null ? objects.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DataList{" +
                "objects=" + objects +
                '}';
    }
}
