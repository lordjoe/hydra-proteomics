package org.systemsbiology.xtandem.fragmentation.ui.form;

import java.io.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.form.SubmitButtonBuilder
 * User: Steve
 * Date: 6/25/12
 */
public class SubmitButtonBuilder extends AbstractInputHtml {
    public static final SubmitButtonBuilder[] EMPTY_ARRAY = {};


    public SubmitButtonBuilder(final HTMLFormBuillder page) {
       super(page,"Submit",HtmlInputTypes.submit);
        setBreakAppendedAtEnd(false);
     }
    public SubmitButtonBuilder(final HTMLFormBuillder page,String name) {
        super(page,"Submit",HtmlInputTypes.submit);
        setValue(name);
        setBreakAppendedAtEnd(false);
      }


    @Override
    public void addOtherAttributes(final Appendable out, final Object[] data) {
        super.addOtherAttributes(out, data);   // todo add code
        try {
            if(getValue() != null)  {    // todo make a value holder superclass
                out.append(" value=\"" + getValue());
                 out.append("\" ");

            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * this is a button - no label
     * @param out
     * @param data
     */
    @Override
    protected void addNameText(final Appendable out, final Object... data) {
      //  super.addNameText(out, data);   // todo add code
    }

    @Override
    protected void addFieldText(final Appendable out, final Object... data) {
        super.addFieldText(out, data);   // todo add code
    }

    @Override
    public void addStartText(final Appendable out, final Object... data) {
        super.addStartText(out, data);   // todo add code
    }

    //    @Override
//    protected void addNameText(final Appendable out, final Object... data) {
//        // do nothing buttons have names
//    }






}
