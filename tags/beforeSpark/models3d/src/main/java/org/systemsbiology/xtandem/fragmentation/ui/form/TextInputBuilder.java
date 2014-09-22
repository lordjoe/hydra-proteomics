package org.systemsbiology.xtandem.fragmentation.ui.form;

import java.io.*;

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

    @Override
    public void addOtherAttributes(final Appendable out, final Object[] data) {
        super.addOtherAttributes(out, data);
        try {
            if(getValue() != null)  {
                out.append(" value=\"" + getValue());
                 out.append("\" ");

            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }
}