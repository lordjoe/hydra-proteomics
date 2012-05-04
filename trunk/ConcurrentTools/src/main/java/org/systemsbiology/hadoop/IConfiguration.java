package org.systemsbiology.hadoop;

import org.apache.hadoop.io.*;

/**
 * org.systemsbiology.hadoop.IConfiguration
 * User: steven
 * Date: Aug 12, 2010
 */
public interface IConfiguration {
    public static final IConfiguration[] EMPTY_ARRAY = {};

    /**
     * Get the value of the <code>name</code> property, <code>null</code> if
     * no such property exists.
     *
     * Values are processed for <a href="#VariableExpansion">variable expansion</a>
     * before being returned.
     *
     * @param name the property name.
     * @return the value of the <code>name</code> property,
     *         or null if no such property exists.
     */
    public String get(String name);

    /**
     * Set the <code>value</code> of the <code>name</code> property.
     *
     * @param name property name.
     * @param value property value.
     */
   public  void set(String name, String value);

    /**
     * Get the value of the <code>name</code> property. If no such property
     * exists, then <code>defaultValue</code> is returned.
     *
     * @param name property name.
     * @param defaultValue default value.
     * @return property value, or <code>defaultValue</code> if the property
     *         doesn't exist.
     */
    public String get(String name, String defaultValue);

    /**
     * Get the value of the <code>name</code> property as an <code>int</code>.
     *
     * If no such property exists, or if the specified value is not a valid
     * <code>int</code>, then <code>defaultValue</code> is returned.
     *
     * @param name property name.
     * @param defaultValue default value.
     * @return property value as an <code>int</code>,
     *         or <code>defaultValue</code>.
     */
    int getInt(String name, int defaultValue);

    /**
     * Set the value of the <code>name</code> property to an <code>int</code>.
     *
     * @param name property name.
     * @param value <code>int</code> value of the property.
     */
    public void setInt(String name, int value);

    /**
     * Get the value of the <code>name</code> property as a <code>long</code>.
     * If no such property is specified, or if the specified value is not a valid
     * <code>long</code>, then <code>defaultValue</code> is returned.
     *
     * @param name property name.
     * @param defaultValue default value.
     * @return property value as a <code>long</code>,
     *         or <code>defaultValue</code>.
     */
    public long getLong(String name, long defaultValue);

    /**
     * Set the value of the <code>name</code> property to a <code>long</code>.
     *
     * @param name property name.
     * @param value <code>long</code> value of the property.
     */
    public void setLong(String name, long value);

    /**
     * Get the value of the <code>name</code> property as a <code>float</code>.
     * If no such property is specified, or if the specified value is not a valid
     * <code>float</code>, then <code>defaultValue</code> is returned.
     *
     * @param name property name.
     * @param defaultValue default value.
     * @return property value as a <code>float</code>,
     *         or <code>defaultValue</code>.
     */
    float getFloat(String name, float defaultValue);

    /**
     * Set the value of the <code>name</code> property to a <code>float</code>.
     *
     * @param name property name.
     * @param value property value.
     */
    public void setFloat(String name, float value);

    /**
     * Get the value of the <code>name</code> property as a <code>boolean</code>.
     * If no such property is specified, or if the specified value is not a valid
     * <code>boolean</code>, then <code>defaultValue</code> is returned.
     *
     * @param name property name.
     * @param defaultValue default value.
     * @return property value as a <code>boolean</code>,
     *         or <code>defaultValue</code>.
     */
   public  boolean getBoolean(String name, boolean defaultValue);

    /**
     * Set the value of the <code>name</code> property to a <code>boolean</code>.
     *
     * @param name property name.
     * @param value <code>boolean</code> value of the property.
     */
    public void setBoolean(String name, boolean value);

    /**
     * Get the comma delimited values of the <code>name</code> property as
     * an array of <code>String</code>s.
     * If no such property is specified then <code>null</code> is returned.
     *
     * @param name property name.
     * @return property value as an array of <code>String</code>s,
     *         or <code>null</code>.
     */
    public String[] getStrings(String name);

    /**
     * Get the comma delimited values of the <code>name</code> property as
     * an array of <code>String</code>s.
     * If no such property is specified then default value is returned.
     *
     * @param name property name.
     * @param defaultValue The default value
     * @return property value as an array of <code>String</code>s,
     *         or default value.
     */
    public String[] getStrings(String name, String... defaultValue);

    /**
     * Set the array of string values for the <code>name</code> property as
     * as comma delimited values.
     *
     * @param name property name.
     * @param values The values
     */
    public void setStrings(String name, String... values);

     public void write(WritableComparable key, Writable value);
}
