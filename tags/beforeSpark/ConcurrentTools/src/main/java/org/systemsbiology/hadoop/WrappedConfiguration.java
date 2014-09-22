package org.systemsbiology.hadoop;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import java.io.*;


/**
 * org.systemsbiology.hadoop.WrappedConfiguration
 * Wrapper around a Hadoop configuration
 * User: steven
 * Date: Aug 12, 2010
 */
public class WrappedConfiguration implements IConfiguration{
    public static final WrappedConfiguration[] EMPTY_ARRAY = {};

    private final Configuration m_Configuration;
    private final TaskInputOutputContext m_Context;

    public WrappedConfiguration(final TaskInputOutputContext pConfiguration) {
        m_Context = pConfiguration;
        m_Configuration = pConfiguration.getConfiguration();
    }

    public WrappedConfiguration(final Configuration pConfiguration) {
        m_Context = null;
        m_Configuration = pConfiguration;
    }

    /**
     * Get the value of the <code>name</code> property, <code>null</code> if
     * no such property exists.
     * <p/>
     * Values are processed for <a href="#VariableExpansion">variable expansion</a>
     * before being returned.
     *
     * @param name the property name.
     * @return the value of the <code>name</code> property,
     *         or null if no such property exists.
     */
    @Override
    public String get(final String name) {
        return m_Configuration.get(name);
    }

    /**
     * Set the <code>value</code> of the <code>name</code> property.
     *
     * @param name  property name.
     * @param value property value.
     */
    @Override
    public void set(final String name, final String value) {
         m_Configuration.set(name,value);
    }

    /**
     * Get the value of the <code>name</code> property. If no such property
     * exists, then <code>defaultValue</code> is returned.
     *
     * @param name         property name.
     * @param defaultValue default value.
     * @return property value, or <code>defaultValue</code> if the property
     *         doesn't exist.
     */
    @Override
    public String get(final String name, final String defaultValue) {
        return m_Configuration.get(name,defaultValue);
    }

    /**
     * Get the value of the <code>name</code> property as an <code>int</code>.
     * <p/>
     * If no such property exists, or if the specified value is not a valid
     * <code>int</code>, then <code>defaultValue</code> is returned.
     *
     * @param name         property name.
     * @param defaultValue default value.
     * @return property value as an <code>int</code>,
     *         or <code>defaultValue</code>.
     */
    @Override
    public int getInt(final String name, final int defaultValue) {
        return m_Configuration.getInt(name,defaultValue);
    }

    /**
     * Set the value of the <code>name</code> property to an <code>int</code>.
     *
     * @param name  property name.
     * @param value <code>int</code> value of the property.
     */
    @Override
    public void setInt(final String name, final int value) {
            m_Configuration.setInt(name,value);
    }

    /**
     * Get the value of the <code>name</code> property as a <code>long</code>.
     * If no such property is specified, or if the specified value is not a valid
     * <code>long</code>, then <code>defaultValue</code> is returned.
     *
     * @param name         property name.
     * @param defaultValue default value.
     * @return property value as a <code>long</code>,
     *         or <code>defaultValue</code>.
     */
    @Override
    public long getLong(final String name, final long defaultValue) {
        return m_Configuration.getLong(name,defaultValue);
    }

    /**
     * Set the value of the <code>name</code> property to a <code>long</code>.
     *
     * @param name  property name.
     * @param value <code>long</code> value of the property.
     */
    @Override
    public void setLong(final String name, final long value) {
        m_Configuration.setLong(name,value);

    }

    /**
     * Get the value of the <code>name</code> property as a <code>float</code>.
     * If no such property is specified, or if the specified value is not a valid
     * <code>float</code>, then <code>defaultValue</code> is returned.
     *
     * @param name         property name.
     * @param defaultValue default value.
     * @return property value as a <code>float</code>,
     *         or <code>defaultValue</code>.
     */
    @Override
    public float getFloat(final String name, final float defaultValue) {
        return m_Configuration.getFloat(name,defaultValue);
     }

    /**
     * Set the value of the <code>name</code> property to a <code>float</code>.
     *
     * @param name  property name.
     * @param value property value.
     */
    @Override
    public void setFloat(final String name, final float value) {
        m_Configuration.setFloat(name,value);

    }

    /**
     * Get the value of the <code>name</code> property as a <code>boolean</code>.
     * If no such property is specified, or if the specified value is not a valid
     * <code>boolean</code>, then <code>defaultValue</code> is returned.
     *
     * @param name         property name.
     * @param defaultValue default value.
     * @return property value as a <code>boolean</code>,
     *         or <code>defaultValue</code>.
     */
    @Override
    public boolean getBoolean(final String name, final boolean defaultValue) {
        return m_Configuration.getBoolean(name,defaultValue);
    }

    /**
     * Set the value of the <code>name</code> property to a <code>boolean</code>.
     *
     * @param name  property name.
     * @param value <code>boolean</code> value of the property.
     */
    @Override
    public void setBoolean(final String name, final boolean value) {
        m_Configuration.setBoolean(name,value);

    }

    /**
     * Get the comma delimited values of the <code>name</code> property as
     * an array of <code>String</code>s.
     * If no such property is specified then <code>null</code> is returned.
     *
     * @param name property name.
     * @return property value as an array of <code>String</code>s,
     *         or <code>null</code>.
     */
    @Override
    public String[] getStrings(final String name) {
        return m_Configuration.getStrings(name);
    }

    /**
     * Get the comma delimited values of the <code>name</code> property as
     * an array of <code>String</code>s.
     * If no such property is specified then default value is returned.
     *
     * @param name         property name.
     * @param defaultValue The default value
     * @return property value as an array of <code>String</code>s,
     *         or default value.
     */
    @Override
    public String[] getStrings(final String name, final String... defaultValue) {
        return m_Configuration.getStrings(name,defaultValue);
    }

    /**
     * Set the array of string values for the <code>name</code> property as
     * as comma delimited values.
     *
     * @param name   property name.
     * @param values The values
     */
    @Override
    public void setStrings(final String name, final String... values) {
        m_Configuration.setStrings(name,values);

    }

    @Override
    public void write(final WritableComparable key, final Writable value) {
        try {
            m_Context.write(key,value);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);

        }
    }
}
