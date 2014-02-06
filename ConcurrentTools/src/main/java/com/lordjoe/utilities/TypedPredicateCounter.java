package com.lordjoe.utilities;

import javax.annotation.*;

/**
 * simply counts the items  satisfying a predicate
 * @param <T>
 */
public class TypedPredicateCounter<T>  extends  TypedCounter<T> implements TypedVisitor<T>   {
    private final TypedPredicate<T> satisties;


    public TypedPredicateCounter(final TypedPredicate<T> pSatisties) {
        satisties = pSatisties;
    }

    /**
     * @param pT interface implemented by the visitor pattern
     */
    @Override
    public void visit(@Nonnull final T pT) {
        if(satisties.apply(pT) )
           super.visit(pT);   // add to count
    }


}
