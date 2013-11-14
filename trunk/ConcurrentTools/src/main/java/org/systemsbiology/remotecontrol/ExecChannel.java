package org.systemsbiology.remotecontrol;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.remotecontrol.FTPChannel
 * User: steven
 * Date: Jun 2, 2010
 */
public class ExecChannel extends AbstractChannel {
    public static final ExecChannel[] EMPTY_ARRAY = {};


    public ExecChannel(final RemoteSession pSession) {
        super(pSession);
    }

    public ChannelExec getExecChannel() {
        return (ChannelExec) getBaseChannel();
    }

    @Override
    protected Class<? extends Channel> getChannelClass() {
        return ChannelExec.class;
    }

    @Override
    protected String getChannelType() {
        return "exec";
    }

    public String execCommand(String command, List<IOutputListener> listeners) {
        //noinspection UnusedDeclaration
        FileInputStream fis = null;
        try {
            ChannelExec channel = getExecChannel();
            ChannelOutput out = new ChannelOutput(this);
            channel.setCommand(command);
            int answer = out.runUntilEnd(listeners);
            if (answer != 0)
                return out.getErrors();
            return out.getOutput();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    @SuppressWarnings("UnusedDeclaration")
    public void sendFile(File sent, String dst) {
        FileInputStream fis = null;
        String lfile = sent.getName();
        try {
            ChannelExec channel = getExecChannel();
            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            setConnected(true);

            if (checkAck(in) != 0) {
                throw new IllegalStateException("problem"); // ToDo change
            }

            // send "C0644 filesize filename", where filename should not include '/'
            long filesize = sent.length();
            String command = "C0644 " + filesize + " ";
            if (lfile.lastIndexOf('/') > 0) {
                command += lfile.substring(lfile.lastIndexOf('/') + 1);
            } else {
                command += lfile;
            }
            command += "\n";
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                throw new IllegalStateException("problem"); // ToDo change
            }

            // send a content of lfile
            fis = new FileInputStream(sent);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fis.read(buf, 0, buf.length);
                if (len <= 0) break;
                out.write(buf, 0, len); //out.flush();
            }
            fis.close();
            fis = null;
            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            if (checkAck(in) != 0) {
                throw new IllegalStateException("problem"); // ToDo change
            }
            out.close();

            setConnected(false);

        } catch (Exception e) {
            System.out.println(e);
            try {
                if (fis != null) fis.close();
            } catch (Exception ignored) {
            }
        }
    }

    static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

}