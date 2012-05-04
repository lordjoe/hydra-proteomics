package org.systemsbiology.sam;

import net.sf.samtools.*;
import org.systemsbiology.common.*;
import org.systemsbiology.picard.*;

/**
 * org.systemsbiology.sam.StrandFlagFilter
 * written by Steve Lewis
 * on Apr 7, 2010
 */
public class StrandFlagFilter implements IFilter<IExtendedSamRecord> {
    public static final StrandFlagFilter[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = StrandFlagFilter.class;

    public static final StrandFlagFilter NEG_NEG = new StrandFlagFilter(ReadDirections.NegNeg);
    public static final StrandFlagFilter POS_NEG = new StrandFlagFilter(ReadDirections.PosNeg);
    public static final StrandFlagFilter NEG_POS = new StrandFlagFilter(ReadDirections.NegPos);
    public static final StrandFlagFilter POS_POS = new StrandFlagFilter(ReadDirections.PosPos);

    private final ReadDirections m_Directions;

    public StrandFlagFilter(ReadDirections pDirections) {
        m_Directions = pDirections;
    }

    public boolean accept(IExtendedSamRecord test, Object... added) {
          if(!test.getReadPairedFlag())
             return false;
          return m_Directions == test.getReadDirections();


   }

}
