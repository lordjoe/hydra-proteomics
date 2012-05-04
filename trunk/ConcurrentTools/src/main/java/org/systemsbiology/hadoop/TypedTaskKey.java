package org.systemsbiology.hadoop;

import org.apache.hadoop.io.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.TypedTaskKey
 * Generic key sorted first by type then by other data
 * User: steven
 * Date: May 18, 2010
 */
public abstract class TypedTaskKey<T extends WritableComparable<T>>  implements WritableComparable<TypedTaskKey> {

    public static final TypedTaskKey[] EMPTY_ARRAY = {};

    private Text m_Type  = new Text();;
    private T m_Value;

    public Text getType() {
        return m_Type;
    }

    public void setType(final Text pType) {
        m_Type = pType;
    }

    public T getValue() {
        return m_Value;
    }

    public void setValue(final T pValue) {
        m_Value = pValue;
    }

     public int compareTo(final TypedTaskKey<T> o) {
        int ret = getType().compareTo(o.getType());
        if(ret != 0)
            return ret;
        final Comparable<T> value = getValue();
        return  value.compareTo( o.getValue());
    }

    @Override
    public void write(final DataOutput out) throws IOException {
        getType().write( out );
        getValue().write(out);

    }

    @Override
    public void readFields(final DataInput in) throws IOException {
        getType().readFields( in );
        getValue().readFields( in );
    
    }
}
