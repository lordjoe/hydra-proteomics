package org.systemsbiology.aws;

import org.systemsbiology.remotecontrol.*;

import java.util.*;

/**
 * org.systemsbiology.aws.S3Directory
 * User: steven
 * Date: Aug 10, 2010
 */
public class S3Directory extends S3File {
    public static final S3Directory[] EMPTY_ARRAY = {};


    private boolean m_Loaded;
    private final Map<String, S3File> m_Files = new HashMap<String, S3File>();

    public S3Directory(final S3Bucket pBucket, final S3Directory pParent, String name) {
        super(pBucket, pParent, name, true);
    }


    @Override
    public S3File[] getFiles() {
        if (!m_Loaded) {
            String path = getPath();
            if(path != null && path.length() > 0)
                path += "/";
            String s = getBucket().getName();
            HDFSFile[] files = AWSUtilities.getDirectories(s, path);
            buildDirectoryTree(files);
            m_Loaded = true;
        }
        S3File[] files = m_Files.values().toArray(S3File.EMPTY_ARRAY);
        Arrays.sort(files);
        return files;
    }

    public S3File getFileByName(String name)
    {
        String[] dirs = name.split("/");
        return getFileByName(dirs,0);
    }

    protected S3File getFileByName(String[] name,int index)
    {
        if(index > name.length -1)
            return this;
        String s = name[index];
        S3File file = getFile(s);
        if(file == null)
            return null;
        if(file instanceof S3Directory) {
            return ((S3Directory)file).getFileByName(name,index + 1);
        }
        if(index == name.length -1)
            return file;
        return null;
    }

    public Date getModificationDate()
    {
       long ret = 0;
        S3File[] files  = getFiles();
        for (int i = 0; i < files.length; i++) {
            S3File file = files[i];
            ret = Math.max( ret,file.getModificationDate().getTime());
        }
        return new Date(ret);
    }




    public long getSize() {
        long ret = 0;
        S3File[] files  = getFiles();
        for (int i = 0; i < files.length; i++) {
            S3File file = files[i];
            ret =+ file.getSize();
        }
        return ret;
    }



    protected void buildDirectoryTree(HDFSFile[] files) {
        for (int i = 0; i < files.length; i++) {
            HDFSFile file = files[i];
            guaranteeFile(file);
        }
    }

    protected void guaranteeFile(HDFSFile file) {
        String parent = file.getParent();
        if (parent.equals(getPath())) {
            addFile(file);
        }
        else {
            S3Directory parentDir = guaranteeParentDir(parent);
            parentDir.addFile(file);
        }

    }


    protected S3Directory guaranteeParentDir(String file) {

        int index = file.lastIndexOf("/");
        if (index > -1) {
            String name = file.substring(index + 1);
            String dir = file.substring(0, index);
            S3Directory parent = guaranteeParentDir(dir);
            S3Directory added = (S3Directory) parent.getFile(name);
            if (added == null) {
                added = new S3Directory(getBucket(), parent, name);
                parent.addFile(added);
            }
            return added;
        }
        else {
            S3Directory dir = (S3Directory) getFile(file);
            if (dir == null) {
                dir = new S3Directory(getBucket(), this, file);
                addFile(dir);
            }
            return dir;
        }

    }


    protected void addFile(S3File added) {
        m_Files.put(added.getName(), added);
    }


    public void addFile(HDFSFile added) {

        if (added.isFile()) {
            S3DataFile value = new S3DataFile(getBucket(), this, added.getName());
            value.setLength(added.getLength());
            value.setModificationDate(added.getModificationDate());
            addFile(value);
        }
        else {
            String s = added.getName();
            S3Directory value = new S3Directory(getBucket(), this, s.replace("_$folder$",""));
              addFile(value);

        }
    }


    public void removeFile(String removed) {
        m_Files.remove(removed);
    }


    public S3File getFile(String key) {
        return m_Files.get(key);
    }


}
