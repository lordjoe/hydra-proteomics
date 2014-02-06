package com.lordjoe.utilities;

import javax.annotation.*;

/**
 * simply counts the items visited
 * @param <T>
 */
public class TypedCounter<T>  implements TypedVisitor<T>   {
    private int count;
    /**
     * @param pT interface implemented by the visitor pattern
     */
    @Override
    public void visit(@Nonnull final T pT) {
       count++;
    }

    public int getCount() {
        return count;
    }
}
