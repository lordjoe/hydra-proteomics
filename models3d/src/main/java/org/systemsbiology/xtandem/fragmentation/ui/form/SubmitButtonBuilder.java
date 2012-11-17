package org.systemsbiology.xtandem.fragmentation.ui.form;

/**
 * org.systemsbiology.xtandem.fragmentation.ui.form.SubmitButtonBuilder
 * User: Steve
 * Date: 6/25/12
 */
public class SubmitButtonBuilder extends AbstractInputHtml {
    public static final SubmitButtonBuilder[] EMPTY_ARRAY = {};


    public SubmitButtonBuilder(final HTMLFormBuillder page) {
       super(page,"Submit",HtmlInputTypes.submit);
     }

    public SubmitButtonBuilder(final HTMLFormBuillder page,String name) {
       super(page,name,HtmlInputTypes.submit);
     }







}
