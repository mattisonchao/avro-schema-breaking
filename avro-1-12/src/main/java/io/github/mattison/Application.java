package io.github.mattison;


import org.apache.avro.Schema;
import org.apache.avro.protobuf.ProtobufData;

public class Application {


    public static void main(String[] args) {
        final Schema schema = ProtobufData.get().getSchema(DataRecordOuterClass.DataRecord.class);
        final Schema.Parser parser = new Schema.Parser();
        parser.parse(schema.toString());
        System.out.println(parser);
    }
}
