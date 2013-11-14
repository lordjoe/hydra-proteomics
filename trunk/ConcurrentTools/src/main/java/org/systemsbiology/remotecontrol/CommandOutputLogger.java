package org.systemsbiology.remotecontrol;

import java.util.*;

/**
 * org.systemsbiology.remotecontrol.CommandOutputLogger
 * User: steven
 * Date: Jun 9, 2010
 */
public class CommandOutputLogger implements IOutputListener {
    public static final CommandOutputLogger[] EMPTY_ARRAY = {};

    public static final CommandOutputLogger INSTANCE = new CommandOutputLogger();


    private CommandOutputLogger() {
    }

    @Override
    public void onCompletion(final int status, final String out) {
        if (status == 0)
            System.out.println(out);
        else
            System.err.println(out);


    }

    @Override
    public void onOutput(final String out) {
        System.out.print(out);

    }

    @Override
    public void onErrorOutput(final String out) {
        System.err.print(out);

    }
}
