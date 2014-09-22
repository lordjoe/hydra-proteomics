package org.systemsbiology.hadoop;

/**
 * org.systemsbiology.hadoop.ResourceFileSystem
 * User: steven
 * Date: 2/1/12
 */


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.permission.*;
import org.apache.hadoop.util.*;

import java.io.*;
import java.net.*;

/**
 * Implementation of the Hadoop FileSystem.  Looking for resources
 * relatice to a setable base class specified in the configuration as
 * a property  org.systemsbiology.hadoop.ResourceFileSystem.BaseClass
 * <p/>
 * This implementation allows a user to access HDFS over HTTP via a Hoop server.
 */
public class ResourceFileSystem extends FileSystem {

    public static final String BASE_CLASS_PROPERTY_NAME = "org.systemsbiology.hadoop.ResourceFileSystem.BaseClass";
    public static final String SCHEME_PROPERTY_NAME = "fs.res.impl";

    private URI m_Uri;
    private Class m_BaseClass = Object.class;

    /**
     * Called after a new FileSystem instance is constructed.
     *
     * @param name a m_Uri whose authority section names the host, port, etc. for this FileSystem
     * @param conf the configuration
     */
    @Override
    public void initialize(URI name, Configuration conf) throws IOException {
        super.initialize(name, conf);
        String s = conf.get(BASE_CLASS_PROPERTY_NAME);
        if (s != null) {
            try {
                m_BaseClass = Class.forName(s);
            }
            catch (ClassNotFoundException e) {
                throw new RuntimeException(e);

            }
        }
        try {
            m_Uri = new URI(name.getScheme() + "://",null,null,"/","");  //   URI(String scheme, String host, String path, String fragment)
        }
        catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
    }

    public Class getBaseClass() {
        return m_BaseClass;
    }

    public void setBaseClass(final Class pBaseClass) {
        m_BaseClass = pBaseClass;
    }

    /**
     * Returns a URI whose scheme and authority identify this FileSystem.
     *
     * @return the URI whose scheme and authority identify this FileSystem.
     */
    @Override
    public URI getUri() {
        return m_Uri;
    }

    /**
     * Hoop subclass of the <code>FSDataInputStream</code>.
     * <p/>
     * This implementation does not support the
     * <code>PositionReadable</code> and <code>Seekable</code> methods.
     */
    protected static class ResourceFSDataInputStream extends FilterInputStream implements Seekable, PositionedReadable {

        protected ResourceFSDataInputStream(InputStream in, int bufferSize) {
            super(new BufferedInputStream(in, bufferSize));
        }

        @Override
        public int read(long position, byte[] buffer, int offset, int length) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void readFully(long position, byte[] buffer, int offset, int length) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void readFully(long position, byte[] buffer) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void seek(long pos) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getPos() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean seekToNewSource(long targetPos) throws IOException {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Opens an FSDataInputStream at the indicated Path.
     * </p>
     * IMPORTANT: the returned <code><FSDataInputStream/code> does not support the
     * <code>PositionReadable</code> and <code>Seekable</code> methods.
     *
     * @param f          the file name to open
     * @param bufferSize the size of the buffer to be used.
     */
    @Override
    public FSDataInputStream open(Path f, int bufferSize) throws IOException {

        InputStream is = getPathAsStream(f);
        if(is == null)
            return null;
        return new FSDataInputStream(new ResourceFSDataInputStream(is, bufferSize));
    }

    protected InputStream getPathAsStream(final Path f) {
        if(pathExists(f))  {
            String res = pathToResourceName(f);
            Class baseClass = getBaseClass();
            return baseClass.getResourceAsStream(res);

        }
        else {
            return null;
        }
    }

    protected String pathToResourceName(final Path pF) {
        String pathName = pF.toString();
        if(!pathName.startsWith("res://"))
            throw new UnsupportedOperationException("bad path " + pathName);
        pathName  = pathName.substring("res://".length());
        return pathName;
    }



    /**
     * Converts a <code>FsPermission</code> to a Unix string symbolic representation (ie: '-rwxr--r--')
     *
     * @param p the permission.
     * @return the Unix string symbolic reprentation.
     */
    private String permissionToString(FsPermission p) {
        return (p == null) ? "default" : "-" + p.getUserAction().SYMBOL + p.getGroupAction().SYMBOL +
                p.getOtherAction().SYMBOL;
    }

    /**
     * Opens an FSDataOutputStream at the indicated Path with write-progress
     * reporting.
     * <p/>
     * IMPORTANT: The <code>Progressable</code> parameter is not used.
     *
     * @param f           the file name to open
     * @param permission
     * @param overwrite   if a file with this name already exists, then if true,
     *                    the file will be overwritten, and if false an error will be thrown.
     * @param bufferSize  the size of the buffer to be used.
     * @param replication required block replication for the file.
     * @param blockSize
     * @param progress
     * @throws IOException
     * @see #setPermission(Path, FsPermission)
     */
    @Override
    public FSDataOutputStream create(Path f, FsPermission permission, boolean overwrite, int bufferSize,
                                     short replication, long blockSize, Progressable progress) throws IOException {
        throw new UnsupportedOperationException("This File System is readonly");
    }


    /**
     * Append to an existing file (optional operation).
     * <p/>
     * IMPORTANT: The <code>Progressable</code> parameter is not used.
     *
     * @param f          the existing file to be appended.
     * @param bufferSize the size of the buffer to be used.
     * @param progress   for reporting progress if it is not null.
     * @throws IOException
     */
    @Override
    public FSDataOutputStream append(Path f, int bufferSize, Progressable progress) throws IOException {
        throw new UnsupportedOperationException("This File System is readonly");
    }

    /**
     * Renames Path src to Path dst.  Can take place on local fs
     * or remote DFS.
     */
    @Override
    public boolean rename(Path src, Path dst) throws IOException {
        throw new UnsupportedOperationException("This File System is readonly");
    }

    /**
     * Delete a file.
     *
     * @deprecated Use delete(Path, boolean) instead
     */
    @Deprecated
    @Override
    public boolean delete(Path f) throws IOException {
        throw new UnsupportedOperationException("This File System is readonly");
    }

    /**
     * Delete a file.
     *
     * @param f         the path to delete.
     * @param recursive if path is a directory and set to
     *                  true, the directory is deleted else throws an exception. In
     *                  case of a file the recursive can be set to either true or false.
     * @return true if delete is successful else false.
     * @throws IOException
     */
    @Override
    public boolean delete(Path f, boolean recursive) throws IOException {
        throw new UnsupportedOperationException("This File System is readonly");
    }

    /**
     * List the statuses of the files/directories in the given path if the path is
     * a directory.
     *
     * @param f given path
     * @return the statuses of the files/directories in the given patch
     * @throws IOException
     */
    @Override
    public FileStatus[] listStatus(Path f) throws IOException {
        throw new UnsupportedOperationException("Not Supported for resources");
    }

    /**
     * Set the current working directory for the given file system. All relative
     * paths will be resolved relative to it.
     *
     * @param new_dir
     */
    @Override
    public void setWorkingDirectory(Path new_dir) {
        throw new UnsupportedOperationException("Not Supported for resources");
        // workingDir = new_dir;
    }

    /**
     * Get the current working directory for the given file system
     *
     * @return the directory pathname
     */
    @Override
    public Path getWorkingDirectory() {
        Class baseClass = getBaseClass();
        return classToPath(baseClass);

    }


    public Path classToPath(Class baseClass) {
        String name = baseClass.getName();
        if (name.contains("."))
            name = name.substring(0, name.lastIndexOf("."));
        name = name.replace(".", "/");
        name = getUri().toString() + name;
        return new Path(name);
    }

    /**
     * Make the given file and all non-existent parents into
     * directories. Has the semantics of Unix 'mkdir -p'.
     * Existence of the directory hierarchy is not an error.
     */
    @Override
    public boolean mkdirs(Path f, FsPermission permission) throws IOException {
        throw new UnsupportedOperationException("Not Supported for resources");
    }

    /**
     * Return a file status object that represents the path.
     *
     * @param f The path we want information from
     * @return a FileStatus object
     * @throws FileNotFoundException when the path does not exist;
     *                               IOException see specific implementation
     */
    @Override
    public FileStatus getFileStatus(Path f) throws IOException {
        return createFileStatus(f);
    }

    /**
     * Return the current user's home directory in this filesystem.
     * The default implementation returns "/user/$USER/".
     */
    @Override
    public Path getHomeDirectory() {
        return getWorkingDirectory();
    }

    /**
     * Set owner of a path (i.e. a file or a directory).
     * The parameters username and groupname cannot both be null.
     *
     * @param p         The path
     * @param username  If it is null, the original username remains unchanged.
     * @param groupname If it is null, the original groupname remains unchanged.
     */
    @Override
    public void setOwner(Path p, String username, String groupname) throws IOException {
        throw new UnsupportedOperationException("Not Supported for resources");
    }

    /**
     * Set permission of a path.
     *
     * @param p
     * @param permission
     */
    @Override
    public void setPermission(Path p, FsPermission permission) throws IOException {
        throw new UnsupportedOperationException("Not Supported for resources");
    }

    /**
     * Set access time of a file
     *
     * @param p     The path
     * @param mtime Set the modification time of this file.
     *              The number of milliseconds since Jan 1, 1970.
     *              A value of -1 means that this call should not set modification time.
     * @param atime Set the access time of this file.
     *              The number of milliseconds since Jan 1, 1970.
     *              A value of -1 means that this call should not set access time.
     */
    @Override
    public void setTimes(Path p, long mtime, long atime) throws IOException {
        throw new UnsupportedOperationException("Not Supported for resources");
    }

    /**
     * Set replication for an existing file.
     *
     * @param src         file name
     * @param replication new replication
     * @return true if successful;
     *         false if file does not exist or is a directory
     * @throws IOException
     */
    @Override
    public boolean setReplication(Path src, short replication) throws IOException {
        return pathExists(src);
    }

    protected boolean pathExists(final Path pSrc) {
        String res = pathToResourceName(pSrc);
        URL is = getBaseClass().getResource(res);
        return is != null;
    }

    public static final int BUFFER_LENGTH = 4096;
    public long getReadLength(Path path) {
          byte[] buffer = new byte[BUFFER_LENGTH];
        if(pathExists(path))  {
          InputStream is = getPathAsStream(path);
            try {
                int read = is.read(buffer);
                long ret = 0;
                while(read > 0)  {
                    ret += read;
                    read = is.read(buffer);
                }
                return ret;
            }
            catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
        else {
            return 0;
        }
    }

    /**
     * Creates a <code>FileStatus</code> object using a JSON file-status payload
     * received from a Hoop server.
     *
     * @param json a JSON file-status payload received from a Hoop server
     * @return the corresponding <code>FileStatus</code>
     */
    private FileStatus createFileStatus(Path path) {
        if(!pathExists(path))
            return null;
        boolean isDir = false;
        long len = getReadLength(  path);
         long aTime = 0;
        long mTime = 0;
        long blockSize = 1;
        short replication = 1;
        return new FileStatus(len, isDir, replication, blockSize, mTime, aTime, null, null, null, path);
    }

}