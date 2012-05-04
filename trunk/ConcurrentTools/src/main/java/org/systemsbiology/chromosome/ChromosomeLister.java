package org.systemsbiology.chromosome;

import org.systemsbiology.common.AbstractConditionalOperator;
import org.systemsbiology.common.IFilter;
import org.systemsbiology.picard.*;

import java.util.*;

/**
 * org.systemsbiology.chromosome.ChromosomeLister
 * written by Steve Lewis
 *
 * on Apr 7, 2010
 */
public class ChromosomeLister extends AbstractConditionalOperator<IExtendedSamRecord> {
    public static final ChromosomeLister[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ChromosomeLister.class;

    private final Set<String> m_Chromosomes = new HashSet<String>();

    public ChromosomeLister() {
        super("ChromosomeLister",IFilter.TRUE_FILTER);
    }

    @Override
    public void operate(IExtendedSamRecord item, Object... added) {
        String chr = item.getMateReferenceName();
        String chrs = item.getReferenceName();
        if(!"*".equals(chrs))
            m_Chromosomes.add(chrs);
        if(!"*".equals(chr) && !"=".equals(chr) )
            m_Chromosomes.add(chrs);


    }

    @Override
    public String toString() {

        String[] items = m_Chromosomes.toArray(new String[0]);
        Arrays.sort(items);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            sb.append("," + item);
        }
        return sb.toString();
    }

    @Override
    public void reset() {

    }

    @Override
    public void finish(Object... added) {

        String[] items = m_Chromosomes.toArray(new String[0]);
        Arrays.sort(items);
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            System.out.println(item);
        }


    }
}
