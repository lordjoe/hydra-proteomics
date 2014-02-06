package com.lordjoe.utilities;

import javax.annotation.*;

/**
 * uk.ac.ebi.pride.spectracluster.util.TypedVisitor
 * interface tp visit a specific type
 * User: Steve
 * Date: 9/25/13
 */
public interface TypedVisitor<T> {

    /**
     *
     * @param pT interface implemented by the visitor pattern
        */
   public void visit(@Nonnull T pT);
}
