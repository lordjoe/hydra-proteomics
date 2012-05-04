package org.systemsbiology.sam;

import org.systemsbiology.common.*;
import org.systemsbiology.picard.*;

/**
* org.systemsbiology.sam.VisitorFilterAdapter
* Converts a visitor into a filter
* written by Steve Lewis
* on Apr 22, 2010
*/
public class VisitorFilterAdapter implements IFilter<IExtendedSamRecord>
{
    private final ISamRecordVisitor m_Visitor;

    public VisitorFilterAdapter(ISamRecordVisitor pVisitor) {
        m_Visitor = pVisitor;
    }

    public ISamRecordVisitor getVisitor() {
        return m_Visitor;
    }

    /**
     * determine whether this matches a specific filter
     *
     * @param test  - non-null test item
     * @param added any added data
     * @return tru for a match
     */
    public boolean accept(IExtendedSamRecord test, Object... added) {
        return m_Visitor.visit(test, added);
    }
}
