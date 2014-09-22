package org.systemsbiology.common;

/**
 * org.systemsbiology.common.BasicCounter
 * written by Steve Lewis
 * on Apr 7, 2010
 */
public class BasicCounter<T> extends AbstractConditionalOperator<T> implements ICounter<T>
{
    public static final BasicCounter[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = BasicCounter.class;

    private long m_Count;

    public BasicCounter(String name) {
        super(name);
    }


    public BasicCounter(String name,IFilter<T> pFilter) {
        this(name);
        m_Filter = pFilter;
    }

    @Override
    public String toString() {

        return getName() + ":" + getCount();
    }



    public boolean perhapsCount(T test) {
        perhapsOperate(test);
        return true;
    }

    public long getCount() {
        return m_Count;
    }

    public void operate(T item, Object... added) {
         m_Count++;
     }

    public void reset() {
         m_Count = 0;

     }

    @Override
    public void finish(Object... added) {
        // nothing to do
    }
}
