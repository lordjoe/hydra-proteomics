package com.lordjoe.utilities;

import javax.annotation.*;

/**
 * com.lordjoe.utilities.TypedPredicate
 * interface representing a single boolean decision - frequently
 * use to implement a filter
 * User: Steve
 * Date: 9/25/13
 */
public interface  TypedPredicate<T> {

    /**
     *
     * @param pT
     * @param otherdata - implementation specific and usually blank
     * @return  what the implementation does
     */
   public boolean apply(@Nonnull T pT,Object... otherdata);
}
