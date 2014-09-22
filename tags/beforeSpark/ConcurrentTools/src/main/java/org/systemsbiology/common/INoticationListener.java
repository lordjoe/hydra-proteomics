package org.systemsbiology.common;

/**
 * org.systemsbiology.common.INoticationListener
 *
 * User: steven
 * Date: 11/17/11
 */
public interface INoticationListener {
    public static final INoticationListener[] EMPTY_ARRAY = {};

    /**
     * notification of something
     * @param data
     */
    public void onNotification(Object... data );
}
