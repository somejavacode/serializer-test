package org.sjc.serializer.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.ObjectDeserializer;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;
import org.sjc.serializer.api.SerializeService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HessianSerializer implements SerializeService {

    private SerializerFactory factory;

    public HessianSerializer() {
        this.factory = SerializerFactory.createDefault();
        this.factory.setAllowNonSerializable(true);
    }

    @Override
    public byte[] serialize(Object obj) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HessianOutput output = new HessianOutput(baos);
        // without this objectListTest fails with:
        // Serialized class org.sjc.serializer.dto.DataObject must implement java.io.Serializable
        output.setSerializerFactory(factory);

        //JavaSerializer serializer = new JavaSerializer(obj.getClass());
        Serializer serializer = factory.getSerializer(obj.getClass());  // UnsafeSerializer?
//        Serializer serializer = factory.getObjectSerializer(obj.getClass());
//        Serializer serializer = new Serializer(obj.getClass());
        serializer.writeObject(obj, output);
        return baos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        HessianInput input = new HessianInput(bais);
//        input.setSerializerFactory(factory);

        // this expects a list of parameters?
        // JavaDeserializer deserializer = new JavaDeserializer(type);
        // deserializer.readObject(input, new String[]{"string1", "int1"});

        // this function does not return an ObjectDeserializer, it returns an UnsafeDeserializer
        // and fails with "com.caucho.hessian.io.HessianProtocolException: expected map/object at ..." (AbstractMapDeserializer:70)
//        Deserializer deserializer = factory.getObjectDeserializer(type.getName());

        // ObjectDeserializer does the right thing.
        ObjectDeserializer deserializer = new ObjectDeserializer(type);
        return (T) deserializer.readObject(input);
    }
}
