package org.systemsbiology.hadoop;

import org.apache.hadoop.io.*;

/**
 * org.systemsbiology.hadoop.IPartialComparable
 * interface used by Hadoop keys to allow grouping
 * User: steven
 * Date: May 21, 2010
 */
public interface IPartialComparable extends WritableComparable {
    public static final IPartialComparable[] EMPTY_ARRAY = {};

    /**
     * generate the ordering for a key sort based on the base key - all itens sorting to equal
     *   appear in the same group even when values are different
     * @param o1  !null object
     * @param o2   !null object
     * @return   0 for same or -1,1
     */
    public int compareBase(IPartialComparable o1,IPartialComparable o2) ;

    /**
     * generate the ordering for a key sort based on the value - within a group items will
     * sort in this order
     * @param o1  !null object
     * @param o2   !null object
     * @return   0 for same or -1,1
     */
    public int compareValue(IPartialComparable o1,IPartialComparable o2) ;

}
