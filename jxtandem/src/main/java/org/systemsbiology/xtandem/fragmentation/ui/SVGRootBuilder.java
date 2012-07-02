package org.systemsbiology.xtandem.fragmentation.ui;

import javax.crypto.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.SVGRootBuilder
 * User: Steve
 * Date: 6/29/12
 */
public class SVGRootBuilder extends SVGFragmentBuilder {
    public static final SVGRootBuilder[] EMPTY_ARRAY = {};
    public static final String TAG = "svg";

    private final List<ToolTipTextBuillder> m_ToolTips = new ArrayList<ToolTipTextBuillder>();

    public SVGRootBuilder(final IHtmlFragmentHolder parent ) {
        super(parent, TAG);
    }


    public SVGRootBuilder getRoot()
    {
        return this;
    }

    @Override
    public Offset getTotalOffset() {
         return getOffset();
      }



    public void addToolTip(ToolTipTextBuillder tt)  {
       m_ToolTips.add(tt);
    }

    @Override
    protected  void appendAllBuilders(final Appendable out, final Object[] data) {
        super.appendAllBuilders(out, data);
        for(ToolTipTextBuillder tt : m_ToolTips)  {
            tt.appendFragment(out,data);
        }
    }

}
