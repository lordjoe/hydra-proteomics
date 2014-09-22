package org.systemsbiology.common;

/**
 * org.systemsbiology.common.IConditionalOperator
 * written by Steve Lewis
 * on Apr 7, 2010
 */
public interface IConditionalOperator<T> extends Resetable, Finishable
{
    public static final IConditionalOperator[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = IConditionalOperator.class;


    public IFilter<T> getFilter();

    public void setFilter(IFilter<T> filter);

    public boolean perhapsOperate(T item,Object... added);

    public void operate(T item,Object... added);

    /**
     * return to base conditions
     */
    public void reset();

    /**
     * perform any operations on finish
     * @param added
     */
    public void finish(Object... added);


}