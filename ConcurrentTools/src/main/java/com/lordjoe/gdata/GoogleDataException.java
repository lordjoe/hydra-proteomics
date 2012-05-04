package com.lordjoe.gdata;

import sun.org.mozilla.javascript.internal.*;

/**
 * com.lordjoe.gdata.GoogleDataException
 * version of DataApiException for the GoogleSpreadSheet implementation
 * User: steven
 * Date: 4/11/12
 */
public class GoogleDataException extends WrappedException {
    public static final GoogleDataException[] EMPTY_ARRAY = {};

    protected GoogleDataException(Throwable throwable) {
        super(throwable);
    }
}
