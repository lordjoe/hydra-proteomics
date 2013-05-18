package org.systemsbiology.hadoop;

import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.permission.*;
import org.apache.hadoop.security.*;
import org.systemsbiology.remotecontrol.*;

import java.io.*;
import java.security.*;
import java.util.*;

/**
 * org.systemsbiology.hadoop.HDFSAccessor
 * Code for accessing a remote file system
 *
 * @author Steve Lewis
 * @date Nov 15, 2010
 */
public class HDFSAsUserAccessor implements IHDFSFileSystem {
    public static HDFSAsUserAccessor[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = HDFSAsUserAccessor.class;

    public static final FsPermission ALL_READ = new FsPermission("766");
    public static final FsPermission ALL_ACCESS = new FsPermission("777");

    public static boolean canAllRead(FsPermission p) {
        FsAction otherAction = p.getOtherAction();
        switch (otherAction) {
            case ALL:
            case READ:
            case READ_EXECUTE:
            case READ_WRITE:
                return true;
            default:
                return false;

        }
    }


    private static UserGroupInformation g_CurrentUserGroup;

    private static UserGroupInformation getCurrentUserGroup() {
        if (g_CurrentUserGroup == null) {
            final String user = RemoteUtilities.getUser();
            g_CurrentUserGroup
                    = UserGroupInformation.createRemoteUser(user);

        }
        return g_CurrentUserGroup;
    }


    public static boolean isHDFSAccessible() {

        IHDFSFileSystem access = null;
        final String host = RemoteUtilities.getHost();
        final int port = RemoteUtilities.getPort();
        final String user = RemoteUtilities.getUser();
        //     RemoteUtilities.getPassword()
        String connStr = host + ":" + port + ":" + user + ":" + RemoteUtilities.getPassword();

        final String userDir = "/user/" + user;

        try {
            UserGroupInformation ugi = getCurrentUserGroup();
            ugi.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {

                    Configuration conf = new Configuration();
                    conf.set("fs.defaultFS", "hdfs://" + host + ":" + port);
                    //       conf.set("fs.defaultFS", "hdfs://" + host + ":" + port + userDir);
                    conf.set("hadoop.job.ugi", user);

                    FileSystem fs = FileSystem.get(conf);

                    Path udir = new Path(userDir);
                    fs.setPermission(udir, ALL_ACCESS);


//                    fs.createNewFile(new Path(userDir + "/test"));
//
//                    FileStatus[] status = fs.listStatus(new Path("/user/" + user));
//                    for (int i = 0; i < status.length; i++) {
//                        System.out.println(status[i].getPath());
//                    }
                    return null;

                }
            });
        } catch (Exception e) {
            return false;
        }

        if (true)
            return true;
        return true;
        // never get here
//
//        UserGroupInformation currentUser = null;
//        try {
//            Configuration conf = HDFSAccessor.getSharedConfiguration();
//            UserGroupInformation.setConfiguration(conf);
//            File keyTab = RemoteUtilities.getKeyTabFile();
//            String canonicalPath = keyTab.getCanonicalPath();
//            SecurityUtil.login(conf, canonicalPath, "dfs.namenode.kerberos.principal");
//            currentUser = UserGroupInformation.getCurrentUser();
//
//            //    if(kt != null)
//            //         UserGroupInformation.loginUserFromKeytab(user,kt.getPath());
//            currentUser = UserGroupInformation.getCurrentUser();
//        } catch (IOException e) {
//            throw new UnsupportedOperationException(e);
//        }
//
//          Boolean ret = gConditionToAvailability.get(connStr);
//        if (ret == null) {
//            try {
//                access = HDFSAccessor.getFileSystem(host, port);
//                ret = Boolean.TRUE;
//            } catch (Exception e) {
//                ret = Boolean.FALSE;
//            }
//
////             try {
////                 new FTPWrapper(RemoteUtilities.getUser(), RemoteUtilities.getPassword(), RemoteUtilities.getHost());
////                 ret = Boolean.TRUE;
////             }
////             catch (Exception e) {
////                 ret = Boolean.FALSE;
////
////             }
//            gConditionToAvailability.put(connStr, ret);
//        }
//        return ret;
    }

    private final Configuration m_Config;


    protected HDFSAsUserAccessor() {
        this(RemoteUtilities.getHost(), RemoteUtilities.getPort(), RemoteUtilities.getUser());
    }


    protected HDFSAsUserAccessor(Configuration config) {
        m_Config = config;

    }

    protected HDFSAsUserAccessor(final String host, final int port) {
        this(host, port, RemoteUtilities.getUser());
    }


//    public HDFSAccessor( String host) {
//          this(host,RemoteUtilities.getPort());
//      }

    protected HDFSAsUserAccessor(final String host, final int port, final String user) {
        if (port <= 0)
            throw new IllegalArgumentException("bad port " + port);
        String connectString = "hdfs://" + host + ":" + port + "/";
        final String userDir = "/user/" + user;
        m_Config = new Configuration();
        m_Config.set("fs.defaultFS", "hdfs://" + host + ":" + port + userDir);
        m_Config.set("hadoop.job.ugi", user);


        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {
                    getDFS();
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect on " + connectString + " because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }
    }

    /**
     * some file systems simply delete emptydirectories - others allow them
     *
     * @return
     */
    public boolean isEmptyDirectoryAllowed() {
        return true;
    }


    public FileSystem getDFS() {
        try {
            return FileSystem.get(m_Config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void copyFromFileSystem(final String hdfsPath, final File localPath) {

        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {
                    FileSystem fileSystem = getDFS();

                    Path src = new Path(hdfsPath);

                    Path dst = new Path(localPath.getAbsolutePath());

                    fileSystem.copyToLocalFile(src, dst);
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }


    }


    @Override
    public void writeToFileSystem(final String hdfsPath, final File localPath) {
        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {
                    FileSystem fileSystem = getDFS();

                    Path dst = new Path(hdfsPath);

                    Path src = new Path(localPath.getAbsolutePath());

                    fileSystem.copyFromLocalFile(src, dst);
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }

    }


    /**
     * create a directory if ot does not exist
     *
     * @param hdfsPath !null path
     * @return true on success
     */
    @Override
    public boolean mkdir(final String hdfsPath) {
        return false;
    }

    /**
     * true if the file exists and is a directory
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return
     */
    @Override
    public boolean isDirectory(final String hdfsPath) {
        final boolean[] retHolder = new boolean[1];
        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {
                    FileSystem fileSystem = getDFS();

                    Path dst = new Path(hdfsPath);

                    boolean directory = fileSystem.isDirectory(dst);
                    retHolder[0] = directory;
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }
        return retHolder[0];
    }

    /**
     * true if the file exists and is a file
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return
     */
    @Override
    public boolean isFile(final String hdfsPath) {
        final boolean[] retHolder = new boolean[1];
        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {
                    FileSystem fileSystem = getDFS();

                    Path dst = new Path(hdfsPath);

                    boolean directory = fileSystem.isFile(dst);
                    retHolder[0] = directory;
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }
        return retHolder[0];

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
    }

    /**
     * delete a directory and all enclosed files and directories
     *
     * @param hdfsPath !null path
     * @return true on success
     */
    @Override
    public boolean expunge(final String hdfsPath) {
        final boolean[] retHolder = new boolean[1];
        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {
                    FileSystem fs = getDFS();

                    Path src = new Path(hdfsPath);


                    if (!fs.exists(src)) {
                        retHolder[0] = true;
                        return null;
                    }
                    // break these out
                    if (fs.getFileStatus(src).isDir()) {
                        boolean doneOK = fs.delete(src, true);
                        doneOK = !fs.exists(src);
                        retHolder[0] = doneOK;
                        return null;
                    }
                    if (fs.isFile(src)) {
                        boolean doneOK = fs.delete(src, false);
                        retHolder[0] = doneOK;
                        return null;
                    }
                    throw new IllegalStateException("should be file of directory if it exists");

                }
            });

        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }
        return retHolder[0];
    }

    /**
     * true if the file esists
     *
     * @param hdfsPath
     * @return
     */
    @Override
    public boolean exists(final String hdfsPath) {
        final boolean[] retHolder = new boolean[1];
        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {
                    FileSystem fileSystem = getDFS();

                    Path dst = new Path(hdfsPath);

                    boolean directory = fileSystem.exists(dst);
                    retHolder[0] = directory;
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }
        return retHolder[0];


    }

    /**
     * true if the file exists
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return
     */
    @Override
    public long fileLength(final String hdfsPath) {
        final long[] retHolder = new long[1];
        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {
                    FileSystem fs = getDFS();

                    if (!exists(hdfsPath))
                        return null;
                    Path src = new Path(hdfsPath);
                    ContentSummary contentSummary = fs.getContentSummary(src);
                    if (contentSummary == null)
                        return null;
                    retHolder[0] = contentSummary.getLength();
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }
        return retHolder[0];

    }

    /**
     * delete the file
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return true if file is deleted
     */
    @Override
    public boolean deleteFile(String hdfsPath) {
        final FileSystem fs = getDFS();
        try {
            Path src = new Path(hdfsPath);
            fs.delete(src, false);
            return fs.exists(src);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * guarantee the existance of a file on the remote system
     *
     * @param hdfsPath !null path - on the remote system
     * @param file     !null exitsing file
     */
    @Override
    public void guaranteeFile(String hdfsPath, File file) {
        final FileSystem fs = getDFS();
        Path src = new Path(hdfsPath);


        try {
            if (fs.exists(src))
                return;
            this.writeToFileSystem(hdfsPath, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * guarantee the existance of a directory on the remote system
     *
     * @param hdfsPath !null path - on the remote system
     */
    @Override
    public void guaranteeDirectory(String hdfsPath) {
        Path src = new Path(hdfsPath);


        guaranteeDirectory(src);

    }

    private void guaranteeDirectory(final Path src) {
        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {
                    FileSystem fs = getDFS();

                    if (fs.exists(src))
                        return null;
                    if (!fs.isFile(src)) {
                        return null;
                    } else {
                        fs.delete(src, false);   // drop a file we want a directory
                    }
                    fs.setPermission(src, FULL_ACCESS);
                    fs.mkdirs(src, FULL_ACCESS);
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }

    }

    /**
     * open a file for reading
     *
     * @param hdfsPath !null path - probably of an existing file
     * @return !null stream
     */
    @Override
    public InputStream openFileForRead(String hdfsPath) {
        if (isFileNameLocal(hdfsPath)) {
            try {
                return new FileInputStream(hdfsPath); // better be local
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);

            }
        }
        if (true)
            throw new UnsupportedOperationException("Fix This"); // ToDo
        final FileSystem fs = getDFS();
        Path src = new Path(hdfsPath);
        try {
            return fs.open(src);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public static boolean isFileNameLocal(final String hdfsPath) {
        return hdfsPath.contains(":") && !hdfsPath.startsWith("s3n:") && !hdfsPath.startsWith("res:");
    }

    public FsPermission getPermissions(String hdfsPath) {
        return getPermissions(new Path(hdfsPath));
    }

    private FsPermission getPermissions(final Path src) {
        final FsPermission[] retHolder = new FsPermission[1];
        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {
                public Void run() throws Exception {
                    FileSystem fs = getDFS();
                    if (fs.exists(src)) {
                        FileStatus fileStatus = fs.getFileStatus(src);
                        retHolder[0] = fileStatus.getPermission();
                    }
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }
        return retHolder[0];
    }


    public void setPermissions(String hdfsPath, String premissions) {
        Path src = new Path(hdfsPath);
        final FsPermission p = new FsPermission(premissions);
        setPermissions(src, p);

    }

    private void setPermissions(final Path src, final FsPermission p) {
        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {
                public Void run() throws Exception {
                    FileSystem fs = getDFS();
                    if (fs.exists(src))
                        fs.setPermission(src, p);
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }
    }

    /**
     * open a file for writing
     *
     * @param hdfsPath !null path -
     * @return !null stream
     */
    @Override
    public OutputStream openFileForWrite(String hdfsPath) {
        if (isFileNameLocal(hdfsPath)) {
            try {
                return new FileOutputStream(hdfsPath); // better be local
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }

        Path src = new Path(hdfsPath);
        return openFileForWrite(src);
    }

    private OutputStream openFileForWrite(final Path src) {
        if (true)
            throw new UnsupportedOperationException("Fix This"); // ToDo
        final FileSystem fs = getDFS();
        try {
            Path parent = src.getParent();
            guaranteeDirectory(parent);
            return FileSystem.create(fs, src, FULL_FILE_ACCESS);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * print the current file system location
     *
     * @return !null string
     */
    @Override
    public String pwd() {
        final FileSystem fs = getDFS();
        return absolutePath(fs.getWorkingDirectory());
    }

    public static String absolutePath(Path p) {
        if (p == null)
            return "";
        StringBuilder sb = new StringBuilder();
        Path parentPath = p.getParent();
        if (parentPath == null)
            return "/";
        sb.append(absolutePath(parentPath));
        if (sb.length() > 1)
            sb.append("/");
        sb.append(p.getName());
        return sb.toString();
    }

    /**
     * list subfiles
     *
     * @param hdfsPath !null path probably exists
     * @return !null list of enclused file names - emptu if file of not exists
     */
    @Override
    public String[] ls(final String hdfsPath) {
        final FileSystem fs = getDFS();
        try {
            FileStatus[] statuses = fs.listStatus(new Path(hdfsPath));
            if (statuses == null)
                return new String[0];
            List<String> holder = new ArrayList<String>();
            for (int i = 0; i < statuses.length; i++) {
                FileStatus statuse = statuses[i];
                String s = statuse.getPath().getName();
                holder.add(s);
            }
            String[] ret = new String[holder.size()];
            holder.toArray(ret);
            return ret;
        } catch (IOException e) {
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
        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {
                    FileSystem fs = getDFS();
                    Path src = new Path(hdfsPath);
                    Path parent = src.getParent();
                    guaranteeDirectory(parent);
                    OutputStream os = FileSystem.create(fs, src, FULL_FILE_ACCESS);
                    FileUtilities.writeFile(os, content);
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
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
        final String[] retHolder = new String[1];
        UserGroupInformation uig = getCurrentUserGroup();
        try {
            uig.doAs(new PrivilegedExceptionAction<Void>() {

                public Void run() throws Exception {
                    FileSystem fs = getDFS();
                    Path src = new Path(hdfsPath);
                    InputStream is = fs.open(src);
                    String ret = FileUtilities.readInFile(is);
                    retHolder[0] = ret;
                    return null;

                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to copyFromFileSystem because " + e.getMessage() +
                    " exception of class " + e.getClass(), e);
        }
        return retHolder[0];
    }


    public static void main(String[] args) {
        HDFSAsUserAccessor dfs = new HDFSAsUserAccessor("glados", 9000, "slewis");
        final Path path = dfs.getDFS().getHomeDirectory();
        System.out.println(path);
        String[] ls = dfs.ls("/user/howdah/JXTandem/data/HoopmanSample/");
        for (int i = 0; i < ls.length; i++) {
            String l = ls[i];
            System.out.println(l);
        }
        //      dfs.expunge(RemoteUtilities.getDefaultPath() + "/JXTandem/data/largerSample");
        //     dfs.expunge(RemoteUtilities.getDefaultPath() + "/JXTandem/data/largeSample");

        //       dfs.writeToFileSystem( "/user/howdah/moby/",new File("E:/moby"));
        dfs.copyFromFileSystem("/user/howdah/JXTandem/data/HoopmanSample/Only_yeast.2012_04_115_15_32_53.t.xml.scans", new File("E:/ForSteven/HoopmanSample/Only_yeast.2012_04_115_15_32_53.t.xml.scans"));
    }
}
