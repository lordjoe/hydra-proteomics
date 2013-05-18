package org.systemsbiology.hadoop;

import org.apache.hadoop.fs.permission.*;
import org.systemsbiology.common.*;

/**
 * org.systemsbiology.hadoop.IHDFSFileSystem
 *
 * @author Steve Lewis
 * @date 18/05/13
 */
public interface IHDFSFileSystem extends IFileSystem {
    public static IHDFSFileSystem[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = IHDFSFileSystem.class;


    public static final FsPermission FULL_ACCESS = new FsPermission(Short.parseShort("777", 8));
    public static final FsPermission FULL_FILE_ACCESS = new FsPermission(Short.parseShort("666", 8));



}
