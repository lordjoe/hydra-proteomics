package org.systemsbiology.xtandem.fragmentation.ui;

import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.fragmentation.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.HTMLPageBuillder
 * User: Steve
 * Date: 6/25/12
 */
public class ThreeDModelBuillder extends AbstractHtmlFragmentHolder {
    public static final ThreeDModelBuillder[] EMPTY_ARRAY = {};

    private final ProteinFragmentationDescription m_Fragments;
    private final Map<ProteinFragment, IAminoAcidAtLocation[]> m_FragmentLocations;
    private PDBObject m_Model;

    public ThreeDModelBuillder(final IHtmlFragmentHolder page, ProteinFragmentationDescription pfd) {
        super(page);
        m_Fragments = pfd;
        m_Model = pfd.getModel();
        if (m_Model == null)
            throw new IllegalArgumentException("no 3d model");
        ProteinFragment[] frags = pfd.getFragments();
        m_FragmentLocations = pfd.getAminoAcidLocations();
        int index = 0;
        if (m_FragmentLocations.isEmpty()) {
            new HTMLHeaderHolder(this, "No Fragments found in 3D model", 1);
            pfd.setModel(null);
        }
        else {
            int totalFragments = frags.length;
            int modeledFragments = m_FragmentLocations.size();
            if (totalFragments > 3 * modeledFragments) {
                new HTMLHeaderHolder(this, "Dropping bad Model " + getModel().getFile() +
                        " modeled  " + modeledFragments + " of " + totalFragments + " fragments "
                        , 1);
                pfd.setModel(null);
                return;
            }
            new HTMLHeaderHolder(this, "Using Model " + getModel().getFile() +
                    " modeled  " + modeledFragments + " of " + totalFragments + " fragments "
                    , 1);
            new SingleTagBuillder(this, "p");
            ThreeDModelCompositeAppletBuillder mfall = new ThreeDModelCompositeAppletBuillder(this, pfd, m_FragmentLocations);
            // make individtual models
            new SingleTagBuillder(this, "p");
//            new HTMLHeaderHolder(this, "Coverage Plot", 2);
//            ThreeDModelCoverageAppletBuillder cfall = new ThreeDModelCoverageAppletBuillder(this, pfd);
//            // make individtual models
//            new SingleTagBuillder(this, "p");
//            for (ProteinFragment pf : m_FragmentLocations.keySet()) {
//                ThreeDModelAppletBuillder mf = new ThreeDModelAppletBuillder(this, pfd, m_FragmentLocations.get(pf), index++);
//            }
        }
    }


    public ProteinFragmentationDescription getFragments() {
        return m_Fragments;
    }

    public PDBObject getModel() {
        return m_Model;
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
//        try {
//            out.append("<body>");
//            out.append("\n");
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//
//        }

    }

    @Override
    public void addEndText(final Appendable out, final Object... data) {
//        try {
//            out.append("</body>");
//            out.append("\n");
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//
//        }

    }


}
