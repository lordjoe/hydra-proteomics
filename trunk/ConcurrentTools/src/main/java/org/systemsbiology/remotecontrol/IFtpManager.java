package org.systemsbiology.remotecontrol;


import com.jcraft.jsch.*;

import java.io.*;

/**
 * org.systemsbiology.remotecontrol.IFtpManager
 * User: steven
 * Date: Jun 4, 2010
 */
public interface IFtpManager {
    public static final IFtpManager[] EMPTY_ARRAY = {};

    public String getHome();
    
    public void quit();

    public void exit();

    public void lcd(String path) throws SftpException;

    public void cd(String path) throws SftpException;

    public void put(String src, String dst) throws SftpException;

    public void put(String src, String dst, int mode) throws SftpException;

    public void put(String src, String dst, 
            SftpProgressMonitor monitor) throws SftpException;

    public void put(String src, String dst, 
                    SftpProgressMonitor monitor, int mode) throws SftpException;

    public void put(InputStream src, String dst) throws SftpException;

    public void put(InputStream src, String dst, int mode) throws SftpException;

    public void put(InputStream src, String dst, 
                            SftpProgressMonitor monitor) throws SftpException;

    public void put(InputStream src, String dst, 
                                    SftpProgressMonitor monitor, int mode) throws SftpException;

    public OutputStream put(String dst) throws SftpException;

    public OutputStream put(String dst, int mode) throws SftpException;

    public OutputStream put(String dst, SftpProgressMonitor monitor, int mode) throws SftpException;

    public OutputStream put(String dst, SftpProgressMonitor monitor, int mode, long offset) throws SftpException;

    public void get(String src, String dst) throws SftpException;

    public void get(String src, String dst,
                                            SftpProgressMonitor monitor) throws SftpException;

    public void get(String src, String dst,
                                                    SftpProgressMonitor monitor, int mode) throws SftpException;

    public void get(String src, OutputStream dst) throws SftpException;

    public void get(String src, OutputStream dst,
                                                            SftpProgressMonitor monitor) throws SftpException;

    public void get(String src, OutputStream dst,
                                                                     SftpProgressMonitor monitor, int mode, long skip) throws SftpException;

    public InputStream get(String src) throws SftpException;

    public InputStream get(String src, SftpProgressMonitor monitor) throws SftpException;


    public InputStream get(String src, SftpProgressMonitor monitor, long skip) throws SftpException;

    public java.util.Vector ls(String path) throws SftpException;

    public void rename(String oldpath, String newpath) throws SftpException;

    public void rm(String path) throws SftpException;

    public void chown(int uid, String path) throws SftpException;

    public void chmod(int permissions, String path) throws SftpException;

    public void rmdir(String path) throws SftpException;

    public void mkdir(String path) throws SftpException;

    public SftpATTRS stat(String path) throws SftpException;

    public SftpATTRS lstat(String path) throws SftpException;

    public String pwd() throws SftpException;

    public String lpwd();
}
