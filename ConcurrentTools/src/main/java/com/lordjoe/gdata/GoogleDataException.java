package com.lordjoe.gdata;

/**
 * com.lordjoe.gdata.GoogleDataException
 * version of DataApiException for the GoogleSpreadSheet implementation
 * User: steven
 * Date: 4/11/12
 */
public class GoogleDataException extends RuntimeException {
    public static final GoogleDataException[] EMPTY_ARRAY = {};

    protected GoogleDataException(Throwable throwable) {
        super(throwable);
    }
}
