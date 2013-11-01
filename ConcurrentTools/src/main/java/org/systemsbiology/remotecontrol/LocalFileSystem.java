package org.systemsbiology.remotecontrol;

import com.lordjoe.utilities.*;
import org.systemsbiology.common.*;

import java.io.*;

/**
 * org.systemsbiology.remotecontrol.LocalFileSystem
 * implementation of IFileSystem in the  local machine
 * User: Steve
 * Date: May 13, 2011
 */
public class LocalFileSystem implements IFileSystem {

    private File m_BaseDirectory;

    public LocalFileSystem(final File pBaseDirectory) {
        m_BaseDirectory = pBaseDirectory;
    }

    public LocalFileSystem() {
        this(new File(System.getProperty("user.dir")));
    }

    /**
     * true of you aer running on a local disk
     *
     * @return as above
     */
    @Override
    public boolean isLocal() {
          return true;
    }

    /**
     * shut down all running sessions   on local file systems
     * this may be a noop but for remote systems shut all connections
     */
    @Override
    public void disconnect() {
        // this is a noop

    }

    /**
     * some file systems simply delete emptydirectories - others allow them
     * @return
     */
    public boolean isEmptyDirectoryAllowed()
    {
        return true;
    }

    public File getBaseDirectory() {
        return m_BaseDirectory;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setBaseDirectory(final File pBaseDirectory) {
        m_BaseDirectory = pBaseDirectory;
    }

    @Override
    public void copyFromFileSystem(final String hdfsPath, final File localPath) {
        File src = new File(hdfsPath);
        if (src.equals(localPath))
            return;
        if (!src.exists())
            return; // or throw an exception
        FileUtilities.copyFile(src, localPath);
    }

    /**
     * delete a directory and all enclosed files and directories
     *
     * @param hdfsPath !null path
     * @return true on success
     */
    @Override
    public boolean expunge(final String hdfsPath) {
        FileUtilities.expungeDirectory(hdfsPath);
        return true;
    }

    /**
     * true if the file esists
     *
     * @param hdfsPath
     * @return
     */
    @Override
    public boolean exists(final String hdfsPath) {
        return new File(hdfsPath).exists();
    }

    /**
     * length of the file
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return
     */
    @Override
    public long fileLength(final String hdfsPath) {
        return new File(hdfsPath).length();
    }

    /**
     * print the current file system location
     *
     * @return !null string
     */
    @Override
    public String pwd() {
        return getBaseDirectory().getAbsolutePath();
    }

    /**
     * create a directory if ot does not exist
     *
     * @param hdfsPath !null path
     * @return true on success
     */
    @Override
    public boolean mkdir(final String hdfsPath) {
        return new File(hdfsPath).mkdirs();
    }

    /**
     * list subfiles
     *
     * @param hdfsPath !null path probably exists
     * @return !null list of enclused file names - emptu if file of not exists
     */
    @Override
    public String[] ls(final String hdfsPath) {
        final File file = new File(hdfsPath);
        //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
        String psth = file.getAbsolutePath();
        String[] ret = file.list();
        if (ret == null)
            ret = new String[0];
        return ret;
    }

    /**
     * true if the file exists and is a directory
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return
     */
    @Override
    public boolean isDirectory(final String hdfsPath) {
        return new File(hdfsPath).isDirectory();
    }

    /**
     * true if the file exists and is a file
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return
     */
    @Override
    public boolean isFile(final String hdfsPath) {
        return new File(hdfsPath).isFile();
    }

    /**
     * delete the file
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return true if file is deleted
     */
    @Override
    public boolean deleteFile(final String hdfsPath) {
        return new File(hdfsPath).delete();
    }

    /**
     * guarantee the existance of a file on the remote system
     *
     * @param hdfsPath !null path - on the remote system
     * @param file     !null exitsing file
     */
    @Override
    public void guaranteeFile(final String hdfsPath, final File file) {
        //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
        String realPath = hdfsPath.replace("\\", "/");
        String filePath = file.getAbsolutePath().replace("\\", "/");
        if (hdfsPath.equals(filePath))
            return;
        File dst = new File(hdfsPath);
        FileUtilities.copyFile(file, dst);
    }

    /**
     * guarantee the existance of a directory on the remote system
     *
     * @param hdfsPath !null path - on the remote system
     */
    @Override
    public void guaranteeDirectory(final String hdfsPath) {
        if (!exists(hdfsPath))
            mkdir(hdfsPath);
    }

    /**
     * open a file for reading
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return !null stream
     */
    @SuppressWarnings("UnusedDeclaration")
     public InputStream openFileForRead(final String hdfsPath) {
        try {
            return new FileInputStream(new File(hdfsPath));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * open a file for writing
     *
     * @param hdfsPath !null path -
     * @return !null stream
     */
     public OutputStream openFileForWrite(final String hdfsPath) {
        try {
            return new FileOutputStream(new File(hdfsPath));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * write text to a remote file system
     *
     * @param hdfsPath !null remote path
     * @param content  !null test content
     */
    @Override
    public void writeToFileSystem(final String hdfsPath, final String content) {
        FileUtilities.writeFile(hdfsPath, content);
    }

    /**
     * write text to a remote file system
     *
     * @param hdfsPath !null remote path
     * @param content  !null file to write
     */
    @Override
    public void writeToFileSystem(final String hdfsPath, final File content) {
        File dst = new File(hdfsPath);
        if (content.equals(dst))
            return;
        FileUtilities.copyFile(content, dst);

    }

    /**
     * write text to a remote file system
     *
     * @param hdfsPath !null remote path
     * @param content  !null file to write
     */
    @Override
    public void writeToFileSystem(final String hdfsPath, final InputStream content) {
        File file = new File(hdfsPath);
        try {
            OutputStream os = new FileOutputStream(file);
            FileUtilities.copyStream(content, os);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    /**
     * read a remote file as text
     *
     * @param hdfsPath !null remote path to an existing file
     * @return content as text
     */
    @Override
    public String readFromFileSystem(final String hdfsPath) {
        File file = new File(hdfsPath);
        return FileUtilities.readInFile(file);
    }
}
