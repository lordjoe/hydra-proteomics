package org.systemsbiology.xtandem.fragmentation.ui;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.IHtmlFragmentHolder
 * User: Steve
 * Date: 6/25/12
 */
public interface IHtmlFragmentHolder extends  IHtmlFragmentBuilder {
    public static final IHtmlFragmentHolder[] EMPTY_ARRAY = {};

    public void addBuilder(IHtmlFragmentBuilder added);

    /**
     * make a builder which adds the string
     * @param added  !null stringto add
     */
    public void addString(String added);

}
