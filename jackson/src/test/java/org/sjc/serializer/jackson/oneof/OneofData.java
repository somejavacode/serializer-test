package org.sjc.serializer.jackson.oneof;

public class OneofData {

    private ObjectA objA;
    private ObjectB objB;

    public OneofData() {
    }

    public ObjectA getObjA() {
        return objA;
    }

    public void setObjA(ObjectA objA) {
        this.objA = objA;
    }

    public ObjectB getObjB() {
        return objB;
    }

    public void setObjB(ObjectB objB) {
        this.objB = objB;
    }

    @Override
    public String toString() {
        return "OneofData{" +
                "objA=" + objA +
                ", objB=" + objB +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OneofData oneofData = (OneofData) o;

        if (objA != null ? !objA.equals(oneofData.objA) : oneofData.objA != null) {
            return false;
        }
        return objB != null ? objB.equals(oneofData.objB) : oneofData.objB == null;
    }

    @Override
    public int hashCode() {
        int result = objA != null ? objA.hashCode() : 0;
        result = 31 * result + (objB != null ? objB.hashCode() : 0);
        return result;
    }
}
