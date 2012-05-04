package org.systemsbiology.remotecontrol;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.remotecontrol.ChannelOutput
 * User: steven
 * Date: Jun 4, 2010
 */
public class ChannelOutput {
    public static final ChannelOutput[] EMPTY_ARRAY = {};


    private final ExecChannel m_Channel;
    private String m_Errors;
    private String m_Output;
    private int m_Status;

    public ChannelOutput(final ExecChannel pChannel) {
        m_Channel = pChannel;
    }

    public static final String CACHE_REQUEST = "Store key in cache? (y/n)";

    public int runUntilEnd(List<IOutputListener> listeners) {
        ChannelExec execChannel = m_Channel.getExecChannel();
        int errorLength = 0;
        int inputLength = 0;
        boolean OK_RSA = false;
        try {
            InputStream in = execChannel.getInputStream();
            InputStream err = execChannel.getErrStream();

            ByteArrayOutputStream bsout = new ByteArrayOutputStream();
            ByteArrayOutputStream bserr = new ByteArrayOutputStream();

            execChannel.connect();
            execChannel.start();
            OutputStream outputStream = execChannel.getOutputStream();
            PrintStream psout = new PrintStream(outputStream);
            boolean closed = execChannel.isClosed();
            while (!execChannel.isClosed()) {
                boolean dataRead = false;
                if (readStream(bsout, in)) {
                    dataRead = true;
                    if (listeners != null && !listeners.isEmpty()) {
                        String s = new String(bsout.toByteArray());

                        if(!OK_RSA && s.contains(CACHE_REQUEST)) {
                            psout.println("yes");
                            OK_RSA = true;
                        }

                        if(s.length() > inputLength) {
                            String newRead = s.substring(inputLength);
                            inputLength = newRead.length();
                            for(IOutputListener l : listeners)
                                l.onOutput(newRead);
                        }
                    }

                }
                if (readStream(bserr, err)) {
                    dataRead = true;
                    if (listeners != null && !listeners.isEmpty()) {
                          String s = new String(bserr.toByteArray());
                          if(s.length() > errorLength) {
                              String newRead = s.substring(errorLength);
                              errorLength = s.length();
                              for(IOutputListener l : listeners)
                                  l.onErrorOutput(newRead);
                          }
                      }

                }
                if (!dataRead)
                    Thread.sleep(100);
            }
            psout.close();
            m_Status = execChannel.getExitStatus();
            m_Output = new String(bsout.toByteArray());
            m_Errors = new String(bserr.toByteArray());
//            if (m_Errors.length() > 0) {
//
//                System.out.println("REMOTE OUTPUT MESSAGE !!!");
//                System.out.println(m_Errors);
//            }

            return m_Status;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean readStream(final ByteArrayOutputStream pBserr, final InputStream in) throws IOException {
        byte[] tmp = new byte[1024];
        if (in.available() == 0)
            return false;

        while (in.available() > 0) {
            int i = in.read(tmp, 0, 1024);
            if (i > 0) {
                pBserr.write(tmp, 0, i);
            }
        }
        return true;

    }

    public String getErrors() {
        return m_Errors;
    }

    public String getOutput() {
        return m_Output;
    }

    public int getStatus() {
        return m_Status;
    }
}
