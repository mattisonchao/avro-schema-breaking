# Avro Protobuf Schema Generation Breaking Change

This project demonstrates a breaking change in Avro's schema generation from Protobuf messages between `avro-protobuf:1.11.5` and `avro-protobuf:1.12.1`.

## Project Structure

The project is a multi-project Gradle build with the following structure:

- `pojo`: A Java library project that uses the `com.google.protobuf` plugin to generate Java classes from a `.proto` file (`src/main/proto/data_record.proto`).
- `avro-1-11`: A Java application that depends on the `pojo` project and `org.apache.avro:avro-protobuf:1.11.5`. It generates an Avro schema from the Protobuf-generated classes.
- `avro-1-12`: A Java application, identical to `avro-1-11`, but it depends on `org.apache.avro:avro-protobuf:1.12.1`.

## The Breaking Change

When running the applications, the `avro-1-11` module executes successfully, while the `avro-1-12` module fails with a `SchemaParseException`.

This is because in version 1.12.0, a validation was added to the Avro schema parser that disallows the `$` character in namespace parts. The `ProtobufData.get().getSchema()` method generates a schema that includes a `$` in the namespace for nested records, which causes the parsing to fail in version 1.12.1.

## How to Run

You can see the different outcomes by running the `run` task for each application.

1.  **Run the `avro-1-11` application (succeeds):**

    ```bash
    ./gradlew :avro-1-11:run
    ```

2.  **Run the `avro-1-12` application (fails):**

    ```bash
    ./gradlew :avro-1-12:run
    ```

## Output

### `avro-1-11` Output

The `avro-1-11` application successfull prints the parser object.

```
org.apache.avro.Schema$Parser@...
```

### `avro-1-12` Error

The `avro-1-12` application fails with the following error:

```
Exception in thread "main" org.apache.avro.SchemaParseException: Namespace part "DataRecord$DataRecord" is invalid: Illegal character in: DataRecord$DataRecord
        at org.apache.avro.ParseContext.validateName(ParseContext.java:241)
        at org.apache.avro.ParseContext.requireValidFullName(ParseContext.java:232)
        at org.apache.avro.ParseContext.put(ParseContext.java:213)
        at org.apache.avro.Schema.parseRecord(Schema.java:1882)
        at org.apache.avro.Schema.parse(Schema.java:1836)
        at org.apache.avro.Schema.parseUnion(Schema.java:1972)
        at org.apache.avro.Schema.parse(Schema.java:1849)
        at org.apache.avro.Schema.parseField(Schema.java:1892)
        at org.apache.avro.Schema.parseRecord(Schema.java:1872)
        at org.apache.avro.Schema.parse(Schema.java:1836)
        at org.apache.avro.Schema$Parser.parse(Schema.java:1539)
        at org.apache.avro.Schema$Parser.parse(Schema.java:1516)
        at io.github.mattison.Application.main(Application.java:13)
```

This demonstrates the breaking change introduced in Avro 1.12.