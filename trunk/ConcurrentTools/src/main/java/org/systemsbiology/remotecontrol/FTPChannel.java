package org.systemsbiology.remotecontrol;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.remotecontrol.FTPChannel
 * User: steven
 * Date: Jun 2, 2010
 */
public class FTPChannel extends AbstractChannel implements IFtpManager {
    public static final FTPChannel[] EMPTY_ARRAY = {};


    public FTPChannel(final RemoteSession pSession) {
        super(pSession);
    }

    public ChannelSftp getFtpChannel() {
        return (ChannelSftp) getBaseChannel();
    }

    @Override
    protected Class<? extends Channel> getChannelClass() {
        return ChannelSftp.class;
    }

    @Override
    protected String getChannelType() {
        return "sftp";
    }


    @Override
    public void quit() {
        getFtpChannel().quit();
    }

    @Override
    public void exit() {

        getFtpChannel().exit();
    }

    @Override
    public void lcd(final String path) throws SftpException {
        getFtpChannel().lcd(path);

    }

    @Override
    public void cd(final String path) throws SftpException {
        getFtpChannel().cd(path);

    }

    @Override
    public void put(final String src, final String dst) throws SftpException {
        getFtpChannel().put(src, dst);

    }

    @Override
    public void put(final String src, final String dst, final int mode) throws SftpException {
        getFtpChannel().put(src, dst, mode);

    }

    @Override
    public void put(final String src, final String dst, final SftpProgressMonitor monitor) throws SftpException {
        getFtpChannel().put(src, dst, monitor);

    }

    @Override
    public void put(final String src, final String dst, final SftpProgressMonitor monitor, final int mode) throws SftpException {
        getFtpChannel().put(src, dst, monitor, mode);

    }

    @Override
    public void put(final InputStream src, final String dst) throws SftpException {
        getFtpChannel().put(src, dst);

    }

    @Override
    public void put(final InputStream src, final String dst, final int mode) throws SftpException {
        getFtpChannel().put(src, dst, mode);

    }

    @Override
    public void put(final InputStream src, final String dst, final SftpProgressMonitor monitor) throws SftpException {
        getFtpChannel().put(src, dst);

    }

    @Override
    public void put(final InputStream src, final String dst, final SftpProgressMonitor monitor, final int mode) throws SftpException {
        getFtpChannel().put(src, dst, monitor, mode);

    }

    @Override
    public OutputStream put(final String dst) throws SftpException {
        return getFtpChannel().put(dst);

    }

    @Override
    public OutputStream put(final String dst, final int mode) throws SftpException {
        return getFtpChannel().put(dst, mode);

    }

    @Override
    public OutputStream put(final String dst, final SftpProgressMonitor monitor, final int mode) throws SftpException {
        return getFtpChannel().put(dst, monitor, mode);

    }

    @Override
    public OutputStream put(final String dst, final SftpProgressMonitor monitor, final int mode, final long offset) throws SftpException {
        return getFtpChannel().put(dst, monitor, mode, offset);

    }

    @Override
    public void get(final String src, final String dst) throws SftpException {
        getFtpChannel().get(src, dst);

    }

    @Override
    public void get(final String src, final String dst, final SftpProgressMonitor monitor) throws SftpException {
        getFtpChannel().get(src, dst, monitor);

    }

    @Override
    public void get(final String src, final String dst, final SftpProgressMonitor monitor, final int mode) throws SftpException {
        getFtpChannel().get(src, dst, monitor, mode);

    }

    @Override
    public void get(final String src, final OutputStream dst) throws SftpException {
        getFtpChannel().get(src, dst);

    }

    @Override
    public void get(final String src, final OutputStream dst, final SftpProgressMonitor monitor) throws SftpException {
        getFtpChannel().get(src, dst, monitor);

    }

    @Override
    public void get(final String src, final OutputStream dst, final SftpProgressMonitor monitor, final int mode, final long skip) throws SftpException {
        getFtpChannel().get(src, dst, monitor, mode, skip);

    }

    @Override
    public InputStream get(final String src) throws SftpException {
        return getFtpChannel().get(src);

    }

    @Override
    public InputStream get(final String src, final SftpProgressMonitor monitor) throws SftpException {
        return getFtpChannel().get(src, monitor);
    }

    @Override
    public InputStream get(final String src, final SftpProgressMonitor monitor, final long skip) throws SftpException {
        return getFtpChannel().get(src, monitor, skip);

    }

    @Override
    public Vector ls(final String path) throws SftpException {
        return getFtpChannel().ls(path);
    }

    @Override
    public void rename(final String oldpath, final String newpath) throws SftpException {
        getFtpChannel().rename(oldpath, newpath);

    }

    @Override
    public void rm(final String path) throws SftpException {
        getFtpChannel().rm(path);

    }

    @Override
    public void chown(final int uid, final String path) throws SftpException {
        getFtpChannel().chown(uid, path);

    }

    @Override
    public void chmod(final int permissions, final String path) throws SftpException {
        getFtpChannel().chmod(permissions, path);

    }

    @Override
    public void rmdir(final String path) throws SftpException {
        getFtpChannel().rmdir(path);

    }

    @Override
    public void mkdir(final String path) throws SftpException {
        getFtpChannel().mkdir(path);

    }

    @Override
    public SftpATTRS stat(final String path) throws SftpException {
        return getFtpChannel().stat(path);
    }

    @Override
    public SftpATTRS lstat(final String path) throws SftpException {
        return getFtpChannel().lstat(path);
    }

    @Override
    public String pwd() throws SftpException {
        return getFtpChannel().pwd();

    }

    @Override
    public String lpwd() {
        return getFtpChannel().lpwd();

    }

    @Override
    public String getHome() {
        try {
            return getFtpChannel().getHome();
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }

    }
}
