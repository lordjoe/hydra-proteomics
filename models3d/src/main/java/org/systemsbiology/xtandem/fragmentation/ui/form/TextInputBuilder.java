package org.systemsbiology.xtandem.fragmentation.ui.form;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.form.TextInputBuilder
 * User: Steve
 * Date: 11/21/12
 */
public class TextInputBuilder extends AbstractInputHtml {
    public static final TextInputBuilder[] EMPTY_ARRAY = {};


    public TextInputBuilder(final HTMLFormBuillder page, final String name ) {
        super(page, name, HtmlInputTypes.text);
    }


}