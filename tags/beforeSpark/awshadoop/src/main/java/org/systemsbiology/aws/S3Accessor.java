package org.systemsbiology.aws;

import org.systemsbiology.common.*;

import java.io.*;

/**
 * org.systemsbiology.aws.S3Accessor
 * User: steven
 * Date: 12/7/11
 */
public class S3Accessor implements IFileSystem {
    public static final S3Accessor[] EMPTY_ARRAY = {};

    private final S3Bucket m_Bucket;

    public S3Accessor(final S3Bucket pBucket) {
        m_Bucket = pBucket;
    }

    public S3Accessor(final String pBucket) {
        this(new S3Bucket(pBucket));
    }

    public S3Bucket getBucket() {
        return m_Bucket;
    }

    /**
     * some file systems simply delete emptydirectories - others allow them
     * @return
     */
    public boolean isEmptyDirectoryAllowed()
    {
        return false;
    }



    @Override
    public void copyFromFileSystem(final String hdfsPath, final File localPath) {
        String name = getBucket().getName();
        AWSUtilities.uploadFile(name, hdfsPath, localPath);
    }

    /**
     * delete a directory and all enclosed files and directories
     *
     * @param hdfsPath !null path
     * @return true on success
     */
    @Override
    public boolean expunge(final String hdfsPath) {
        String name = getBucket().getName();
        AWSUtilities.expunge(name,hdfsPath);
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
        String name = getBucket().getName();
        return  AWSUtilities.exists(name, hdfsPath);

    }

    /**
     * length of the file
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return
     */
    @Override
    public long fileLength(final String hdfsPath) {

        String name = getBucket().getName();
        return  AWSUtilities.fileLength(name, hdfsPath);

    }

    /**
     * print the current file system location
     *
     * @return !null string
     */
    @Override
    public String pwd() {
        return null;
    }

    /**
     * create a directory if ot does not exist
     *
     * @param hdfsPath !null path
     * @return true on success
     */
    @Override
    public boolean mkdir(final String hdfsPath) {
        return true;  // we can always do this
    }

    /**
     * list subfiles
     *
     * @param hdfsPath !null path probably exists
     * @return !null list of enclused file names - emptu if file of not exists
     */
    @Override
    public String[] ls(final String hdfsPath) {
        String name = getBucket().getName();
        String[] items =  AWSUtilities.listFiles(name,hdfsPath);
        return items;
    }

    /**
     * true if the file exists and is a directory
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return
     */
    @Override
    public boolean isDirectory(final String hdfsPath) {
        String name = getBucket().getName();
        return AWSUtilities.isDirectory(name,hdfsPath);
       }

    /**
     * true if the file exists and is a file
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return
     */
    @Override
    public boolean isFile(final String hdfsPath) {
        String name = getBucket().getName();
        return AWSUtilities.isFile(name, hdfsPath);
       }

    /**
     * delete the file
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return true if file is deleted
     */
    @Override
    public boolean deleteFile(final String hdfsPath) {
        String name = getBucket().getName();
        AWSUtilities.deleteFile(name,hdfsPath);
        return true;
    }

    /**
     * guarantee the existance of a file on the remote system
     *
     * @param hdfsPath !null path - on the remote system
     * @param file     !null exitsing file
     */
    @Override
    public void guaranteeFile(final String hdfsPath, final File file) {
        throw new UnsupportedOperationException("Fix This"); // ToDo

    }

    /**
     * guarantee the existance of a directory on the remote system
     *
     * @param hdfsPath !null path - on the remote system
     */
    @Override
    public void guaranteeDirectory(final String hdfsPath) {
        if(isFile(hdfsPath))
            throw new IllegalArgumentException("desired directory " + hdfsPath+ " is file"); // ToDo change
       // throw new UnsupportedOperationException("Fix This"); // ToDo
    }

    /**
     * open a file for reading
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return !null stream
     */
    @Override
    public InputStream openFileForRead(final String hdfsPath) {
        String name = getBucket().getName();
        return AWSUtilities.openFileS3(name,hdfsPath);
     }

    /**
     * open a file for writing
     *
     * @param hdfsPath !null path -
     * @return !null stream
     */
    @Override
    public OutputStream openFileForWrite(final String hdfsPath) {
        String name = getBucket().getName();
        return AWSUtilities.openFileForWrite(name,hdfsPath);

    }

    /**
     * write text to a remote file system
     *
     * @param hdfsPath !null remote path
     * @param content  !null test content
     */
    @Override
    public void writeToFileSystem(final String hdfsPath, final String content) {
        String name = getBucket().getName();
        AWSUtilities.uploadText(name, hdfsPath, content);

    }

    /**
     * write text to a remote file system
     *
     * @param hdfsPath !null remote path
     * @param content  !null file to write
     */
    @Override
    public void writeToFileSystem(final String hdfsPath, final File content) {
        String name = getBucket().getName();
        AWSUtilities.uploadFile(name,hdfsPath,content);

    }

    /**
     * write text to a remote file system
     *
     * @param hdfsPath !null remote path
     * @param content  !null file to write
     */
    @Override
    public void writeToFileSystem(final String hdfsPath, final InputStream content) {
        throw new UnsupportedOperationException("Fix This"); // ToDo
           //  AWSUtilities.uploadStream(getBucket().getName(),hdfsPath,content);
    }

    /**
     * read a remote file as text
     *
     * @param hdfsPath !null remote path to an existing file
     * @return content as text
     */
    @Override
    public String readFromFileSystem(final String hdfsPath) {
        String name = getBucket().getName();
        return AWSUtilities.getFileData(name, hdfsPath);
     }
}
