package org.systemsbiology.remotecontrol;

import java.util.*;

/**
 * org.systemsbiology.remotecontrol.IOutputListener
 * User: steven
 * Date: Jun 9, 2010
 */
public interface IOutputListener {

    public static final IOutputListener[] DEFAULT_ARRAY = {CommandOutputLogger.INSTANCE};
    public static final List<IOutputListener> DEFAULT_LISTENERS = new ArrayList<IOutputListener>(Arrays.asList(DEFAULT_ARRAY));

    public static final IOutputListener[] EMPTY_ARRAY = {};

    public void onCompletion(int status, String out);

    public void onOutput(String out);

    public void onErrorOutput(String out);

}
