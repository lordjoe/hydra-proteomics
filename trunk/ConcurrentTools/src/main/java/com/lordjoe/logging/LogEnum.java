package com.lordjoe.logging;

import com.lordjoe.utilities.*;

/**
 * com.lordjoe.logging.LogTypeEnum
 *
 * @author Steve Lewis
 * @date Jun 5, 2008
 */
public class LogEnum extends NamedPseudoEnum<LogEnum>
{
    public static LogEnum[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = LogEnum.class;
    protected static final LogEnum EXEMPLAR = new LogEnum(EXAMPLAR_NAME);

    public static LogEnum getInstance(String s)
    {
        LogEnum ret = null;
        try {
            ret = EXEMPLAR.getValueOf(s);
        }
        catch (Exception e) {
            ret =  EXEMPLAR.createInstance(s);
          }
        return ret;
    }

    private LogEnum(String name)
    {
        super(name);
        if (isExemplar())
            addName(EXEMPLAR, name, this);
    }

    @Override
    public LogEnum getExemplar()
    {
        return EXEMPLAR;
    }

    public LogEnum[] makeBlankNativeArray(int size)
    {
        return new LogEnum[size];
    }

    /**
     * A protected constructor for a new value.
     * Check {@link WCSPseudoEnum#populateValues(String)} for usage.
     */
    public LogEnum createInstance(String value)
    {
        return new LogEnum(value);
    }

}
