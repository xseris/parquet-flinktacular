/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package flink.parquet.avro;

import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class PhoneNumberStr extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -1630876555994974182L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PhoneNumberStr\",\"namespace\":\"flink.parquet.avro\",\"fields\":[{\"name\":\"number\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.CharSequence number;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public PhoneNumberStr() {}

  /**
   * All-args constructor.
   * @param number The new value for number
   */
  public PhoneNumberStr(java.lang.CharSequence number) {
    this.number = number;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return number;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: number = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'number' field.
   * @return The value of the 'number' field.
   */
  public java.lang.CharSequence getNumber() {
    return number;
  }

  /**
   * Sets the value of the 'number' field.
   * @param value the value to set.
   */
  public void setNumber(java.lang.CharSequence value) {
    this.number = value;
  }

  /**
   * Creates a new PhoneNumberStr RecordBuilder.
   * @return A new PhoneNumberStr RecordBuilder
   */
  public static flink.parquet.avro.PhoneNumberStr.Builder newBuilder() {
    return new flink.parquet.avro.PhoneNumberStr.Builder();
  }

  /**
   * Creates a new PhoneNumberStr RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new PhoneNumberStr RecordBuilder
   */
  public static flink.parquet.avro.PhoneNumberStr.Builder newBuilder(flink.parquet.avro.PhoneNumberStr.Builder other) {
    return new flink.parquet.avro.PhoneNumberStr.Builder(other);
  }

  /**
   * Creates a new PhoneNumberStr RecordBuilder by copying an existing PhoneNumberStr instance.
   * @param other The existing instance to copy.
   * @return A new PhoneNumberStr RecordBuilder
   */
  public static flink.parquet.avro.PhoneNumberStr.Builder newBuilder(flink.parquet.avro.PhoneNumberStr other) {
    return new flink.parquet.avro.PhoneNumberStr.Builder(other);
  }

  /**
   * RecordBuilder for PhoneNumberStr instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<PhoneNumberStr>
    implements org.apache.avro.data.RecordBuilder<PhoneNumberStr> {

    private java.lang.CharSequence number;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(flink.parquet.avro.PhoneNumberStr.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.number)) {
        this.number = data().deepCopy(fields()[0].schema(), other.number);
        fieldSetFlags()[0] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing PhoneNumberStr instance
     * @param other The existing instance to copy.
     */
    private Builder(flink.parquet.avro.PhoneNumberStr other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.number)) {
        this.number = data().deepCopy(fields()[0].schema(), other.number);
        fieldSetFlags()[0] = true;
      }
    }

    /**
      * Gets the value of the 'number' field.
      * @return The value.
      */
    public java.lang.CharSequence getNumber() {
      return number;
    }

    /**
      * Sets the value of the 'number' field.
      * @param value The value of 'number'.
      * @return This builder.
      */
    public flink.parquet.avro.PhoneNumberStr.Builder setNumber(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.number = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'number' field has been set.
      * @return True if the 'number' field has been set, false otherwise.
      */
    public boolean hasNumber() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'number' field.
      * @return This builder.
      */
    public flink.parquet.avro.PhoneNumberStr.Builder clearNumber() {
      number = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    public PhoneNumberStr build() {
      try {
        PhoneNumberStr record = new PhoneNumberStr();
        record.number = fieldSetFlags()[0] ? this.number : (java.lang.CharSequence) defaultValue(fields()[0]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  private static final org.apache.avro.io.DatumWriter
    WRITER$ = new org.apache.avro.specific.SpecificDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  private static final org.apache.avro.io.DatumReader
    READER$ = new org.apache.avro.specific.SpecificDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
