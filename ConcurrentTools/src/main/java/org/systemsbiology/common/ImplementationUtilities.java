package org.systemsbiology.common;

import com.lordjoe.lib.xml.*;
import com.lordjoe.utilities.*;

import java.awt.Dimension;

/**
 * org.systemsbiology.common.ImplementationUtilities
 * written by Steve Lewis
 * on Apr 23, 2010
 */
 /**
 * com.lordjoe.general.ImplementationUtilities
 *
 * @author Steve Lewis
 * @date Jun 2, 2006
 */
public abstract class ImplementationUtilities
{
    public final static ImplementationUtilities[] EMPTY_ARRAY = {};
    public final static Class THIS_CLASS = ImplementationUtilities.class;

    private static IStringConverter[] gConverters = {
            new IntegerConverter(),
            new LongConverter(),
            new DoubleConverter(),
            new CharacterConverter(),
            new SqlDateConverter(),
            new DateConverter(),
            new StringConverter(),
            new BooleanConverter(),
            new DimensionConverter(),
            new PointConverter()

      };

    public static IStringConverter[] getConverters()
    {
        return gConverters;
    }

    public static void addConverter(IStringConverter added)
    {
        synchronized (gConverters) {
            IStringConverter[] converters = getConverters();
            for (int i = 0; i < converters.length; i++) {
                IStringConverter converter = converters[i];
                if (added.equals(converter))
                    return;
            }
            gConverters =  Util.addToArray(gConverters, added);
        }
    }


    public static String convertToString(Object item)
    {
        if (item == null)
            return null;
        return buildValueString(item);
//        Class itemClass = item.getClass();
//        if (itemClass.isArray()) {
//            if (itemClass.getComponentType() == Integer.TYPE) {
//                int[] realItem = (int[]) item;
//                StringBuffer sb = new StringBuffer();
//                for (int i = 0; i < realItem.length; i++) {
//                    int i1 = realItem[i];
//                    if (sb.length() > 0) {
//                        sb.append(",");
//                    }
//                    sb.append(i1);
//                }
//                return sb.toString();
//            }
//        }
//        return item.toString();
    }


    public static String buildValueString(Object value)
    {
        if (value == null)
            return "null";

        Class cls = value.getClass();
        if (cls.isArray()) {
            if (value instanceof int[]) {
                return buildValueStringFromArray((int[]) value);
            }
            else {
                return buildValueStringFromArray((Object[]) value);
            }
        }
        IStringConverter[] converters = getConverters();
        for (int i = 0; i < converters.length; i++) {
            IStringConverter converter = converters[i];
            if (converter.isApplicable(cls)) {
                String valueStr = converter.convertToString(value);
                if (valueStr.indexOf(".") > -1 && !(value instanceof Number))
                    CommonUtilities.breakHere();
                return valueStr;
            }
        }
//        if (value instanceof Long) {
//            Long realValue = (Long) value;
//            if (realValue.longValue() == Long.MIN_VALUE)
//                value = "null";
//        }
//        if (value instanceof Integer) {
//            Integer realValue = (Integer) value;
//            if (realValue.intValue() == Integer.MIN_VALUE)
//                value = "null";
//        }
//        if (value instanceof Double) {
//            Double realValue = (Double) value;
//            if (realValue.doubleValue() == Double.NEGATIVE_INFINITY)
//                value = "null";
//        }
//        if (value instanceof Character) {
//            Character realValue = (Character) value;
//            if (realValue.charValue() == 0)
//                value = "null";
//        }
        if (value != null) {
            if (value.getClass().getName().indexOf("Parameter") > -1) {
                String val = value.toString();
                val = XMLUtil.makeXMLString(val);
                return val;
            }
            String val = value.toString();
            val = XMLUtil.makeXMLString(val);
            return val;
        }
        else {
            return "";
        }
    }

    protected static String buildValueStringFromArray(int[] value)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            Integer o = value[i];
            String s = ImplementationUtilities.buildValueString(o);
            if (sb.length() > 0)
                sb.append(",");
            sb.append(s);
        }
        return sb.toString();
    }

    protected static String buildValueStringFromArray(Object[] value)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            Object o = value[i];
            String s = ImplementationUtilities.buildValueString(o);
            if (sb.length() > 0)
                sb.append(",");
            sb.append(s);
        }
        return sb.toString();
    }

    public static class IntegerConverter implements IStringConverter<Integer>
    {
        public String convertToString(Integer obj)
        {
            if (obj == null)
                return "null";
            return obj.toString();
        }

        public Integer convertFromString(String str)
        {
            if (str == null || "null".equals(str))
                return null;
            else
                return new Integer(str);
        }

        public boolean isApplicable(Class test)
        {
            return Integer.class.isAssignableFrom(test);
        }
    }

    public static class LongConverter implements IStringConverter<Long>
    {
        public String convertToString(Long obj)
        {
            if (obj == null)
                return "null";
            return obj.toString();
        }

        public Long convertFromString(String str)
        {
            if (str == null || "null".equals(str))
                return null;
            else
                return new Long(str);
        }

        public boolean isApplicable(Class test)
        {
            return Long.class.isAssignableFrom(test);
        }
    }

    public static class DoubleConverter implements IStringConverter<Double>
    {
        public String convertToString(Double obj)
        {
            if (obj == null)
                return "null";
            return obj.toString();
        }

        public Double convertFromString(String str)
        {
            if (str == null || "null".equals(str))
                return null;
            else
                return new Double(str);
        }

        public boolean isApplicable(Class test)
        {
            return Double.class.isAssignableFrom(test);
        }
    }


    public static class StringConverter implements IStringConverter<String>
    {
        public String convertToString(String obj)
        {
            if (obj == null)
                return "null";
            return XMLUtil.makeXMLString(obj);
        }

        public String convertFromString(String str)
        {
            if (str == null || "null".equals(str))
                return null;
            else
                return XMLUtil.fromXMLString(str);
        }

        public boolean isApplicable(Class test)
        {
            return String.class.isAssignableFrom(test);
        }
    }

    /**
     * converter for java.sql.Date
     *
     * @author Scott
     */
    public static class SqlDateConverter implements IStringConverter<java.sql.Date>
    {
        public String convertToString(java.sql.Date obj)
        {
            if (obj == null)
                return "null";
            return Util.formatDate(obj);
        }

        public java.sql.Date convertFromString(String str)
        {
            if (str == null || "null".equals(str))
                return null;
            else
                return new java.sql.Date(
                        Util.parseDate(str, Util.DEFAULT_DATE_TIME_FORMAT).getTime());
        }

        public boolean isApplicable(Class test)
        {
            return java.sql.Date.class.isAssignableFrom(test);
        }
    }

    /**
     * converter for java.util.Date
     *
     * @author Scott
     */
    public static class DateConverter implements IStringConverter<java.util.Date>
    {
        public String convertToString(java.util.Date obj)
        {
            if (obj == null)
                return "null";
            return Util.formatDate(obj);
        }

        public java.util.Date convertFromString(String str)
        {
            if (str == null || "null".equals(str))
                return null;
            else
                return Util.parseDate(str, Util.DEFAULT_DATE_TIME_FORMAT);
        }

        public boolean isApplicable(Class test)
        {
            return java.util.Date.class.isAssignableFrom(test);
        }
    }

    /**
     * converter for java.awt.Dimension
     *
     * @author Scott
     */
    public static class DimensionConverter implements IStringConverter<Dimension>
    {
        public String convertToString(Dimension obj)
        {
            if (obj == null)
                return "null";
            return Integer.toString(obj.width) + "," + Integer.toString(obj.height);
        }

        public Dimension convertFromString(String str)
        {
            if (str == null || "null".equals(str))
                return null;
            String[] items = str.split(",");
            return new Dimension(Integer.parseInt(items[0]),
                    Integer.parseInt(items[1])
            );

        }

        public boolean isApplicable(Class test)
        {
            return Dimension.class.isAssignableFrom(test);
        }
    }


    /**
     * converter for java.awt.Point
     *
     * @author Scott
     */
    public static class PointConverter implements IStringConverter<java.awt.Point>
    {
        public String convertToString(java.awt.Point obj)
        {
            if (obj == null)
                return "null";
            return Integer.toString(obj.x) + "," + Integer.toString(obj.y);
        }

        public java.awt.Point convertFromString(String str)
        {
            if (str == null || "null".equals(str))
                return null;
            String[] items = str.split(",");
            return new java.awt.Point(Integer.parseInt(items[0]),
                    Integer.parseInt(items[1])
            );

        }

        public boolean isApplicable(Class test)
        {
            return Point.class.isAssignableFrom(test);
        }
    }


    public static class CharacterConverter implements IStringConverter<Character>
    {
        public String convertToString(Character obj)
        {
            if (obj == null)
                return "null";
            return obj.toString();
        }

        public Character convertFromString(String str)
        {
            if (str == null || "null".equals(str) || str.length() == 0)
                return null;
            else
                return new Character(str.charAt(0));
        }

        public boolean isApplicable(Class test)
        {
            return Character.class.isAssignableFrom(test);
        }
    }

    public static class BooleanConverter implements IStringConverter<Boolean>
    {
        public String convertToString(Boolean obj)
        {
            if (obj == null)
                return "null";
            return obj.toString();
        }

        public Boolean convertFromString(String str)
        {
            if (str == null || "null".equals(str) || str.length() == 0)
                return null;
            else
                return "true".equals(str) ? Boolean.TRUE : Boolean.FALSE;
        }

        public boolean isApplicable(Class test)
        {
            return Boolean.class.isAssignableFrom(test);
        }
    }


}
