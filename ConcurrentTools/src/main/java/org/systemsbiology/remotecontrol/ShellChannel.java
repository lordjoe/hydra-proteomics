package org.systemsbiology.remotecontrol;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.remotecontrol.FTPChannel
 * User: steven
 * Date: Jun 2, 2010
 */
public class ShellChannel extends AbstractChannel {
    public static final ShellChannel[] EMPTY_ARRAY = {};

    

    public ShellChannel(final RemoteSession pSession) {
        super(pSession);
    }

    public Channel getChannel()
    {
        return (ChannelSftp)getBaseChannel();
    }

    @Override
    protected Class<? extends Channel> getChannelClass() {
        return ChannelSftp.class;
    }

    @Override
    protected String getChannelType() {
        return "shell";
    }

    public String execCommand(String command)   {
        final Channel channel = getChannel();
        command += "\n";
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
   //     byteArrayOutputStream.
    //    channel.setExtOutputStream(byteArrayOutputStream,true);

    //    channel.setInputStream(new ByteArrayInputStream());
        throw new UnsupportedOperationException("Fix This"); // ToDo
    }





}