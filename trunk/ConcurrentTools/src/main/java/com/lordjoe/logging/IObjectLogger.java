package com.lordjoe.logging;

import java.io.*;

/**
 * com.lordjoe.logging.IObjectLogger
 *   This logger logs Objects not Text
 * @author Steve Lewis
 * @date Jun 30, 2008
 */
public interface IObjectLogger<T> 
{
    public static IObjectLogger[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = IObjectLogger.class;


    public LogEnum getLogType();

    /**
     * return sample of type
     * @return  noin-null array
     */
    public T[] getTypeArray();
    /**
     * clear the contents
     */
    public void clear();


    /**
     * save the contents
     * @param dest non-null writeable  destination file
     */
    public void save(File dest);

    /**
     * restore the contents
     * @param dest non-null existing readable source file
     */
    public void restore(File source);


    /**
     * build a LoggedObject and log it
     * @param data non-null data
     */
    public void log(T data);

    /**
     *  log a complete package
     * @param data  non-null data
     */
    public void log(LoggedObject<T> data);

    /**
     * reeturn all objects passing all filters
     * @param filters o or more filters
     * @return  non-null array of objects
     */
   // public LoggedObject<T>[] getObjects(ILoggedObjectFilter<T>... filters);


    public void addObjectLogChangedListener(ObjectLogChangedListener added);

    public void removeObjectLogChangedListener(ObjectLogChangedListener added);

    
}
