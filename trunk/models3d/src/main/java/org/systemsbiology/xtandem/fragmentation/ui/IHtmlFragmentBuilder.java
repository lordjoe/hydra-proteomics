package org.systemsbiology.xtandem.fragmentation.ui;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.IHtmlFragmentBuilder
 * User: Steve
 * Date: 6/25/12
 */
public interface IHtmlFragmentBuilder {
    public static final IHtmlFragmentBuilder[] EMPTY_ARRAY = {};

    public void appendFragment(Appendable out,Object... data);

}
