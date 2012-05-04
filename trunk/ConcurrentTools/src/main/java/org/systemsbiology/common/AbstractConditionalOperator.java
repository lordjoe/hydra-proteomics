package org.systemsbiology.common;

/**
 * org.systemsbiology.common.AbstractConditionalOperator
 * written by Steve Lewis
 * on Apr 7, 2010
 */
public abstract class AbstractConditionalOperator<T> implements IConditionalOperator<T> 
{
    public static final AbstractConditionalOperator[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = AbstractConditionalOperator.class;
    protected final String m_Name;
    protected IFilter<T> m_Filter = IFilter.NULL_FILTER;

    public AbstractConditionalOperator(String name) {
        m_Name = name;
    }

    public AbstractConditionalOperator(String name,IFilter<T> filter) {
        this(name);
        m_Filter = filter;
    }

    public String getName() {
        return m_Name;
    }

    public boolean perhapsOperate(T item, Object... added) {
        if(getFilter().accept(item))  {
            operate(item,added);
            return true;
        }
        else {
            return false;
        }
    }

    public abstract void operate(T item, Object... added);

    public IFilter<T> getFilter() {
        return m_Filter;
    }

    public void setFilter(IFilter<T> pFilter) {
        m_Filter = pFilter;
    }

    public abstract void reset();

    public abstract void finish(Object... added);


}
